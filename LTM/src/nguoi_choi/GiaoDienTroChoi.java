package nguoi_choi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GiaoDienTroChoi extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTable tblHistory, tblLeaderboard;
    private JLabel lblMe, lblOpponent;
    private JLabel lblMyChoice, lblOpponentChoice, lblResult;
    private JButton btnRock, btnPaper, btnScissors, btnLeave;
    private KetNoiKhach connection;

    public GiaoDienTroChoi() {
        setTitle("Oẳn Tù Tì Online - Client");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));

        connection = new KetNoiKhach(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));
        tabs.add("Đăng nhập", createLoginPanel());
        tabs.add("Chơi", createGamePanel());
        tabs.add("Lịch sử", createHistoryPanel());
        tabs.add("Bảng xếp hạng", createLeaderboardPanel());

        add(tabs);
    }

    // ==== TAB ĐĂNG NHẬP ====
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(new Color(240, 248, 255));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("🔑 Đăng nhập");
        JButton btnRegister = new JButton("📝 Đăng ký");

        btnLogin.setBackground(new Color(72, 209, 204));
        btnRegister.setBackground(new Color(100, 149, 237));
        btnLogin.setForeground(Color.WHITE);
        btnRegister.setForeground(Color.WHITE);

        btnLogin.addActionListener(e -> connection.login(txtUsername.getText(), new String(txtPassword.getPassword())));
        btnRegister.addActionListener(e -> connection.register(txtUsername.getText(), new String(txtPassword.getPassword())));

        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        panel.add(new JLabel("Tên đăng nhập:")); panel.add(txtUsername);
        panel.add(new JLabel("Mật khẩu:")); panel.add(txtPassword);
        panel.add(btnLogin); panel.add(btnRegister);

        return panel;
    }

    // ==== TAB CHƠI GAME ====
    private JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Header: thông tin người chơi
        JPanel topInfo = new JPanel(new GridLayout(1, 2));
        lblMe = new JLabel("👤 Bạn: (chưa đăng nhập)", SwingConstants.LEFT);
        lblOpponent = new JLabel("⚔ Đối thủ: (chưa ghép)", SwingConstants.RIGHT);
        lblMe.setFont(new Font("Arial", Font.BOLD, 16));
        lblOpponent.setFont(new Font("Arial", Font.BOLD, 16));
        topInfo.add(lblMe);
        topInfo.add(lblOpponent);
        panel.add(topInfo, BorderLayout.NORTH);

        // Vùng giữa: lựa chọn + kết quả
        JPanel middle = new JPanel(new GridLayout(1, 3, 30, 0));
        lblMyChoice = new JLabel("❓", SwingConstants.CENTER);
        lblOpponentChoice = new JLabel("❓", SwingConstants.CENTER);
        lblResult = new JLabel("Chưa chơi", SwingConstants.CENTER);

        lblMyChoice.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        lblOpponentChoice.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        lblResult.setFont(new Font("Arial", Font.BOLD, 28));

        middle.add(lblMyChoice);
        middle.add(lblResult);
        middle.add(lblOpponentChoice);
        panel.add(middle, BorderLayout.CENTER);

        // Footer: các nút chọn
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

        btnRock = createActionButton("✊ Đá", new Color(70, 130, 180));
        btnPaper = createActionButton("✋ Bao", new Color(46, 139, 87));
        btnScissors = createActionButton("✌ Kéo", new Color(255, 140, 0));
        btnLeave = createActionButton("🚪 Thoát", new Color(220, 20, 60));

        btnRock.addActionListener(e -> sendMoveUI("ROCK", "✊"));
        btnPaper.addActionListener(e -> sendMoveUI("PAPER", "✋"));
        btnScissors.addActionListener(e -> sendMoveUI("SCISSORS", "✌"));
        btnLeave.addActionListener(e -> connection.sendLeave());

        buttons.add(btnRock);
        buttons.add(btnPaper);
        buttons.add(btnScissors);
        buttons.add(btnLeave);

        panel.add(buttons, BorderLayout.SOUTH);

        // mặc định khóa nút khi chưa login
        enableGameButtons(false);

        return panel;
    }

    // ==== TAB LỊCH SỬ ====
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tblHistory = new JTable(new DefaultTableModel(new String[]{"Đối thủ", "Kết quả"}, 0));
        panel.add(new JScrollPane(tblHistory), BorderLayout.CENTER);
        JButton btnLoad = new JButton("📂 Tải lịch sử");
        btnLoad.addActionListener(e -> connection.requestHistory());
        panel.add(btnLoad, BorderLayout.SOUTH);
        return panel;
    }

    // ==== TAB BẢNG XẾP HẠNG ====
    private JPanel createLeaderboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tblLeaderboard = new JTable(new DefaultTableModel(new String[]{"Người chơi", "Số trận thắng"}, 0));
        panel.add(new JScrollPane(tblLeaderboard), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("🔄 Làm mới");
        btnRefresh.addActionListener(e -> connection.requestLeaderboard());
        panel.add(btnRefresh, BorderLayout.SOUTH);
        return panel;
    }

    // ==== Helper ====
    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 45));
        return btn;
    }

    // ========= Update UI ==========
    private void sendMoveUI(String move, String emoji) {
        lblMyChoice.setText(emoji);
        lblResult.setText("⏳ Chờ đối thủ...");
        lblResult.setForeground(Color.BLACK);
        connection.sendMove(move);
    }

    public void showResult(String myMove, String oppMove, String result) {
        lblMyChoice.setText(toEmoji(myMove));
        lblOpponentChoice.setText(toEmoji(oppMove));

        if (result.contains("Thắng")) {
            lblResult.setText("✅ Thắng!");
            lblResult.setForeground(new Color(0, 150, 0));
        } else if (result.contains("Hòa")) {
            lblResult.setText("⚖ Hòa!");
            lblResult.setForeground(new Color(255, 140, 0));
        } else {
            lblResult.setText("❌ Thua!");
            lblResult.setForeground(Color.RED);
        }
    }

    private String toEmoji(String move) {
        switch (move) {
            case "ROCK": return "✊";
            case "PAPER": return "✋";
            case "SCISSORS": return "✌";
            default: return "❓";
        }
    }

    public void updateHistoryTable(String[] data) {
        DefaultTableModel model = (DefaultTableModel) tblHistory.getModel();
        model.setRowCount(0);
        for (String entry : data) {
            String[] parts = entry.split("→");
            if (parts.length == 2) model.addRow(new String[]{parts[0].trim(), parts[1].trim()});
        }
    }

    public void updateLeaderboard(String[][] data) {
        DefaultTableModel model = (DefaultTableModel) tblLeaderboard.getModel();
        model.setRowCount(0);
        for (String[] row : data) {
            model.addRow(row);
        }
    }

    public void setPlayerName(String name) {
        lblMe.setText("👤 Bạn: " + name);
        enableGameButtons(true); // bật nút sau khi login
    }

    public void setOpponentName(String name) {
        lblOpponent.setText("⚔ Đối thủ: " + name);
    }

    public void resetGameUI() {
        lblOpponent.setText("⚔ Đối thủ: (chưa ghép)");
        lblMyChoice.setText("❓");
        lblOpponentChoice.setText("❓");
        lblResult.setText("Chưa chơi");
        lblResult.setForeground(Color.BLACK);
        enableGameButtons(false);
    }

    private void enableGameButtons(boolean enable) {
        btnRock.setEnabled(enable);
        btnPaper.setEnabled(enable);
        btnScissors.setEnabled(enable);
        btnLeave.setEnabled(enable);
    }

	public void appendLog(String string) {
		// TODO Auto-generated method stub
		
	}
}
