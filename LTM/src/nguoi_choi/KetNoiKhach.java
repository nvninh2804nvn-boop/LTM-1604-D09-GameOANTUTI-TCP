package nguoi_choi;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Client TCP: robust hơn, parse chắc tay, hỗ trợ HISTORY / LEADERBOARD, và auto-reconnect khi gửi. */
public class KetNoiKhach {
    private final Object ui;          // chấp nhận cả GiaoDienTroChoi/UngDungClient
    private String host = "127.0.0.1";
    private int port = 7777;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private volatile boolean listening = false;
    private String myName = null;

    public KetNoiKhach(Object ui) {
        this.ui = ui;
    }

    /* ===================== Public API ===================== */

    /** Đổi host/port nếu cần (gọi trước khi login). */
    public synchronized void setServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void register(String user, String pass) { send("REGISTER|" + user + "|" + pass); }

    public void login(String user, String pass) {
        this.myName = user;
        send("LOGIN|" + user + "|" + pass);
    }

    public void invite(String targetUser)   { send("INVITE|" + targetUser); }
    public void randomMatch()               { send("RANDOM"); }
    public void changePassword(String o, String n) { send("CHANGE_PASS|" + o + "|" + n); }
    public void logout()                    { send("LOGOUT"); }
    public void sendMove(String move)       { send("MOVE|" + move); }
    public void leaveRoom()                 { send("LEAVE"); }
    public void who()                       { send("WHO"); }

    // NEW: lịch sử & bảng xếp hạng
    public void requestHistory()            { send("HISTORY"); }
    public void requestLeaderboard()        { send("LEADERBOARD"); }

    /* ===================== Core network ===================== */

    /** Kết nối nếu chưa có; bật keepAlive, tcpNoDelay. */
    private synchronized void connectIfNeeded() throws IOException {
        if (socket != null && socket.isConnected() && !socket.isClosed()) return;

        socket = new Socket(host, port);
        try {
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
        } catch (SocketException ignored) {}

        in  = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

        // lắng nghe 1 thread
        listening = true;
        Thread t = new Thread(this::listenLoop, "Client-Listen");
        t.setDaemon(true);
        t.start();
    }

    /** Gửi 1 dòng tới server; nếu rơi kết nối sẽ tự reconnect 1 lần rồi gửi lại. */
    public synchronized void send(String s) {
        try {
            connectIfNeeded();
            out.println(s);
            if (out.checkError()) throw new IOException("Socket output error");
        } catch (IOException e) {
            // thử reconnect 1 lần
            safeClose();
            try {
                connectIfNeeded();
                out.println(s);
            } catch (IOException ex) {
                showError("Không gửi được tới server: " + ex.getMessage());
            }
        }
    }

    /** Vòng lắng nghe; mọi cập nhật UI chạy trên EDT. */
    private void listenLoop() {
        try {
            String line;
            while (listening && (line = in.readLine()) != null) {
                final String msg = line;
                SwingUtilities.invokeLater(() -> handleMessage(msg));
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> showError("Mất kết nối tới server."));
        } finally {
            listening = false;
            safeClose();
        }
    }

    private synchronized void safeClose() {
        try { if (in != null) in.close(); } catch (Exception ignore) {}
        try { if (out != null) out.close(); } catch (Exception ignore) {}
        try { if (socket != null) socket.close(); } catch (Exception ignore) {}
        in = null; out = null; socket = null;
    }

    /* ===================== Message handling ===================== */

    // Regex kết quả: "Bạn: ROCK, Đối thủ: PAPER → Thua"
    private static final Pattern RESULT_PAT =
            Pattern.compile("Bạn:\\s*([A-Za-zÀ-ỹĐđ_]+)\\s*,\\s*Đối thủ:\\s*([A-Za-zÀ-ỹĐđ_]+)\\s*→\\s*(.+)");

