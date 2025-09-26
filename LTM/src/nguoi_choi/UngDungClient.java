package nguoi_choi;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Oẳn Tù Tì Online — Premium Compact UI (2025, Pink Edition)
 * - Typography dịu mắt
 * - Card bo góc + đổ bóng, nút pill/ghost có hover
 * - Input: KHÔNG icon/emoji (tránh ô vuông) + nút Hiện/Ẩn text
 * - Trang chủ: search + empty state
 * - Lịch sử: màu/icon kết quả
 * - BXH: KHÔNG emoji huy chương; chỉ hiện số hạng (đậm) + thanh tiến độ
 * - GamePanel: Emoji KÉO/BÚA/BAO, ⬅ Về trang chủ, ESC; Thoát phòng điều hướng ngay
 * - Theme: Hồng pastel, “blink blink ✨” khi Thắng!
 * - Heading: ✨ tiêu đề ✨ + subtitle nowrap 1 dòng
 * ✅ Nước đi hiển thị & thứ tự: KÉO – BÚA – BAO (server vẫn SCISSORS/ROCK/PAPER)
 */
public class UngDungClient extends JFrame {

    /* ================== THEME ================== */
    interface Palette {
        // Nền hồng pastel êm mắt
        Color BG_GRADIENT_TOP    = new Color(0xFFF1F5); // #FFF1F5
        Color BG_GRADIENT_BOTTOM = new Color(0xFFFFFF); // #FFFFFF

        // Chữ
        Color TEXT_PRIMARY       = new Color(0x31111D); // nâu tím đậm
        Color TEXT_MUTED         = new Color(0x6B5B6E); // tím khói

        // Card + viền + bóng
        Color CARD_BG            = Color.WHITE;
        Color CARD_BORDER        = new Color(0xF5DCE6); // viền phấn hồng
        Color SHADOW             = new Color(0,0,0,22);

        // Tông hồng làm màu nhấn
        Color BLUE               = new Color(0xEC4899); // pink (primary)
        Color GREEN              = new Color(0xFB7185); // rose (success)
        Color ORANGE             = new Color(0xF9A8D4); // blush
        Color RED                = new Color(0xF43F5E); // rose-600 (danger)
        Color VIOLET             = new Color(0xD946EF); // tím hồng
        Color CYAN               = new Color(0xF472B6); // hồng sáng

        // Xám ấm hợp nền hồng
        Color GRAY_50            = new Color(0xFFF7FB);
        Color GRAY_100           = new Color(0xFDECF4);
        Color GRAY_200           = new Color(0xF7DDEB);
        Color GRAY_300           = new Color(0xEFC9DD);
    }

    /* ================== APP CORE ================== */

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new GradientPanel();

    // 6 màn hình
    final DangKyPanel        pnlRegister = new DangKyPanel();
    final DangNhapPanel      pnlLogin    = new DangNhapPanel();
    final TrangChuPanel      pnlHome     = new TrangChuPanel();
    final GamePanel          pnlGame     = new GamePanel();
    final LichSuPanel        pnlHistory  = new LichSuPanel();
    final BangXHPanel        pnlBoard    = new BangXHPanel();

    // Kết nối
    private final KetNoiKhach net = new KetNoiKhach(this);
    // Alias theo yêu cầu
    private final KetNoiKhach connection = net;

    private String currentUser = null;
    private String currentOpponent = null;

    public UngDungClient() {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception ignored) {}
        UIManager.put("control", Palette.GRAY_50);
        UIManager.put("text",    Palette.TEXT_PRIMARY);
        UIManager.put("nimbusBlueGrey", Palette.GRAY_200);

        setTitle("Oẳn Tù Tì Online — Premium UI (Pink)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 520); // khung gọn
        setLocationRelativeTo(null);

        root.setLayout(cards);
        root.add(pnlRegister, "register");
        root.add(pnlLogin,    "login");
        root.add(pnlHome,     "home");
        root.add(pnlGame,     "game");
        root.add(pnlHistory,  "history");
        root.add(pnlBoard,    "board");
        setContentPane(root);

        showLogin();
    }

    /* ====== Điều hướng ====== */
    public void showRegister() { cards.show(root, "register"); }
    public void showLogin()    { cards.show(root, "login");    }
    public void showHome()     { cards.show(root, "home");     }
    public void showGame()     { cards.show(root, "game");     }
    public void showHistory()  { cards.show(root, "history");  }
    public void showBoard()    { cards.show(root, "board");    }

    /* ====== Callbacks từ KetNoiKhach ====== */
    public void onLoginSuccess(String username) {
        currentUser = username;
        pnlHome.setHello(username);
        showHome();
        net.who();
    }

    public void onStartMatch(String opponent) {
        currentOpponent = opponent;
        pnlGame.setNames(currentUser, opponent);
        pnlGame.resetRoundUI();
        showGame();
    }

    public void onRoundResult(String myMove, String oppMove, String resultText) {
        pnlGame.showRound(myMove, oppMove, resultText);
        net.requestHistory();
        net.requestLeaderboard();
    }

    public void onLeftRoom(String msg) {
        showInfo(msg);
        currentOpponent = null;
        pnlGame.resetRoundUI();
        showHome();
        net.who();
    }

    public void updateUserList(String[] users) { pnlHome.updateUsers(users); }
    public void onHistory(String[] rows)       { pnlHistory.setData(rows); }
    public void onLeaderboard(String[][] data) { pnlBoard.setData(data); }

    public void onLogoutOk() {
        currentUser = null;
        currentOpponent = null;
        pnlLogin.clear();
        pnlHome.clearUsers();
        pnlGame.resetRoundUI();
        showLogin();
        showInfo("Đã đăng xuất.");
    }

