package nguoi_choi;

import javax.swing.*;

public class UngDungKhach {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GiaoDienTroChoi ui = new GiaoDienTroChoi();
            ui.setVisible(true);
        });
    }
}
