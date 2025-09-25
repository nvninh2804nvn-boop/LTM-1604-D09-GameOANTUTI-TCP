package may_chu;

import java.sql.*;

/**
 * QuanLyTaiKhoan - dùng JDBC qua Database.getConnection()
 * Tự tạo bảng users nếu chưa có.
 */
public class QuanLyTaiKhoan {

    public QuanLyTaiKhoan() {
        ensureSchema();
    }

    /** Tạo bảng users nếu chưa tồn tại */
    private void ensureSchema() {
        final String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.println("[QuanLyTaiKhoan] Lỗi tạo bảng users: " + e.getMessage());
        }
    }

    /** Đăng ký: username duy nhất */
    public boolean register(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank())
            return false;

        final String insert = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setString(1, username.trim());
            ps.setString(2, password); // TODO: nên mã hóa (bcrypt) nếu dùng thật
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Lỗi thường gặp: UNIQUE constraint failed: users.username
            // => tài khoản đã tồn tại
            return false;
        }
    }

    /** Đăng nhập: so sánh username+password đơn giản (demo) */
    public boolean login(String username, String password) {
        if (username == null || password == null) return false;

        final String query = "SELECT 1 FROM users WHERE username = ? AND password = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username.trim());
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[QuanLyTaiKhoan] Lỗi đăng nhập: " + e.getMessage());
            return false;
        }
    }

    /** Lấy id người dùng (hữu ích cho lưu lịch sử/leaderboard) */
    public Integer getUserId(String username) {
        final String sql = "SELECT id FROM users WHERE username = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("[QuanLyTaiKhoan] Lỗi getUserId: " + e.getMessage());
        }
        return null;
    }
}
