package nguoi_choi;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class KetNoiKhach {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GiaoDienTroChoi ui;
    private String currentUser;

    public KetNoiKhach(GiaoDienTroChoi ui) {
        this.ui = ui;
        try {
            socket = new Socket("localhost", 7777);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(this::listen).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(ui, "Không kết nối được tới server", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==== gửi lệnh ====
    public void login(String username, String password) {
        this.currentUser = username;
        out.println("LOGIN|" + username + "|" + TienIch.hash(password));
    }

    public void register(String username, String password) {
        out.println("REGISTER|" + username + "|" + TienIch.hash(password));
    }

    public void sendMove(String move) {
        out.println("MOVE|" + move);
    }

    public void sendLeave() {
        out.println("LEAVE");
    }

    public void requestHistory() {
        out.println("HISTORY");
    }

    public void requestLeaderboard() {
        out.println("LEADERBOARD");
    }

    // ==== lắng nghe server ====
    private void listen() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("INFO|")) {
                    String msg = line.substring(5);
                    ui.appendLog(msg);

                    if (msg.startsWith("Đã ghép cặp với")) {
                        String opp = msg.replace("Đã ghép cặp với", "").trim();
                        ui.setOpponentName(opp);
                    }

                } else if (line.startsWith("RESULT|")) {
                    // RESULT|Bạn: ROCK, Đối thủ: PAPER → Thua
                    String content = line.substring(7).replace("Bạn:", "").trim();
                    String[] parts = content.split(", Đối thủ:");
                    if (parts.length == 2) {
                        String myMove = parts[0].trim();
                        String[] oppAndRes = parts[1].split("→");
                        if (oppAndRes.length == 2) {
                            String oppMove = oppAndRes[0].trim();
                            String result = oppAndRes[1].trim();
                            ui.showResult(myMove, oppMove, result);
                        }
                    }

                } else if (line.startsWith("HISTORY|")) {
                    ui.updateHistoryTable(line.substring(8).split(","));

                } else if (line.startsWith("LEADERBOARD|")) {
                    String[] rows = line.substring(12).split(",");
                    String[][] data = new String[rows.length][2];
                    for (int i = 0; i < rows.length; i++) {
                        String[] kv = rows[i].split(":");
                        if (kv.length == 2) data[i] = kv;
                    }
                    ui.updateLeaderboard(data);

                } else if (line.startsWith("BYE|")) {
                    JOptionPane.showMessageDialog(ui, line.substring(4));
                    ui.resetGameUI();

                } else if (line.startsWith("ERROR|")) {
                    JOptionPane.showMessageDialog(ui, line.substring(6), "Lỗi", JOptionPane.ERROR_MESSAGE);

                } else if (line.startsWith("OK|")) {
                    JOptionPane.showMessageDialog(ui, line.substring(3));
                    ui.setPlayerName(currentUser); // bật nút chơi sau khi login
                }
            }
        } catch (IOException e) {
            ui.appendLog("⛔ Mất kết nối đến server");
            ui.resetGameUI();
        }
    }
}
