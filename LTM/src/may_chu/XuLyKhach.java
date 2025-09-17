package may_chu;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class XuLyKhach implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private static Map<String, String> accounts = QuanLyLichSu.loadAccounts();
    private static Queue<XuLyKhach> waitingQueue = new ConcurrentLinkedQueue<>();
    private XuLyKhach opponent;
    private String move;

    public XuLyKhach(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\\|");
                switch (parts[0]) {
                    case "REGISTER": handleRegister(parts); break;
                    case "LOGIN": handleLogin(parts); break;
                    case "MOVE": handleMove(parts[1]); break;
                    case "LEAVE": handleLeave(); break;
                    case "HISTORY": handleHistory(); break;
                    case "LEADERBOARD": handleLeaderboard(); break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + username);
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    /** Đăng ký: nếu user đã tồn tại thì cập nhật mật khẩu mới */
    private void handleRegister(String[] parts) {
        if (parts.length < 3) {
            out.println("ERROR|Đăng ký không hợp lệ"); 
            return;
        }
        accounts.put(parts[1], parts[2]); // ghi đè nếu đã tồn tại
        QuanLyLichSu.saveAccounts(accounts);
        out.println("Registered");
    }

    /** Đăng nhập */
    private void handleLogin(String[] parts) {
        if (parts.length < 3 || !accounts.containsKey(parts[1]) || !accounts.get(parts[1]).equals(parts[2])) {
            out.println("ERROR|Sai tên đăng nhập hoặc mật khẩu"); 
            return;
        }
        this.username = parts[1];
        out.println("OK|Đăng nhập thành công, chờ ghép cặp...");
        match();
    }

    /** Ghép cặp trong hàng đợi */
    private void match() {
        waitingQueue.add(this);
        while (opponent == null) {
            for (XuLyKhach other : waitingQueue) {
                if (other != this && other.opponent == null) {
                    this.opponent = other;
                    other.opponent = this;
                    waitingQueue.remove(this);
                    waitingQueue.remove(other);
                    sendInfo("Đã ghép cặp với " + opponent.username);
                    opponent.sendInfo("Đã ghép cặp với " + username);
                    break;
                }
            }
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }
    }

    /** Xử lý nước đi */
    private void handleMove(String choice) {
        this.move = choice;
        if (opponent != null && opponent.move != null) {
            String result = XuLyLuotChoi.judge(move, opponent.move);
            out.println("RESULT|Bạn: " + move + ", Đối thủ: " + opponent.move + " → " + result);
            opponent.out.println("RESULT|Bạn: " + opponent.move + ", Đối thủ: " + move + " → " + XuLyLuotChoi.judge(opponent.move, move));

            // lưu lịch sử
            QuanLyLichSu.saveHistory(username, opponent.username, result);
            QuanLyLichSu.saveHistory(opponent.username, username, XuLyLuotChoi.judge(opponent.move, move));

            // reset
            move = null; opponent.move = null;
        }
    }

    /** Thoát phòng */
    private void handleLeave() {
        if (opponent != null) {
            opponent.sendBye("Đối thủ đã rời phòng");
            opponent.opponent = null;
            opponent.move = null;
        }
        this.opponent = null;
        this.move = null;
        sendBye("Bạn đã rời phòng");
    }

    /** Lấy lịch sử */
    private void handleHistory() {
        List<String> history = QuanLyLichSu.readHistory(username);
        // format thành opponent→kết quả
        out.println("HISTORY|" + String.join(",", history));
    }

    /** Lấy leaderboard (top thắng nhiều nhất) */
    private void handleLeaderboard() {
        Map<String, Integer> board = QuanLyLichSu.getLeaderboard();
        List<String> rows = new ArrayList<>();
        for (var e : board.entrySet()) {
            rows.add(e.getKey() + ":" + e.getValue());
        }
        out.println("LEADERBOARD|" + String.join(",", rows));
    }

    private void sendBye(String msg) {
        out.println("BYE|" + msg);
    }

    private void sendInfo(String msg) {
        out.println("INFO|" + msg);
    }
}