    /* ====== Helpers ====== */
    public String getCurrentUser() { return currentUser; }
    public void   showInfo (String m){ JOptionPane.showMessageDialog(this, m, "Thông báo", JOptionPane.INFORMATION_MESSAGE); }
    public void   showWarn (String m){ JOptionPane.showMessageDialog(this, m, "Cảnh báo",   JOptionPane.WARNING_MESSAGE); }
    public void   showError(String m){ JOptionPane.showMessageDialog(this, m, "Lỗi",        JOptionPane.ERROR_MESSAGE); }

    /* ==========================================================
     *                   UI UTILITIES
     * ========================================================== */

    static class GradientPanel extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, Palette.BG_GRADIENT_TOP, 0, h, Palette.BG_GRADIENT_BOTTOM);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }

    static class ShadowCard extends JPanel {
        private final int radius;
        ShadowCard(LayoutManager lm, int radius) {
            super(lm);
            this.radius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(12, 14, 14, 14));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(Palette.SHADOW);
            g2.fillRoundRect(4, 6, w-10, h-10, radius+8, radius+8);
            g2.setColor(Palette.CARD_BG);
            g2.fillRoundRect(0, 0, w-10, h-10, radius, radius);
            g2.setColor(Palette.CARD_BORDER);
            g2.drawRoundRect(0, 0, w-10, h-10, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override public Insets getInsets() {
            Insets i = super.getInsets();
            return new Insets(i.top+4, i.left+6, i.bottom+8, i.right+10);
        }
    }

    /** Heading có ✨ hai bên, subtitle nhỏ & nowrap để không xuống dòng */
    private JLabel heading(String title, String subtitle) {
        String html = "<html><div style='line-height:1.5'>"
                + "<div style='font-weight:700;font-size:20px;color:#31111D;white-space:nowrap;'>"
                + "✨ " + title + " ✨"
                + "</div>"
                + (subtitle==null ? "" :
                   "<div style='font-size:12px;color:#6B5B6E;margin-top:4px;white-space:nowrap;'>"
                   + subtitle + "</div>")
                + "</div></html>";
        return new JLabel(html);
    }

    private static class PillButton extends JButton {
        private final Color base; private boolean hover, press;
        PillButton(String t, Color b){ super(t); base=b;
            setFocusPainted(false); setForeground(Color.WHITE);
            setOpaque(false); setContentAreaFilled(false);
            setBorder(new EmptyBorder(8,14,8,14));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            var self=this;
            addMouseListener(new MouseAdapter(){
                @Override public void mouseEntered(MouseEvent e){hover=true; self.repaint();}
                @Override public void mouseExited (MouseEvent e){hover=false; press=false; self.repaint();}
                @Override public void mousePressed (MouseEvent e){press=true; self.repaint();}
                @Override public void mouseReleased(MouseEvent e){press=false; self.repaint();}
            });
        }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w=getWidth(),h=getHeight(); Color c=base;
            if(press) c=c.darker(); else if(hover) c=c.brighter();
            g2.setPaint(new GradientPaint(0,0,c, w,0, c.brighter()));
            g2.fillRoundRect(0,0,w,h,999,999); g2.dispose();
            super.paintComponent(g);
        }
    }
    private JButton pill(String t, Color c){ return new PillButton(t,c); }
    private JButton ghost(String t){
        JButton b=new JButton(t);
        b.setFocusPainted(false);
        b.setForeground(Palette.TEXT_PRIMARY);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setOpaque(true);
        b.setBackground(Palette.GRAY_100);
        b.setBorder(new CompoundBorder(new LineBorder(Palette.GRAY_200,1,true), new EmptyBorder(6,12,6,12)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter(){
            @Override public void mouseEntered(MouseEvent e){b.setBackground(Palette.GRAY_200);}
            @Override public void mouseExited (MouseEvent e){b.setBackground(Palette.GRAY_100);}
        });
        return b;
    }
    private JTextField textField(){ JTextField t=new JTextField(20); t.setFont(new Font("Segoe UI",Font.PLAIN,15)); t.setBorder(new CompoundBorder(new LineBorder(Palette.GRAY_300,1,true), new EmptyBorder(10,14,10,14))); return t; }
    private JPasswordField passField(){ JPasswordField t=new JPasswordField(20); t.setFont(new Font("Segoe UI",Font.PLAIN,15)); t.setBorder(new CompoundBorder(new LineBorder(Palette.GRAY_300,1,true), new EmptyBorder(10,14,10,14))); return t; }

    /** Card wrapper có thanh gradient mảnh, tinh tế */
    private JPanel cardWrapper(String title, String subtitle, JComponent inner) {
        JPanel wrap = new JPanel(new GridBagLayout()); wrap.setOpaque(false);
        ShadowCard card = new ShadowCard(new BorderLayout(0,10), 16);
        JLabel head = heading(title, subtitle); head.setBorder(new EmptyBorder(6,10,0,10));
        JComponent bar = new JComponent(){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;
                g2.setPaint(new GradientPaint(0,0,Palette.VIOLET,getWidth(),0,Palette.CYAN));
                g2.fillRoundRect(12,0,getWidth()-24,getHeight(),10,10);
            }
            @Override public Dimension getPreferredSize(){return new Dimension(10,6);}
        };
        JPanel headWrap=new JPanel(new BorderLayout()); headWrap.setOpaque(false);
        headWrap.add(head,BorderLayout.NORTH); headWrap.add(bar,BorderLayout.CENTER);
        card.add(headWrap, BorderLayout.NORTH);
        JPanel pad=new JPanel(new BorderLayout()); pad.setOpaque(false); pad.add(inner, BorderLayout.CENTER);
        card.add(pad, BorderLayout.CENTER);
        GridBagConstraints gc=new GridBagConstraints();
        gc.insets=new Insets(16,18,16,18); gc.fill=GridBagConstraints.BOTH; gc.weightx=gc.weighty=1;
        wrap.add(card,gc);
        return wrap;
    }

    private void styleTable(JTable table){
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        JTableHeader header=table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        TableCellRenderer base=table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class,(tbl,val,isSel,hasFocus,row,col)->{
            Component c=base.getTableCellRendererComponent(tbl,val,isSel,hasFocus,row,col);
            if(c instanceof JComponent jc){
                jc.setBackground(isSel?new Color(0xFCE7F3):(row%2==0?Color.WHITE:Palette.GRAY_50));
                jc.setBorder(new MatteBorder(0,0,1,0, Palette.GRAY_100));
            }
            return c;
        });
    }

    /* ================== INPUT COMPONENTS ================== */
    // == IconTextField: KHÔNG icon/emoji nếu leadingEmoji null/blank ==
    class IconTextField extends JPanel {
        final JTextField field = new JTextField();
        private final Color borderNor = Palette.GRAY_300;
        private final Color borderFoc = Palette.CYAN;
        IconTextField(String placeholder, String leadingEmoji) {
            super(new BorderLayout(8,0));
            setOpaque(false);
            setBorder(new EmptyBorder(2,2,2,2));

            field.setBorder(null);
            field.setOpaque(false);
            field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            field.putClientProperty("JTextField.placeholderText", placeholder);

            JPanel inner = new JPanel(new BorderLayout(8,0)) {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w=getWidth(), h=getHeight();
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0,0,w,h,14,14);
                    g2.setColor(field.isFocusOwner()? borderFoc : borderNor);
                    g2.drawRoundRect(0,0,w-1,h-1,14,14);
                    g2.dispose();
                }
            };
            inner.setOpaque(false);
            inner.setBorder(new EmptyBorder(10,12,10,12));

            // Chỉ add icon khi có emoji hợp lệ (ở đây mình sẽ KHÔNG truyền emoji)
            if (leadingEmoji != null && !leadingEmoji.isBlank()) {
                JLabel ico = new JLabel(leadingEmoji);
                ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
                ico.setForeground(Palette.TEXT_MUTED);
                inner.add(ico, BorderLayout.WEST);
            }

            inner.add(field, BorderLayout.CENTER);
            add(inner, BorderLayout.CENTER);

            field.addFocusListener(new java.awt.event.FocusAdapter(){
                @Override public void focusGained(java.awt.event.FocusEvent e){ repaint(); }
                @Override public void focusLost  (java.awt.event.FocusEvent e){ repaint(); }
            });
            setPreferredSize(new Dimension(340, 46));
        }
        String getText(){ return field.getText(); }
        void setText(String s){ field.setText(s); }
    }

    // == IconPasswordField: KHÔNG icon/emoji; nút mắt dùng text "Hiện/Ẩn" ==
    class IconPasswordField extends JPanel {
        final JPasswordField field = new JPasswordField();
        private boolean visible=false;
        private final char echoDefault; // default echo
        private final Color borderNor = Palette.GRAY_300;
        private final Color borderFoc = Palette.CYAN;

        IconPasswordField(String placeholder, String leadingEmoji) {
            super(new BorderLayout(8,0));
            setOpaque(false);
            setBorder(new EmptyBorder(2,2,2,2));

            echoDefault = field.getEchoChar()==0 ? '•' : field.getEchoChar();

            field.setBorder(null);
            field.setOpaque(false);
            field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            field.putClientProperty("JTextField.placeholderText", placeholder);

            // Nút Hiện/Ẩn dạng text (không emoji)
            JButton eye = new JButton("Hiện");
            eye.setBorder(new EmptyBorder(0,6,0,6));
            eye.setContentAreaFilled(false);
            eye.setFocusPainted(false);
            eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            eye.addActionListener(e -> {
                visible = !visible;
                field.setEchoChar(visible ? (char)0 : echoDefault);
                eye.setText(visible ? "Ẩn" : "Hiện");
            });

            JPanel inner = new JPanel(new BorderLayout(8,0)) {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w=getWidth(), h=getHeight();
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0,0,w,h,14,14);
                    g2.setColor(field.isFocusOwner()? borderFoc : borderNor);
                    g2.drawRoundRect(0,0,w-1,h-1,14,14);
                    g2.dispose();
                }
            };
            inner.setOpaque(false);
            inner.setBorder(new EmptyBorder(10,12,10,12));

            // KHÔNG thêm icon phía trái (dù có truyền leadingEmoji cũng bỏ qua)
            // (Nếu muốn bật lại thì add JLabel như IconTextField ở trên)
            inner.add(field, BorderLayout.CENTER);
            inner.add(eye, BorderLayout.EAST);
            add(inner, BorderLayout.CENTER);

            field.addFocusListener(new java.awt.event.FocusAdapter(){
                @Override public void focusGained(java.awt.event.FocusEvent e){ repaint(); }
                @Override public void focusLost  (java.awt.event.FocusEvent e){ repaint(); }
            });
            setPreferredSize(new Dimension(340, 46));
        }

        String getText(){ return new String(field.getPassword()); }
        void clear(){ field.setText(""); }
    }

    static class TagChip extends JLabel {
        TagChip(String text, Color bg) {
            super(text);
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(bg);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(new EmptyBorder(4,8,4,8));
        }
    }

    static class EmptyState extends JPanel {
        EmptyState(String big, String small) {
            setOpaque(false);
            setLayout(new GridBagLayout());
            JLabel a = new JLabel("🕊", SwingConstants.CENTER); // giữ nguyên; nếu không hiển thị được báo mình bỏ luôn
            a.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
            JLabel b = new JLabel("<html><b style='font-size:15px;color:#31111D'>"+big+
                    "</b><br><span style='color:#6B5B6E'>"+small+"</span></html>");
            JPanel box = new JPanel(new BorderLayout(8,8));
            box.setOpaque(false);
            box.add(a, BorderLayout.WEST);
            box.add(b, BorderLayout.CENTER);
            add(box);
            setBorder(new EmptyBorder(32,0,32,0));
        }
    }

    /* =============== Blink blink ✨ label =============== */
    static class BlinkLabel extends JLabel {
        private final javax.swing.Timer timer;
        private float phase = 0f; // 0..1

        BlinkLabel(String text, int style, int size) {
            super(text, SwingConstants.CENTER);
            setFont(new Font("Segoe UI", style, size));
            setOpaque(false);
            timer = new javax.swing.Timer(40, e -> {
                phase += 0.04f;
                if (phase > 1f) phase -= 1f;
                repaint();
            });
        }

        public void startBlink() { if (!timer.isRunning()) timer.start(); }
        public void stopBlink()  { if (timer.isRunning())  timer.stop();  repaint(); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            float alpha = 0.5f + 0.5f*(float)Math.sin(phase * Math.PI * 2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
            super.paintComponent(g2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f * alpha));
            g2.setColor(Palette.CYAN);
            int w = getWidth(), h = getHeight();
            int r = Math.min(w, h) / 6;
            int cx = w/2, cy = h/2;
            g2.fillOval(cx - 3*r, cy - r*2, r, r);
            g2.fillOval(cx + 2*r, cy - r, r, r);
            g2.fillOval(cx - r,  cy + r, r, r);
            g2.dispose();
        }
    }

    /* ================== PANELS ================== */

    class DangKyPanel extends JPanel {
        JTextField txtUser = textField();
        JPasswordField txtPass = passField();
        JButton btnSubmit = pill("Đăng ký", Palette.BLUE);
        JButton btnToLogin = ghost("Đăng nhập");

        DangKyPanel(){
            setOpaque(false);
            JPanel form=new JPanel(new GridBagLayout()); form.setOpaque(false);
            var gc=new GridBagConstraints(); gc.insets=new Insets(8,10,8,10); gc.anchor=GridBagConstraints.WEST;
            JLabel l1=new JLabel("Tên đăng ký:"); JLabel l2=new JLabel("Mật khẩu:");
            l1.setForeground(Palette.TEXT_MUTED); l1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            l2.setForeground(Palette.TEXT_MUTED); l2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            gc.gridx=0; gc.gridy=0; form.add(l1, gc);
            gc.gridx=1; form.add(txtUser, gc);
            gc.gridx=0; gc.gridy=1; form.add(l2, gc);
            gc.gridx=1; form.add(txtPass, gc);

            JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); actions.setOpaque(false);
            actions.add(btnSubmit); actions.add(btnToLogin);
            gc.gridx=0; gc.gridy=2; gc.gridwidth=2; form.add(actions, gc);

            setLayout(new BorderLayout());
            add(cardWrapper("Tạo tài khoản", "Đăng ký để bắt đầu thi đấu online ngay!", form), BorderLayout.CENTER);

            btnSubmit.addActionListener(e -> net.register(txtUser.getText().trim(), new String(txtPass.getPassword())));
            btnToLogin.addActionListener(e -> showLogin());
        }
    }

    class DangNhapPanel extends JPanel {
        // KHÔNG icon/emoji
        private final IconTextField     tfUser = new IconTextField("Tên đăng nhập", null);
        private final IconPasswordField tfPass = new IconPasswordField("Mật khẩu", null);

        private final JButton btnLogin       = pill("Đăng nhập", Palette.GREEN);
        private final JButton btnToRegister  = ghost("Đăng ký");
        private final JLabel  lblHint        = new JLabel(" ");

        DangNhapPanel() {
            setOpaque(false);

            JPanel form = new JPanel(new GridBagLayout());
            form.setOpaque(false);
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(8, 10, 8, 10);
            gc.anchor = GridBagConstraints.WEST;

            JLabel l1 = new JLabel("Tên đăng nhập:"); l1.setForeground(Palette.TEXT_MUTED); l1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JLabel l2 = new JLabel("Mật khẩu:");      l2.setForeground(Palette.TEXT_MUTED); l2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            gc.gridx=0; gc.gridy=0; form.add(l1, gc);
            gc.gridx=1; form.add(tfUser, gc);
            gc.gridx=0; gc.gridy=1; form.add(l2, gc);
            gc.gridx=1; form.add(tfPass, gc);

            lblHint.setForeground(new Color(0x6B5B6E));
            lblHint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            gc.gridx=1; gc.gridy=2; gc.anchor=GridBagConstraints.WEST;
            form.add(lblHint, gc);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            actions.setOpaque(false);
            actions.add(btnLogin); actions.add(btnToRegister);
            gc.gridx=1; gc.gridy=3; gc.anchor=GridBagConstraints.WEST;
            form.add(actions, gc);

            setLayout(new BorderLayout());
            add(cardWrapper("Đăng nhập", "Chào mừng trở lại — chúc bạn may mắn!", form), BorderLayout.CENTER);

            Runnable doLogin = () -> {
                String u = tfUser.getText().trim();
                String p = tfPass.getText();
                if (u.isBlank() || p.isBlank()) {
                    lblHint.setText(" Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
                    lblHint.setForeground(new Color(0xDC2626));
                    return;
                }
                lblHint.setText(" Đang kiểm tra…");
                lblHint.setForeground(new Color(0x6B5B6E));
                net.login(u, p);
            };

            btnLogin.addActionListener(e -> doLogin.run());
            btnToRegister.addActionListener(e -> showRegister());
            tfUser.field.addActionListener(e -> doLogin.run());
            tfPass.field.addActionListener(e -> doLogin.run());
        }

        void clear() {
            tfUser.setText("");
            tfPass.clear();
            lblHint.setText(" ");
            lblHint.setForeground(new Color(0x6B5B6E));
        }
    }

    static class UserCellRenderer extends JPanel implements ListCellRenderer<String> {
        private final JLabel circle=new JLabel("",SwingConstants.CENTER);
        private final JLabel name=new JLabel();
        UserCellRenderer(){
            setLayout(new BorderLayout(10,0)); setOpaque(true);
            circle.setPreferredSize(new Dimension(28,28));
            circle.setFont(new Font("Segoe UI", Font.BOLD, 13));
            name.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            add(circle,BorderLayout.WEST); add(name,BorderLayout.CENTER);
            setBorder(new EmptyBorder(4,8,4,8));
        }
        @Override public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus){
            String initials="";
            if(value!=null && !value.isBlank()){ char c=Character.toUpperCase(value.trim().charAt(0)); initials=String.valueOf(c); }
            name.setText(value);
            int h=Math.abs(value==null?7:value.hashCode());
            Color fill=new Color(255, 170+(h%60), 200-(h%50)); // phối hồng ngẫu nhiên dịu
            circle.setText(initials);
            circle.setForeground(Color.WHITE);
            circle.setOpaque(true);
            circle.setBackground(fill);
            circle.setBorder(new LineBorder(fill.darker(),1,true));
            setBackground(isSelected?new Color(0xFCE7F3):Color.WHITE);
            return this;
        }
    }

    class TrangChuPanel extends JPanel {
        private final JLabel lblHello = new JLabel("Xin chào", SwingConstants.LEFT);
        private final DefaultListModel<String> userModel = new DefaultListModel<>();
        private final JList<String> listUsers = new JList<>(userModel);
        private java.util.List<String> allUsers = new java.util.ArrayList<>();
        private final JTextField tfSearch = new JTextField(20);
        private final TagChip chipOnline = new TagChip("Online: 0", Palette.CYAN);
        private final JPanel centerPanel = new JPanel(new CardLayout());
        private final EmptyState empty = new EmptyState("Chưa có ai online","Bấm “Ghép ngẫu nhiên” hoặc chờ một chút…");

        private final JButton btnInvite   = pill("Mời đấu",        Palette.VIOLET);
        private final JButton btnRandom   = pill("Ghép ngẫu nhiên", Palette.CYAN);
        private final JButton btnHistory  = ghost("Lịch sử");
        private final JButton btnBoard    = ghost("Bảng xếp hạng");
        private final JButton btnEdit     = ghost("Đổi mật khẩu");
        private final JButton btnLogout   = pill("Đăng xuất",       Palette.RED);

        TrangChuPanel() {
            setOpaque(false);

            lblHello.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblHello.setBorder(new EmptyBorder(8,0,6,0));
            tfSearch.putClientProperty("JTextField.placeholderText", "Tìm người chơi...");
            tfSearch.setBorder(new CompoundBorder(new LineBorder(Palette.GRAY_300,1,true), new EmptyBorder(10,14,10,14)));
            tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JPanel header = new JPanel(new BorderLayout(8,8)); header.setOpaque(false);
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); left.setOpaque(false);
            left.add(lblHello); left.add(chipOnline);
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); right.setOpaque(false);
            right.add(tfSearch);
            header.add(left, BorderLayout.WEST); header.add(right, BorderLayout.EAST);

            listUsers.setFixedCellHeight(34);
            listUsers.setBorder(new EmptyBorder(2,2,2,2));
            listUsers.setCellRenderer(new UserCellRenderer());
            JScrollPane sp = new JScrollPane(listUsers);
            sp.setBorder(new CompoundBorder(new LineBorder(Palette.GRAY_200,1,true), new EmptyBorder(4,4,4,4)));
            sp.setOpaque(false); sp.getViewport().setOpaque(false);

            centerPanel.setOpaque(false);
            centerPanel.add(sp, "list");
            centerPanel.add(empty, "empty");
            showCenterCard();

            JPanel leftBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); leftBtns.setOpaque(false);
            leftBtns.add(btnInvite); leftBtns.add(btnRandom); leftBtns.add(btnHistory); leftBtns.add(btnBoard);
            JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); rightBtns.setOpaque(false);
            rightBtns.add(btnEdit); rightBtns.add(btnLogout);
            JPanel south = new JPanel(new BorderLayout()); south.setOpaque(false);
            south.add(leftBtns, BorderLayout.WEST); south.add(rightBtns, BorderLayout.EAST);

            JPanel inner = new JPanel(new BorderLayout(10,10)); inner.setOpaque(false);
            inner.add(header, BorderLayout.NORTH);
            inner.add(centerPanel, BorderLayout.CENTER);
            inner.add(south, BorderLayout.SOUTH);

            setLayout(new BorderLayout());
            add(cardWrapper("Trang chủ", "Mời người chơi hoặc ghép ngẫu nhiên để bắt đầu!", inner), BorderLayout.CENTER);

            btnInvite.addActionListener(e -> {
                String target = listUsers.getSelectedValue();
                if (target == null) { showWarn("Chọn 1 người trong danh sách."); return; }
                net.invite(target);
            });
            btnRandom.addActionListener(e -> net.randomMatch());
            btnHistory.addActionListener(e -> { showHistory(); net.requestHistory(); });
            btnBoard.addActionListener(e -> { showBoard(); net.requestLeaderboard(); });
            btnEdit.addActionListener(e -> openChangePassDialog());
            btnLogout.addActionListener(e -> net.logout());

            tfSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                private void apply(){ filterUsers(tfSearch.getText().trim()); showCenterCard(); }
                public void insertUpdate(javax.swing.event.DocumentEvent e){ apply(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e){ apply(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e){ apply(); }
            });
        }

        void setHello(String name){ lblHello.setText("Xin chào, " + name); }
        void updateUsers(String[] users){
            allUsers = new java.util.ArrayList<>();
            if (users != null) for (String u: users) if (u!=null && !u.isBlank()) allUsers.add(u);
            chipOnline.setText("Online: " + allUsers.size());
            filterUsers(tfSearch.getText().trim());
            showCenterCard();
        }
        void clearUsers(){ allUsers.clear(); userModel.clear(); chipOnline.setText("Online: 0"); showCenterCard(); }

        private void filterUsers(String keyword){
            userModel.clear();
            for (String u : allUsers) {
                if (keyword.isBlank() || u.toLowerCase().contains(keyword.toLowerCase())) userModel.addElement(u);
            }
        }
        private void showCenterCard(){
            CardLayout cl = (CardLayout) centerPanel.getLayout();
            if (userModel.isEmpty()) cl.show(centerPanel, "empty"); else cl.show(centerPanel, "list");
        }

        private void openChangePassDialog() {
            JDialog dlg = new JDialog(UngDungClient.this, "Đổi mật khẩu", true);
            dlg.setSize(420, 300);
            dlg.setLocationRelativeTo(UngDungClient.this);
            dlg.setLayout(new BorderLayout(10,10));
            dlg.getContentPane().setBackground(Color.WHITE);

            // KHÔNG icon/emoji trong dialog
            IconPasswordField txtOld = new IconPasswordField("Mật khẩu cũ", null);
            IconPasswordField txtNew = new IconPasswordField("Mật khẩu mới", null);
            IconPasswordField txtRe  = new IconPasswordField("Nhập lại", null);

            JPanel form = new JPanel(new GridBagLayout());
            form.setOpaque(false);
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(10,10,10,10);
            gc.gridx=0; gc.gridy=0; form.add(new JLabel("Mật khẩu cũ:"), gc);
            gc.gridx=1; form.add(txtOld, gc);
            gc.gridx=0; gc.gridy=1; form.add(new JLabel("Mật khẩu mới:"), gc);
            gc.gridx=1; form.add(txtNew, gc);
            gc.gridx=0; gc.gridy=2; form.add(new JLabel("Nhập lại:"), gc);
            gc.gridx=1; form.add(txtRe, gc);

            JLabel hint = new JLabel(" ");
            hint.setForeground(new Color(0xDC2626));
            hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            gc.gridx=1; gc.gridy=3; gc.anchor=GridBagConstraints.WEST; form.add(hint, gc);

            JButton ok = pill("Đổi mật khẩu", Palette.CYAN);
            JButton cancel = ghost("Hủy");
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false);
            actions.add(ok); actions.add(cancel);

            dlg.add(form, BorderLayout.CENTER);
            dlg.add(actions, BorderLayout.SOUTH);

            ok.addActionListener(e -> {
                String oldPw = txtOld.getText();
                String newPw = txtNew.getText();
                String rePw  = txtRe.getText();
                if (newPw.isBlank()) { hint.setText(" Mật khẩu mới không được trống"); return; }
                if (!newPw.equals(rePw)) { hint.setText(" Mật khẩu nhập lại không khớp"); return; }
                dlg.dispose();
                net.changePassword(oldPw, newPw);
            });
            cancel.addActionListener(e -> dlg.dispose());

            dlg.setVisible(true);
        }
    }

    private JPanel cardWhite(JComponent c){
        ShadowCard sc=new ShadowCard(new BorderLayout(), 16); sc.setOpaque(false);
        JPanel inner=new JPanel(new BorderLayout()); inner.setOpaque(false); inner.add(c,BorderLayout.CENTER);
        sc.add(inner,BorderLayout.CENTER); return sc;
    }

    /** ================== GAME PANEL (Emoji + Home/ESC) ================== */
    class GamePanel extends JPanel {
        private final JLabel lblMe  = new JLabel(" Bạn: ?", SwingConstants.LEFT);
        private final JLabel lblOpp = new JLabel("? :Đối thủ ", SwingConstants.RIGHT);

        private final JLabel lblMyMove  = new JLabel("❓", SwingConstants.CENTER);
        private final BlinkLabel lblResult  = new BlinkLabel("Chưa chơi", Font.BOLD, 26);
        private final JLabel lblOppMove = new JLabel("❓", SwingConstants.CENTER);

        // ✅ Đổi hiển thị & thứ tự: KÉO – BÚA – BAO
        private final JButton btnScissors = pill("KÉO", Palette.ORANGE); // gửi SCISSORS
        private final JButton btnRock     = pill("BÚA", Palette.BLUE);   // gửi ROCK
        private final JButton btnPaper    = pill("BAO", Palette.GREEN);  // gửi PAPER

        private final JButton btnLeave    = pill("Thoát phòng", Palette.RED);
        private final JButton btnHome     = ghost(" Về Trang chủ");

        GamePanel() {
            setOpaque(false);
            JPanel inner = new JPanel(new BorderLayout(12,12));
            inner.setOpaque(false);

            JPanel top = new JPanel(new GridLayout(1,2)); top.setOpaque(false);
            Font f = new Font("Segoe UI", Font.BOLD, 16);
            lblMe.setFont(f); lblOpp.setFont(f);
            top.add(lblMe); top.add(lblOpp);
            inner.add(top, BorderLayout.NORTH);

            lblMyMove.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 92));
            lblOppMove.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 92));

            JPanel mid = new JPanel(new GridLayout(1,3,12,0));
            mid.setOpaque(false);
            mid.add(cardWhite(lblMyMove));
            mid.add(cardWhite(lblResult));
            mid.add(cardWhite(lblOppMove));
            inner.add(mid, BorderLayout.CENTER);

            // South: trái = Home, giữa = KÉO/BÚA/BAO, phải = Thoát
            JPanel south = new JPanel(new BorderLayout()); south.setOpaque(false);
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); left.setOpaque(false); left.add(btnHome);
            JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0)); center.setOpaque(false);
            center.add(btnScissors); // KÉO
            center.add(btnRock);     // BÚA
            center.add(btnPaper);    // BAO
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); right.setOpaque(false); right.add(btnLeave);
            south.add(left, BorderLayout.WEST);
            south.add(center, BorderLayout.CENTER);
            south.add(right, BorderLayout.EAST);
            inner.add(south, BorderLayout.SOUTH);

            setLayout(new BorderLayout());
            add(cardWrapper("Chơi – Oẳn Tù Tì", "Chọn nước đi của bạn và chờ đối thủ!", inner), BorderLayout.CENTER);

            // ==== LISTENERS ====
            btnScissors.addActionListener(e -> sendMoveUI("SCISSORS", "✌")); // KÉO
            btnRock.addActionListener(e     -> sendMoveUI("ROCK",     "✊")); // BÚA
            btnPaper.addActionListener(e    -> sendMoveUI("PAPER",    "✋")); // BAO

            // Thoát phòng
            btnLeave.addActionListener(e -> {
                try { connection.sendLeave(); } catch (Exception ignored) {}
                resetRoundUI();
                showHome();
                net.who();
            });

            btnHome.addActionListener(e -> {
                try { connection.sendLeave(); } catch (Exception ignored) {}
                resetRoundUI();
                showHome();
                net.who();
            });

            // ESC để về Trang chủ
            getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                    .put(KeyStroke.getKeyStroke("ESCAPE"), "goHome");
            getActionMap().put("goHome", new AbstractAction() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    try { connection.sendLeave(); } catch (Exception ignored) {}
                    resetRoundUI();
                    showHome();
                    net.who();
                }
            });
        }

        void setNames(String me, String opp) {
            lblMe.setText(" Bạn: " + (me==null?"?":me));
            lblOpp.setText((opp==null?"?":opp) + " :Đối thủ ");
        }

        void resetRoundUI() {
            lblMyMove.setText("❓");
            lblOppMove.setText("❓");
            lblResult.setText("Chưa chơi");
            lblResult.setForeground(Palette.TEXT_PRIMARY);
            lblResult.stopBlink();
        }

        void sendMoveUI(String move, String emoji) {
            lblMyMove.setText(emoji);
            lblResult.setText(" Chờ đối thủ...");
            lblResult.setForeground(Palette.TEXT_PRIMARY);
            lblResult.stopBlink();
            connection.sendMove(move);
        }

        void showRound(String myMove, String oppMove, String res) {
            lblMyMove.setText(toEmoji(myMove));
            lblOppMove.setText(toEmoji(oppMove));
            if (res != null && res.contains("Thắng")) {
                lblResult.setText("  Thắng!");
                lblResult.setForeground(Palette.GREEN);
                lblResult.startBlink(); // ✨
            } else if (res != null && res.contains("Hòa")) {
                lblResult.setText("  Hòa!");
                lblResult.setForeground(Palette.ORANGE);
                lblResult.stopBlink();
            } else {
                lblResult.setText("  Thua!");
                lblResult.setForeground(Palette.RED);
                lblResult.stopBlink();
            }
        }

        private String toEmoji(String m) {
            if (m == null) return "❓";
            String u = m.toUpperCase();
            if (u.contains("SCISS") || u.contains("KÉO"))  return "✌"; // KÉO
            if (u.contains("ROCK")  || u.contains("BÚA"))  return "✊"; // BÚA
            if (u.contains("PAPER") || u.contains("BAO"))  return "✋"; // BAO
            return "❓";
        }
    }

    class LichSuPanel extends JPanel {
        private final DefaultTableModel model = new DefaultTableModel(
                new Object[][]{}, new String[]{"Đối thủ", "Kết quả"}) {
            public boolean isCellEditable(int r,int c){ return false; }
        };

        private final JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!(c instanceof JLabel l)) return c;

                if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? Color.WHITE : Palette.GRAY_50);
                else c.setBackground(new Color(0xFCE7F3));
                l.setBorder(new EmptyBorder(6,10,6,6));
                l.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                if (col == 1) {
                    String val = String.valueOf(getValueAt(row, col));
                    if (val.contains("Thắng")) {
                        l.setForeground(Palette.GREEN); l.setText(" " + val);
                    } else if (val.contains("Thua")) {
                        l.setForeground(Palette.RED);   l.setText(" " + val);
                    } else if (val.contains("Hòa")) {
                        l.setForeground(Palette.ORANGE); l.setText(" " + val);
                    } else l.setForeground(Palette.TEXT_PRIMARY);
                } else l.setForeground(Palette.TEXT_PRIMARY);
                return c;
            }
        };

        private final JButton btnBack = ghost(" Về Trang chủ");
        private final JButton btnReload = pill(" Tải lịch sử", Palette.CYAN);

        LichSuPanel() {
            setOpaque(false);
            styleTable(table);

            JScrollPane sp = new JScrollPane(table);
            sp.setBorder(new CompoundBorder(new LineBorder(Palette.GRAY_200,1,true), new EmptyBorder(2,2,2,2)));

            JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            south.setOpaque(false);
            south.add(btnReload); south.add(btnBack);

            JPanel inner = new JPanel(new BorderLayout(12,12));
            inner.setOpaque(false);
            inner.add(sp, BorderLayout.CENTER);
            inner.add(south, BorderLayout.SOUTH);

            setLayout(new BorderLayout());
            add(cardWrapper("Lịch sử đấu", "Các trận gần đây của bạn", inner), BorderLayout.CENTER);

            btnReload.addActionListener(e -> net.requestHistory());
            btnBack.addActionListener(e -> showHome());
        }

        void setData(String[] rows){
            model.setRowCount(0);
            if (rows == null) return;
            for (String s : rows) {
                String[] parts = s.split("→");
                if (parts.length >= 2) model.addRow(new Object[]{parts[0].trim(), parts[1].trim()});
            }
        }
    }

    class BangXHPanel extends JPanel {
        private final DefaultTableModel model = new DefaultTableModel(
                new Object[][]{}, new String[]{"Hạng", "Người chơi", "Thắng"}) {
            public boolean isCellEditable(int r,int c){ return false; }
            public Class<?> getColumnClass(int c){ return c==2 ? Integer.class : Object.class; }
        };

        class WinBarRenderer extends DefaultTableCellRenderer {
            int max = 1;
            @Override public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                l.setText(val==null?"0":String.valueOf(val));
                l.setHorizontalAlignment(SwingConstants.RIGHT);
                l.setBorder(new EmptyBorder(0,8,0,8));
                l.setForeground(Palette.TEXT_PRIMARY);
                return l;
            }
            @Override protected void paintComponent(Graphics g) {
                int wins = 0;
                try { wins = Integer.parseInt(getText()); } catch (Exception ignored){}
                float ratio = Math.min(1f, max<=0?0f : wins/(float)max);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(0xFFEAF3));
                g2.fillRoundRect(8, h/2-8, w-16, 16, 10,10);
                g2.setPaint(new GradientPaint(0,0, Palette.VIOLET, w,0, Palette.CYAN));
                g2.fillRoundRect(8, h/2-8, (int)((w-16)*ratio), 16, 10,10);
                g2.dispose();
                super.paintComponent(g);
            }
        }

        private final JTable table = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!(c instanceof JLabel l)) return c;

                if (!isRowSelected(row)) c.setBackground(row%2==0?Color.WHITE:Palette.GRAY_50);
                else c.setBackground(new Color(0xFCE7F3));

                String me = getCurrentUser();
                String name = String.valueOf(getValueAt(row, 1));
                if (me != null && me.equals(name)) c.setBackground(new Color(0xFFE4F1));

                l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                l.setBorder(new EmptyBorder(6,8,6,8));
                l.setForeground(Palette.TEXT_PRIMARY);

                // ❌ Bỏ emoji huy chương — chỉ hiển thị số hạng (đậm, canh giữa)
                if (col==0) {
                    int rank = Integer.parseInt(String.valueOf(getValueAt(row, 0)));
                    l.setText(String.valueOf(rank));
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    l.setFont(l.getFont().deriveFont(Font.BOLD));
                }
                return c;
            }
        };

        private final WinBarRenderer winRenderer = new WinBarRenderer();
        private final JButton btnBack   = ghost(" Về Trang chủ");
        private final JButton btnReload = pill("  Làm mới", Palette.CYAN);

        BangXHPanel(){
            setOpaque(false);
            styleTable(table);

            table.getColumnModel().getColumn(2).setCellRenderer(winRenderer);
            table.getColumnModel().getColumn(0).setMaxWidth(90);

            JScrollPane sp = new JScrollPane(table);
            sp.setBorder(new CompoundBorder(new LineBorder(Palette.GRAY_200,1,true), new EmptyBorder(2,2,2,2)));

            JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            south.setOpaque(false);
            south.add(btnReload); south.add(btnBack);

            JPanel inner = new JPanel(new BorderLayout(12,12));
            inner.setOpaque(false);
            inner.add(sp, BorderLayout.CENTER);
            inner.add(south, BorderLayout.SOUTH);

            setLayout(new BorderLayout());
            add(cardWrapper("Bảng xếp hạng", "Top cao thủ thắng nhiều nhất", inner), BorderLayout.CENTER);

            btnReload.addActionListener(e -> net.requestLeaderboard());
            btnBack.addActionListener(e -> showHome());
        }

        void setData(String[][] data){
            model.setRowCount(0);
            if (data == null || data.length==0) { winRenderer.max = 1; return; }

            java.util.List<String[]> list = new java.util.ArrayList<>();
            int max = 1;
            for (String[] row: data) {
                if (row==null || row.length<2) continue;
                try {
                    int wins = Integer.parseInt(row[1].trim());
                    max = Math.max(max, wins);
                    list.add(new String[]{row[0].trim(), String.valueOf(wins)});
                } catch (Exception ignored) {}
            }
            list.sort((a,b)-> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

            int rank=1;
            for (String[] r : list) model.addRow(new Object[]{rank++, r[0], Integer.parseInt(r[1])});
            winRenderer.max = max;
            table.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UngDungClient().setVisible(true));
    }
}
