package nguoi_choi;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class GiaoDienTroChoi extends JFrame {
    // ====== UI fields ======
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JTable tblHistory, tblLeaderboard, tblUsers;
    private JLabel lblMe, lblOpponent;
    private JLabel lblMyChoice, lblOpponentChoice, lblResult;

    private JButton btnRock, btnPaper, btnScissors, btnLeave;

    private KetNoiKhach connection;

    private JTabbedPane tabs;
    private JPanel gamePanel, historyPanel, leaderboardPanel, usersPanel;

    // ====== Constructor ======
    public GiaoDienTroChoi() {
        setTitle("Oẳn Tù Tì Online - Client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Cửa sổ nhỏ gọn
        setSize(560, 380);
        setMinimumSize(new Dimension(520, 340));
        setLocationRelativeTo(null);

        // Font & tone nhẹ
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));

        connection = new KetNoiKhach(this);

        gamePanel        = createGamePanel();
        historyPanel     = createHistoryPanel();
        leaderboardPanel = createLeaderboardPanel();
        usersPanel       = createUsersPanel();

        // Icon 16px cho tab (đổi tên file nếu khác)
        Icon icLogin = loadIcon("login.png.png", 16);
        Icon icPlay  = loadIcon("game.png.png", 16);
        Icon icUsers = loadIcon("history.png.png", 16);       // nếu có users.png thì đổi ở đây
        Icon icHist  = loadIcon("history.png.png", 16);
        Icon icRank  = loadIcon("leaderboard.png.png", 16);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // ban đầu chỉ tab Đăng nhập
        tabs.addTab("Đăng nhập", icLogin, createLoginPanel());

        // lưu icon để dùng khi mở thêm tab sau khi login
        tabs.putClientProperty("icPlay",  icPlay);
        tabs.putClientProperty("icUsers", icUsers);
        tabs.putClientProperty("icHist",  icHist);
        tabs.putClientProperty("icRank",  icRank);

        add(tabs);
    }

    // ====== Helper icon ======
    private Icon loadIcon(String path, int size) {
        java.net.URL url = getClass().getResource("/assets/" + path);
        if (url == null) return null;
        Image img = new ImageIcon(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // ====== Login tab ======
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 249, 255));

        txtUsername = new JTextField(16);
        txtPassword = new JPasswordField(16);

        JButton btnLogin    = makePrimaryButton("🔑  Đăng nhập", new Color(0x22C55E));
        JButton btnRegister = makePrimaryButton("📝  Đăng ký",   new Color(0x3B82F6));

        btnLogin.addActionListener(e -> connection.login(
                txtUsername.getText().trim(),
                new String(txtPassword.getPassword()))
        );
        btnRegister.addActionListener(e -> connection.register(
                txtUsername.getText().trim(),
                new String(txtPassword.getPassword()))
        );

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.gridx = 0; gc.gridy = 0; gc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Tên đăng nhập:"), gc);
        gc.gridx = 1; gc.anchor = GridBagConstraints.LINE_START;
        panel.add(txtUsername, gc);

        gc.gridx = 0; gc.gridy = 1; gc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Mật khẩu:"), gc);
        gc.gridx = 1; gc.anchor = GridBagConstraints.LINE_START;
        panel.add(txtPassword, gc);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.add(btnLogin);
        actions.add(btnRegister);
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2; gc.anchor = GridBagConstraints.CENTER;
        panel.add(actions, gc);

        return panel;
    }

    // ====== Game tab ======
    private JPanel createGamePanel() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        JPanel header = new JPanel(new GridLayout(1,2));
        lblMe = new JLabel("👤 Bạn: (chưa đăng nhập)", SwingConstants.LEFT);
        lblOpponent = new JLabel("⚔ Đối thủ: (chưa ghép)", SwingConstants.RIGHT);
        lblMe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblOpponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.add(lblMe); header.add(lblOpponent);
        root.add(header, BorderLayout.NORTH);

        JPanel middle = new JPanel(new GridLayout(1,3,14,0));
        lblMyChoice = new JLabel("❓", SwingConstants.CENTER);
        lblOpponentChoice = new JLabel("❓", SwingConstants.CENTER);
        lblResult = new JLabel("Chưa chơi", SwingConstants.CENTER);

        lblMyChoice.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lblOpponentChoice.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lblResult.setFont(new Font("Segoe UI", Font.BOLD, 22));

        middle.add(lblMyChoice);
        middle.add(lblResult);
        middle.add(lblOpponentChoice);
        root.add(middle, BorderLayout.CENTER);

        // Nút có ICON PNG
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        btnRock     = makeIconButton("Đá",   "rock.png",     new Color(0x3B82F6));
        btnPaper    = makeIconButton("Bao",  "paper.png",    new Color(0x22C55E));
        btnScissors = makeIconButton("Kéo",  "scissors.png", new Color(0xF59E0B));
        btnLeave    = makeIconButton("Thoát","exit.png",     new Color(0xEF4444));

        btnRock.addActionListener(e -> sendMoveUI("ROCK", "✊"));
        btnPaper.addActionListener(e -> sendMoveUI("PAPER", "✋"));
        btnScissors.addActionListener(e -> sendMoveUI("SCISSORS", "✌"));
        btnLeave.addActionListener(e -> connection.sendLeave());

        buttons.add(btnRock); buttons.add(btnPaper);
        buttons.add(btnScissors); buttons.add(btnLeave);
        root.add(buttons, BorderLayout.SOUTH);

        enableGameButtons(false);
        return root;
    }

    // ====== Users tab ======
    private JPanel createUsersPanel() {
        JPanel p = new JPanel(new BorderLayout());
        tblUsers = new JTable(new DefaultTableModel(new String[]{"Người chơi"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        styleTableBase(tblUsers);
        p.add(new JScrollPane(tblUsers), BorderLayout.CENTER);

        JButton btnInvite  = makePrimaryButton("🤝  Mời đấu",   new Color(0x8B5CF6));
        JButton btnRandom  = makePrimaryButton("🎲  Ngẫu nhiên", new Color(0x10B981));
        btnInvite.addActionListener(e -> {
            int row = tblUsers.getSelectedRow();
            if (row >= 0) {
                String target = tblUsers.getValueAt(row,0).toString();
                connection.invite(target);
            } else {
                JOptionPane.showMessageDialog(this, "Chọn một người chơi để mời đấu nhé!");
            }
        });
        btnRandom.addActionListener(e -> connection.randomMatch());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnInvite); south.add(btnRandom);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    // ====== History tab ======
    private JPanel createHistoryPanel() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"Đối thủ","Kết quả"},0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHistory = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r,row,col);
                if (!isRowSelected(row))
                    c.setBackground(row%2==0 ? new Color(248,250,252) : Color.WHITE);
                return c;
            }
        };
        styleHistoryTable(tblHistory);
        p.add(new JScrollPane(tblHistory), BorderLayout.CENTER);

        JButton btnLoad = makePrimaryButton("📂  Tải lịch sử", new Color(0x0284C7));
        btnLoad.addActionListener(e -> connection.requestHistory());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnLoad);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    // ====== Leaderboard tab ======
    private JPanel createLeaderboardPanel() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"Người chơi","Số trận thắng"},0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int c) { return c==1 ? Integer.class : String.class; }
        };
        tblLeaderboard = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r,row,col);
                if (!isRowSelected(row))
                    c.setBackground(row%2==0 ? new Color(248,250,252) : Color.WHITE);
                return c;
            }
        };
        styleLeaderboardTable(tblLeaderboard);
        p.add(new JScrollPane(tblLeaderboard), BorderLayout.CENTER);

        JButton btn = makePrimaryButton("🔄  Làm mới", new Color(0x0EA5E9));
        btn.addActionListener(e -> connection.requestLeaderboard());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btn);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    // ====== Styling helpers ======
    private JButton makePrimaryButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // nút gọn cho khung nhỏ
        return b;
    }

    // Nút có ICON PNG
    private JButton makeIconButton(String text, String iconFile, Color bg) {
        JButton b = makePrimaryButton("  " + text, bg); // thêm khoảng trắng cho đẹp
        Icon icon = loadIcon(iconFile, 18);
        if (icon != null) b.setIcon(icon);
        b.setHorizontalTextPosition(SwingConstants.RIGHT);
        b.setIconTextGap(6);
        return b;
    }

    private void styleTableBase(JTable t) {
        t.setRowHeight(26);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0,0));
        t.setFillsViewportHeight(true);
        t.setSelectionBackground(new Color(224,242,254));
        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(new Color(240,242,245));
    }

    private void styleHistoryTable(JTable t) {
        styleTableBase(t);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);
        t.getColumnModel().getColumn(0).setCellRenderer(left);
        t.getColumnModel().getColumn(1).setCellRenderer(new ResultCellRenderer());
    }

    private void styleLeaderboardTable(JTable t) {
        styleTableBase(t);
        // tên: trái, số: giữa + tô màu theo top view
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);
        t.getColumnModel().getColumn(0).setCellRenderer(left);

        t.getColumnModel().getColumn(1).setCellRenderer(new RankCellRenderer());
        t.setAutoCreateRowSorter(true);
        // sort giảm dần theo cột 1
        DefaultRowSorter<?,?> rs = (DefaultRowSorter<?,?>) t.getRowSorter();
        rs.toggleSortOrder(1); rs.toggleSortOrder(1);
    }

    // tô màu Thắng/Hòa/Thua
    static class ResultCellRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                String s = value == null ? "" : value.toString();
                if (s.contains("Thắng")) c.setForeground(new Color(0x16A34A));
                else if (s.contains("Hòa")) c.setForeground(new Color(0xF59E0B));
                else if (s.contains("Thua")) c.setForeground(new Color(0xDC2626));
                else c.setForeground(new Color(60,60,60));
            }
            return c;
        }
    }

    // tô màu Top 1–3 (theo thứ tự hiển thị)
    static class RankCellRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);
            if (!isSelected) {
                int rank = row + 1; // theo view
                if (rank == 1)      c.setForeground(new Color(0xD97706)); // vàng
                else if (rank == 2) c.setForeground(new Color(0x6B7280)); // bạc
                else if (rank == 3) c.setForeground(new Color(0xB45309)); // đồng
                else                c.setForeground(new Color(60,60,60));
            }
            return c;
        }
    }

    // ====== Game interactions ======
    private void sendMoveUI(String move, String emoji) {
        lblMyChoice.setText(emoji);
        lblResult.setText("⏳  Chờ đối thủ...");
        lblResult.setForeground(new Color(70,70,70));
        connection.sendMove(move);
    }

    private String toEmoji(String move) {
        switch (move) {
            case "ROCK": return "✊";
            case "PAPER": return "✋";
            case "SCISSORS": return "✌";
            default: return "❓";
        }
    }

    private void enableGameButtons(boolean on) {
        btnRock.setEnabled(on);
        btnPaper.setEnabled(on);
        btnScissors.setEnabled(on);
        btnLeave.setEnabled(on);
    }

    // ====== API từ KetNoiKhach ======
    public void showResult(String myMove, String oppMove, String result) {
        lblMyChoice.setText(toEmoji(myMove));
        lblOpponentChoice.setText(toEmoji(oppMove));
        if (result.contains("Thắng")) {
            lblResult.setText("✅  Thắng!");
            lblResult.setForeground(new Color(0x16A34A));
        } else if (result.contains("Hòa")) {
            lblResult.setText("⚖  Hòa!");
            lblResult.setForeground(new Color(0xF59E0B));
        } else {
            lblResult.setText("❌  Thua!");
            lblResult.setForeground(new Color(0xDC2626));
        }
    }

    public void updateHistoryTable(String[] rows) {
        DefaultTableModel m = (DefaultTableModel) tblHistory.getModel();
        m.setRowCount(0);
        if (rows == null) return;
        for (String r : rows) {
            String[] parts = r.split("→");
            if (parts.length == 2) m.addRow(new String[]{parts[0].trim(), parts[1].trim()});
        }
    }

    public void updateLeaderboard(String[][] data) {
        DefaultTableModel m = (DefaultTableModel) tblLeaderboard.getModel();
        m.setRowCount(0);
        if (data == null) return;
        for (String[] row : data) m.addRow(new Object[]{row[0], Integer.parseInt(row[1])});
    }

    public void updateUserList(String[] users) {
        DefaultTableModel m = (DefaultTableModel) tblUsers.getModel();
        m.setRowCount(0);
        if (users == null) return;
        for (String u : users) m.addRow(new Object[]{u});
    }

    public void setPlayerName(String name) {
        lblMe.setText("👤 Bạn: " + name);
        enableGameButtons(true);
        showMainTabs();
    }

    public void setOpponentName(String name) {
        lblOpponent.setText("⚔ Đối thủ: " + name);
        lblMyChoice.setText("❓");
        lblOpponentChoice.setText("❓");
        lblResult.setText("Chưa chơi");
        lblResult.setForeground(new Color(70,70,70));
        enableGameButtons(true);
    }

    public void resetGameUI() {
        lblOpponent.setText("⚔ Đối thủ: (chưa ghép)");
        lblMyChoice.setText("❓");
        lblOpponentChoice.setText("❓");
        lblResult.setText("Chưa chơi");
        lblResult.setForeground(new Color(70,70,70));
        enableGameButtons(false);
    }

    // ====== Tab dynamic ======
    private void showMainTabs() {
        Icon icPlay  = (Icon) tabs.getClientProperty("icPlay");
        Icon icUsers = (Icon) tabs.getClientProperty("icUsers");
        Icon icHist  = (Icon) tabs.getClientProperty("icHist");
        Icon icRank  = (Icon) tabs.getClientProperty("icRank");

        if (tabs.indexOfTab("Chơi") == -1)           tabs.addTab("Chơi", icPlay, gamePanel);
        if (tabs.indexOfTab("Người chơi") == -1)     tabs.addTab("Người chơi", icUsers, usersPanel);
        if (tabs.indexOfTab("Lịch sử") == -1)        tabs.addTab("Lịch sử", icHist, historyPanel);
        if (tabs.indexOfTab("Bảng xếp hạng") == -1)  tabs.addTab("Bảng xếp hạng", icRank, leaderboardPanel);
    }

    public void hideMainTabs() {
        for (String tab : new String[]{"Chơi","Người chơi","Lịch sử","Bảng xếp hạng"}) {
            int idx = tabs.indexOfTab(tab);
            if (idx != -1) tabs.removeTabAt(idx);
        }
        tabs.setSelectedIndex(0);
        resetGameUI();
    }
}
