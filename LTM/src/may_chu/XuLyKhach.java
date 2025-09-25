package may_chu;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class XuLyKhach implements Runnable {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String username;
    private XuLyKhach opponent;
    private String move;

    public XuLyKhach(Socket socket) { this.socket = socket; }

    @Override
    public void run() {
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\\|", -1); // giữ ô trống
                String cmd = parts[0];

                switch (cmd) {
                    case "REGISTER": handleRegister(parts); break;
                    case "LOGIN":    handleLogin(parts);    break;
                    case "LOGOUT":   handleLogout();        break;

                    // Lobby / matching
                    case "RANDOM":   MayChu.qlTroChoi.requestRandomMatch(this); break;
                    case "INVITE":   handleInvite(parts);   break;   // INVITE|target
                    case "ACCEPT":   handleAccept(parts);   break;   // ACCEPT|inviter

                    // In-game
                    case "MOVE":     handleMove(parts);     break;   // MOVE|ROCK/PAPER/SCISSORS
                    case "LEAVE":    handleLeave();         break;

                    // Data
                    case "HISTORY":      handleHistory();         break;
                    case "LEADERBOARD":  handleLeaderboard();     break;

                    default: out.println("ERROR|Lệnh không hợp lệ: " + cmd);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Client disconnected: " + username);
        } finally {
            handleLogout();
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    /* ===== Auth ===== */

    private void handleRegister(String[] parts) {
        if (parts.length < 3) { out.println("ERROR|REGISTER sai cú pháp"); return; }
        boolean ok = MayChu.qlTaiKhoan.register(parts[1], parts[2]);
        out.println(ok ? "OK|Đăng ký thành công" : "ERROR|Tài khoản đã tồn tại");
    }

    private void handleLogin(String[] parts) {
        if (parts.length < 3) { out.println("ERROR|LOGIN sai cú pháp"); return; }
        boolean ok = MayChu.qlTaiKhoan.login(parts[1], parts[2]);
        if (!ok) { out.println("ERROR|Sai tên đăng nhập hoặc mật khẩu"); return; }

        this.username = parts[1];
        MayChu.clients.put(username, this);
        out.println("OK|Đăng nhập thành công");
        broadcastUserList();
    }

    private void handleLogout() {
        if (username != null) {
            // rời phòng nếu đang chơi
            handleLeave();
            MayChu.clients.remove(username);
            broadcastUserList();
            username = null;
        }
    }

    /* ===== Lobby ===== */

    private void broadcastUserList() {
        String list = String.join(",", MayChu.clients.keySet());
        for (XuLyKhach c : MayChu.clients.values()) {
            c.out.println("USER_LIST|" + list);
        }
    }

    private void handleInvite(String[] parts) {
        if (parts.length < 2) { out.println("ERROR|INVITE sai cú pháp"); return; }
        String target = parts[1];
        MayChu.qlTroChoi.invite(this, target);
    }

    private void handleAccept(String[] parts) {
        if (parts.length < 2) { out.println("ERROR|ACCEPT sai cú pháp"); return; }
        String inviter = parts[1];
        MayChu.qlTroChoi.acceptInvite(this, inviter);
    }

    /* ===== In-game ===== */

    private void handleMove(String[] parts) {
        if (parts.length < 2) { out.println("ERROR|MOVE sai cú pháp"); return; }
        MayChu.qlTroChoi.handleMove(this, parts[1]);
    }

    private void handleLeave() {
        MayChu.qlTroChoi.leave(this);
        out.println("BYE|Bạn đã rời phòng");
    }

    /* ===== Data ===== */

    private void handleHistory() {
        if (username == null) { out.println("ERROR|Chưa đăng nhập"); return; }
        List<String> history = QuanLyLichSu.readHistory(username);
        out.println("HISTORY|" + String.join(",", history));
    }

    private void handleLeaderboard() {
        Map<String, Integer> board = QuanLyLichSu.getLeaderboard(10);
        List<String> rows = new ArrayList<>();
        for (var e : board.entrySet()) rows.add(e.getKey() + ":" + e.getValue());
        out.println("LEADERBOARD|" + String.join(",", rows));
    }

    /* ===== getter/setter để QuanLyTroChoi dùng ===== */
    public void setMove(String m) { this.move = m; }
    public String getMove() { return this.move; }

    public void setOpponent(XuLyKhach opp) { this.opponent = opp; }
    public XuLyKhach getOpponent() { return this.opponent; }

    public String getUsername() { return this.username; }
    public PrintWriter getOut() { return this.out; }
}