    private void handleMessage(String msg) {
        // System.out.println("<< " + msg);

        if (msg.startsWith("OK|Đăng nhập thành công")) {
            invokeIfPresent("onLoginSuccess", new Class[]{String.class}, new Object[]{myName});
            return;
        }
        if (msg.startsWith("ERROR|")) { showWarn(msg.substring(6)); return; }
        if (msg.startsWith("INFO|"))  { showInfo(msg.substring(5)); return; }

        if (msg.startsWith("USER_LIST|")) {
            String payload = msg.substring(10);
            String[] names = payload.isEmpty() ? new String[0] : payload.split(",");
            // loại mình
            List<String> list = new ArrayList<>();
            for (String n : names) if (n != null && !n.equals(myName)) list.add(n);
            // ưu tiên hàm mới onUserList(String[]) nếu có, ngược lại gọi updateUserList(String[])
            if (!invokeIfPresent("onUserList", new Class[]{String[].class}, new Object[]{list.toArray(new String[0])})) {
                invokeIfPresent("updateUserList", new Class[]{String[].class}, new Object[]{list.toArray(new String[0])});
            }
            return;
        }

        if (msg.startsWith("INVITE|")) {
            String from = msg.substring(7);
            // ưu tiên callback onInvite nếu có, else hỏi trực tiếp (giữ hành vi cũ)
            if (!invokeIfPresent("onInvite", new Class[]{String.class}, new Object[]{from})) {
                int ch = JOptionPane.showConfirmDialog(null,
                        "Người chơi \"" + from + "\" mời bạn đấu. Chấp nhận?",
                        "Lời mời", JOptionPane.YES_NO_OPTION);
                if (ch == JOptionPane.YES_OPTION) send("ACCEPT|" + from);
            }
            return;
        }

        if (msg.startsWith("START|")) {
            String opp = msg.substring(6);
            // ưu tiên onStartMatch, fallback setOpponentName nếu UI cũ
            if (!invokeIfPresent("onStartMatch", new Class[]{String.class}, new Object[]{opp})) {
                invokeIfPresent("setOpponentName", new Class[]{String.class}, new Object[]{opp});
            }
            return;
        }

        if (msg.startsWith("RESULT|")) {
            String body = msg.substring(7).trim();
            ParsedResult pr = parseResultLine(body);
            if (pr != null) {
                // ưu tiên onRoundResult(my, opp, res), fallback showResult(my, opp, res)
                if (!invokeIfPresent("onRoundResult", new Class[]{String.class,String.class,String.class},
                        new Object[]{pr.myMove, pr.oppMove, pr.resultText})) {
                    invokeIfPresent("showResult", new Class[]{String.class,String.class,String.class},
                            new Object[]{pr.myMove, pr.oppMove, pr.resultText});
                }
                // tự request history/leaderboard nếu UI có hỗ trợ
                requestHistory();
                requestLeaderboard();
            } else {
                showInfo(body);
            }
            return;
        }

        if (msg.startsWith("BYE|")) {
            String m = msg.substring(4);
            // ưu tiên onLeftRoom, fallback resetGameUI + dialog
            if (!invokeIfPresent("onLeftRoom", new Class[]{String.class}, new Object[]{m})) {
                showInfo(m);
                invokeIfPresent("resetGameUI", new Class[]{}, new Object[]{});
            }
            return;
        }

        if (msg.startsWith("OK|PASS_CHANGED")) { showInfo("Đổi mật khẩu thành công."); return; }
        if (msg.startsWith("LOGOUT_OK"))       { invokeIfPresent("onLogoutOk", new Class[]{}, new Object[]{}); return; }

        if (msg.startsWith("HISTORY|")) {
            String payload = msg.substring(8);
            String[] rows = payload.isEmpty() ? new String[0] : payload.split(",");
            // gọi theo tên hàm nếu có
            if (!invokeIfPresent("onHistory", new Class[]{String[].class}, new Object[]{rows})) {
                // UI cũ
                invokeIfPresent("updateHistoryTable", new Class[]{String[].class}, new Object[]{rows});
            }
            return;
        }

        if (msg.startsWith("LEADERBOARD|")) {
            String payload = msg.substring(12);
            List<String[]> table = new ArrayList<>();
            if (!payload.isEmpty()) {
                String[] items = payload.split(",");
                for (String it : items) {
                    String[] kv = it.split(":");
                    if (kv.length == 2) table.add(new String[]{kv[0], kv[1]});
                }
            }
            String[][] data = table.toArray(new String[0][0]);
            if (!invokeIfPresent("onLeaderboard", new Class[]{String[][].class}, new Object[]{data})) {
                invokeIfPresent("updateLeaderboard", new Class[]{String[][].class}, new Object[]{data});
            }
            return;
        }
    }

    /* ===================== Parsing utils ===================== */

    private static class ParsedResult { String myMove, oppMove, resultText; }

    private ParsedResult parseResultLine(String s) {
        try {
            Matcher m = RESULT_PAT.matcher(s);
            if (!m.find()) return null;
            String my  = normalizeMove(m.group(1));
            String opp = normalizeMove(m.group(2));
            String res = m.group(3).trim();
            ParsedResult pr = new ParsedResult();
            pr.myMove = my; pr.oppMove = opp; pr.resultText = res;
            return pr;
        } catch (Exception e) {
            return null;
        }
    }

    private String normalizeMove(String m) {
        m = m.toUpperCase();
        if (m.contains("ROCK") || m.contains("ĐÁ"))  return "ROCK";
        if (m.contains("PAPER")|| m.contains("BAO")) return "PAPER";
        if (m.contains("SCISS")|| m.contains("KÉO")) return "SCISSORS";
        return m;
    }

    /* ===================== UI helpers ===================== */

    private void showInfo(String m) {
        if (!invokeIfPresent("showInfo", new Class[]{String.class}, new Object[]{m})) {
            JOptionPane.showMessageDialog(null, m, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showWarn(String m) {
        if (!invokeIfPresent("showWarn", new Class[]{String.class}, new Object[]{m})) {
            JOptionPane.showMessageDialog(null, m, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showError(String m) {
        if (!invokeIfPresent("showError", new Class[]{String.class}, new Object[]{m})) {
            JOptionPane.showMessageDialog(null, m, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gọi method nếu tồn tại trong UI (theo tên + chữ ký).
     * Trả về true nếu gọi được, false nếu không tìm thấy/ lỗi.
     */
    private boolean invokeIfPresent(String method, Class<?>[] types, Object[] args) {
        try {
            var m = ui.getClass().getMethod(method, types);
            m.setAccessible(true);
            m.invoke(ui, args);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

	public Object sendLeave() {
		// TODO Auto-generated method stub
		return null;
	}
}
