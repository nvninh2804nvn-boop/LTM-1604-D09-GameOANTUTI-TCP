package nguoi_choi;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class KetNoiKhach {
    private final GiaoDienTroChoi gui;

    private String host = "127.0.0.1";
    private int port = 7777;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String myName = null;
    private String currentOpponent = null;

    public KetNoiKhach(GiaoDienTroChoi gui) {
        this.gui = gui;
    }

    /* ====================================================== */
    /* =============== KẾT NỐI & GỬI / NHẬN ================= */
    /* ====================================================== */

    private synchronized void connectIfNeeded() throws IOException {
        if (socket != null && socket.isConnected() && !socket.isClosed()) return;

        socket = new Socket(host, port);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        // Thread lắng nghe server
        Thread listener = new Thread(this::listenLoop, "Client-Listen");
        listener.setDaemon(true);
        listener.start();
    }

    private void listenLoop() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                final String msg = line;
                // tất cả cập nhật UI phải đi qua EDT
                SwingUtilities.invokeLater(() -> handleMessage(msg));
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                        "Mất kết nối tới server.", "Lỗi mạng", JOptionPane.ERROR_MESSAGE);
                gui.resetGameUI();
            });
        }
    }

    public synchronized void send(String s) {
        try {
            connectIfNeeded();
            out.println(s);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Không gửi được dữ liệu tới server:\n" + e.getMessage(),
                    "Lỗi mạng", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ====================================================== */
    /* ================== LỆNH TỪ CLIENT ==================== */
    /* ====================================================== */

    public void register(String user, String pass) {
        send("REGISTER|" + user + "|" + pass);
    }

    public void login(String user, String pass) {
        this.myName = user;
        send("LOGIN|" + user + "|" + pass);
    }

    public void logout() {
        send("LOGOUT");
        myName = null;
        currentOpponent = null;
        gui.hideMainTabs();
    }

    public void sendMove(String choice) {
        send("MOVE|" + choice);
    }

    public void sendLeave() {
        send("LEAVE");
        currentOpponent = null;
        gui.resetGameUI();
    }

    public void invite(String targetUser) {
        send("INVITE|" + targetUser);
    }

    public void accept(String inviter) {
        send("ACCEPT|" + inviter);
    }

    public void randomMatch() {
        send("RANDOM");
    }

    public void requestHistory() {
        send("HISTORY");
    }

    public void requestLeaderboard() {
        send("LEADERBOARD");
    }

    /* ====================================================== */
    /* ================== XỬ LÝ TIN NHẮN ==================== */
    /* ====================================================== */

    private void handleMessage(String msg) {
        // Debug nhẹ nếu cần:
        // System.out.println("<< " + msg);

        if (msg.startsWith("OK|Đăng nhập thành công")) {
            if (myName != null) gui.setPlayerName(myName);
            return;
        }

        if (msg.startsWith("ERROR|")) {
            JOptionPane.showMessageDialog(null,
                    msg.substring(6), "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (msg.startsWith("INFO|")) {
            JOptionPane.showMessageDialog(null,
                    msg.substring(5), "Thông tin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (msg.startsWith("BYE|")) {
            JOptionPane.showMessageDialog(null,
                    msg.substring(4), "Thoát phòng", JOptionPane.INFORMATION_MESSAGE);
            currentOpponent = null;
            gui.resetGameUI();
            return;
        }

        if (msg.startsWith("USER_LIST|")) {
            String payload = msg.substring(10);
            String[] names = payload.isEmpty() ? new String[0] : payload.split(",");
            // cập nhật danh sách – bỏ chính mình
            if (gui instanceof GiaoDienTroChoi) {
                ((GiaoDienTroChoi) gui).updateUserList(
                        java.util.Arrays.stream(names)
                                .filter(n -> !n.equals(myName))
                                .toArray(String[]::new)
                );
            }
            return;
        }

        if (msg.startsWith("INVITE|")) {
            String from = msg.substring(7);
            int choice = JOptionPane.showConfirmDialog(null,
                    "Người chơi \"" + from + "\" mời bạn đấu.\nChấp nhận?",
                    "Lời mời", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                accept(from);
            }
            return;
        }

        if (msg.startsWith("START|")) {
            currentOpponent = msg.substring(6);
            gui.setOpponentName(currentOpponent);
            return;
        }

        if (msg.startsWith("RESULT|")) {
            // Định dạng server gửi: "RESULT|Bạn: X - Đối thủ: Y → Kết quả"
            String body = msg.substring(7).trim();
            ParsedResult pr = parseResultLine(body);
            if (pr != null) {
                gui.showResult(pr.myMove, pr.oppMove, pr.resultText);
            } else {
                // fallback: hiển thị nguyên chuỗi
                JOptionPane.showMessageDialog(null, body, "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        if (msg.startsWith("HISTORY|")) {
            String payload = msg.substring(8);
            String[] rows = payload.isEmpty() ? new String[0] : payload.split(",");
            gui.updateHistoryTable(rows);
            return;
        }

        if (msg.startsWith("LEADERBOARD|")) {
            String payload = msg.substring(12);
            if (payload.isEmpty()) {
                gui.updateLeaderboard(new String[0][0]);
                return;
            }
            String[] items = payload.split(",");
            List<String[]> table = new ArrayList<>();
            for (String it : items) {
                String[] kv = it.split(":");
                if (kv.length == 2) table.add(new String[]{kv[0], kv[1]});
            }
            gui.updateLeaderboard(table.toArray(new String[0][0]));
            return;
        }
    }

    /* ====================================================== */
    /* ================== TIỆN ÍCH PARSER ==================== */
    /* ====================================================== */

    private static class ParsedResult {
        String myMove;
        String oppMove;
        String resultText;
    }

    /**
     * Parse: "Bạn: ROCK - Đối thủ: PAPER → Thua"
     */
    private ParsedResult parseResultLine(String s) {
        try {
            // Tách "Bạn: X - Đối thủ: Y → Z"
            int i1 = s.indexOf("Bạn:");
            int i2 = s.indexOf("- Đối thủ:");
            int i3 = s.indexOf("→");

            if (i1 == -1 || i2 == -1 || i3 == -1) return null;

            String my = s.substring(i1 + 4, i2).trim();              // ROCK
            String opp = s.substring(i2 + 10, i3).trim();            // PAPER
            String res = s.substring(i3 + 1).replace(">", "").trim();// Thua

            ParsedResult pr = new ParsedResult();
            pr.myMove = normalizeMove(my);
            pr.oppMove = normalizeMove(opp);
            pr.resultText = res;
            return pr;
        } catch (Exception e) {
            return null;
        }
    }

    private String normalizeMove(String m) {
        m = m.toUpperCase();
        if (m.contains("ROCK") || m.contains("ĐÁ")) return "ROCK";
        if (m.contains("PAPER") || m.contains("BAO")) return "PAPER";
        if (m.contains("SCISSORS") || m.contains("KÉO")) return "SCISSORS";
        return m;
    }
}
