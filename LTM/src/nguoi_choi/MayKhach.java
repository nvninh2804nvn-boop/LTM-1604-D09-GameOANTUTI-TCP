package nguoi_choi;

import javax.swing.*;

public class MayKhach {
    public static void main(String[] args) {
        // Bảo đảm Swing chạy trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // có thể đổi LookAndFeel cho đẹp
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignore) {}

            // Khởi tạo ứng dụng client
            UngDungClient ui = new UngDungClient();
            ui.setVisible(true);
        });
    }
}
