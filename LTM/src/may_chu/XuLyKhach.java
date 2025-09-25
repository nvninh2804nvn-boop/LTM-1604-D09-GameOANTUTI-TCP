package may_chu;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** Xử lý 1 client: đăng ký/đăng nhập, mời/ngẫu nhiên, chơi, đổi mật khẩu, đăng xuất, lịch sử, BXH. */
public class XuLyKhach implements Runnable {
    // ====== Dữ liệu chung (tài khoản) ======
    private static final Map<String, String> accounts =
            new ConcurrentHashMap<>(QuanLyTaiKhoan.loadAccounts());

    // ====== Trạng thái cho 1 client ======
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String username;          // đã đăng nhập hay chưa
    private XuLyKhach opponent;       // handler đối thủ nếu đã ghép
    private String move;              // nước đi hiện tại

    public XuLyKhach(Socket socket) { this.socket = socket; }

    // ==== GETTERS/SETTERS để QuanLyTroChoi dùng ====
    public PrintWriter getOut() { return out; }
    public String getUsername() { return username; }
    public XuLyKhach getOpponent() { return opponent; }
    public void setOpponent(XuLyKhach opp) { this.opponent = opp; }
    public String getMove() { return move; }
    public void setMove(String m) { this.move = m; }

    @Override
    public void run() {
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            String line;
            while ((line = in.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                String cmd = p[0];

                switch (cmd) {
                    case "REGISTER": handleRegister(p); break;
                    case "LOGIN":    handleLogin(p);    break;
                    case "WHO":      handleWho();       break;
                    case "INVITE":   if (p.length>=2) QuanLyTroChoi.invite(this, p[1]); else out.println("ERROR|Thiếu tham số"); break;
                    case "ACCEPT":   if (p.length>=2) QuanLyTroChoi.acceptInvite(this, p[1]); else out.println("ERROR|Thiếu tham số"); break;
                    case "RANDOM":   QuanLyTroChoi.randomMatch(this); break;
                    case "MOVE":     if (p.length>=2) QuanLyTroChoi.handleMove(this, p[1]); else out.println("ERROR|Thiếu tham số"); break;
                    case "LEAVE":    handleLeave(); break;
                    case "CHANGE_PASS": handleChangePass(p); break;
                    case "LOGOUT":   handleLogout(); break;
                    case "HISTORY":  handleHistory(); break;
                    case "LEADERBOARD": handleLeaderboard(); break;
                    default: out.println("ERROR|Lệnh không hợp lệ");
                }
            }
        } catch (IOException ignore) {
        } finally {
            // client rơi mạng
            handleLeave(); // rời phòng + bỏ hàng chờ
            if (username != null) {
                MayChu.clients.remove(username);
                broadcastUserList();
            }
            try { socket.close(); } catch (IOException ignore2) {}
        }
    }

    /* ===================== HANDLERS ===================== */

    private void handleRegister(String[] p) {
        if (p.length < 3) { out.println("ERROR|Thiếu tham số"); return; }
        String user = p[1].trim();
        String pass = p[2];

        if (user.isEmpty() || pass.isEmpty()) { out.println("ERROR|Tài khoản/mật khẩu trống"); return; }
        if (accounts.containsKey(user)) { out.println("ERROR|Tên đăng ký đã tồn tại"); return; }

        accounts.put(user, pass);
        QuanLyTaiKhoan.saveAccounts(accounts);
        out.println("INFO|Đăng ký thành công. Vui lòng đăng nhập.");
    }

    private void handleLogin(String[] p) {
        if (p.length < 3) { out.println("ERROR|Thiếu tham số"); return; }
        String user = p[1].trim();
        String pass = p[2];

        if (!Objects.equals(accounts.get(user), pass)) {
            out.println("ERROR|Sai tên đăng nhập hoặc mật khẩu");
            return;
        }
        // chặn multi-login
        if (MayChu.clients.putIfAbsent(user, this) != null) {
            out.println("ERROR|Tài khoản đang đăng nhập ở nơi khác");
            return;
        }
        this.username = user;
        out.println("OK|Đăng nhập thành công");
        broadcastUserList();
    }

    private void handleWho() {
        if (!isLogged()) { out.println("ERROR|Bạn chưa đăng nhập"); return; }
        out.println("USER_LIST|" + String.join(",", MayChu.clients.keySet()));
    }

    private void handleChangePass(String[] p) {
        if (!isLogged()) { out.println("ERROR|Bạn chưa đăng nhập"); return; }
        if (p.length < 3) { out.println("ERROR|Thiếu tham số"); return; }
        String oldPw = p[1], newPw = p[2];

        String cur = accounts.get(username);
        if (!Objects.equals(cur, oldPw)) { out.println("ERROR|Mật khẩu cũ không đúng"); return; }
        accounts.put(username, newPw);
        QuanLyTaiKhoan.saveAccounts(accounts);
        out.println("OK|PASS_CHANGED");
    }

    private void handleLogout() {
        if (!isLogged()) { out.println("ERROR|Bạn chưa đăng nhập"); return; }
        handleLeave(); // rời phòng nếu có
        MayChu.clients.remove(username);
        this.username = null;
        out.println("LOGOUT_OK");
        broadcastUserList();
    }

    /** Thoát phòng hiện tại + bỏ hàng chờ. */
    private void handleLeave() {
        // rời phòng
        if (this.opponent != null) {
            XuLyKhach opp = this.opponent;
            this.opponent = null;
            this.move = null;
            if (opp != null) {
                opp.opponent = null;
                opp.move = null;
                opp.getOut().println("BYE|Đối thủ đã rời phòng");
            }
        }
        // bỏ hàng chờ random nếu có
        QuanLyTroChoi.removeFromQueue(this);
    }

    private void handleHistory() {
        if (!isLogged()) { out.println("ERROR|Bạn chưa đăng nhập"); return; }
        var list = QuanLyLichSu.readHistory(username);
        out.println("HISTORY|" + String.join(",", list));
    }

    private void handleLeaderboard() {
        var map = QuanLyLichSu.getLeaderboard();
        java.util.List<String> rows = new java.util.ArrayList<>();
        for (var e : map.entrySet()) rows.add(e.getKey() + ":" + e.getValue());
        out.println("LEADERBOARD|" + String.join(",", rows));
    }

    /* ===================== TIỆN ÍCH ===================== */

    private boolean isLogged() { return username != null; }

    private static void broadcastUserList() {
        String payload = String.join(",", MayChu.clients.keySet());
        for (XuLyKhach h : MayChu.clients.values()) {
            h.getOut().println("USER_LIST|" + payload);
        }
    }
}
