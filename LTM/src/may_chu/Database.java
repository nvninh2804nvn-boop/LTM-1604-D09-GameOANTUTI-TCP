package may_chu;

import java.sql.*;

public class Database {
    private static Connection conn;

    /** Lấy kết nối SQLite (db/game.db) */
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                // Bật WAL để concurrent tốt hơn 1 chút (optional)
                conn = DriverManager.getConnection("jdbc:sqlite:db/game.db");
                try (Statement st = conn.createStatement()) {
                    st.execute("PRAGMA journal_mode=WAL;");
                    st.execute("PRAGMA foreign_keys=ON;");
                } catch (SQLException ignore) {}
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("[DB] Error connecting: " + e.getMessage());
            return null;
        }
    }

    /** Tạo bảng nếu chưa có – KHỚP với QuanLyTaiKhoan.ensureSchema() */
    public static void createTables() {
        final String users = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                last_login DATETIME
            );
        """;

        // Lưu toàn bộ ván (match). winner_username NULL nếu hòa.
        final String matches = """
            CREATE TABLE IF NOT EXISTS matches (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player1_username TEXT NOT NULL,
                player2_username TEXT NOT NULL,
                winner_username TEXT,
                played_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;

        // Bảng xếp hạng tổng hợp
        final String leaderboard = """
            CREATE TABLE IF NOT EXISTS leaderboard (
                username TEXT PRIMARY KEY,
                wins INTEGER NOT NULL DEFAULT 0,
                losses INTEGER NOT NULL DEFAULT 0
            );
        """;

        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute(users);
            st.execute(matches);
            st.execute(leaderboard);
        } catch (SQLException e) {
            System.err.println("[DB] Error creating tables: " + e.getMessage());
        }
    }

    /** Đăng ký nhanh (không bắt buộc dùng – QuanLyTaiKhoan đã có register) */
    public static boolean addUser(String username, String password) {
        final String sql = "INSERT INTO users(username, password, last_login) VALUES(?, ?, CURRENT_TIMESTAMP)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Duplicate username -> false
            return false;
        }
    }

    /** Kiểm tra đăng nhập đơn giản (có thể để QuanLyTaiKhoan.login dùng riêng cũng được) */
    public static boolean checkUserExists(String username, String password) {
        final String sql = "SELECT 1 FROM users WHERE username=? AND password=? LIMIT 1";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // cập nhật last_login
                    try (PreparedStatement upd = c.prepareStatement("UPDATE users SET last_login=CURRENT_TIMESTAMP WHERE username=?")) {
                        upd.setString(1, username);
                        upd.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] checkUserExists error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Ghi lịch sử 1 trận vào bảng matches.
     * @param player1  người chơi 1 (username)
     * @param player2  người chơi 2 (username)
     * @param winner   username người thắng; truyền null nếu HÒA
     */
    public static void saveMatchHistory(String player1, String player2, String winner) {
        final String sql = "INSERT INTO matches(player1_username, player2_username, winner_username) VALUES(?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, player1);
            ps.setString(2, player2);
            if (winner == null || winner.isBlank()) ps.setNull(3, Types.VARCHAR);
            else ps.setString(3, winner);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] saveMatchHistory error: " + e.getMessage());
        }
    }

    /**
     * Cập nhật bảng xếp hạng cho 1 người.
     * Gọi 2 lần cho mỗi trận: (winner, true) và (loser, false).
     */
    public static void updateLeaderboard(String username, boolean isWin) {
        // SQLite hỗ trợ UPSERT (ON CONFLICT) từ 3.24.0
        final String upsertWin = """
            INSERT INTO leaderboard(username, wins, losses)
            VALUES(?, 1, 0)
            ON CONFLICT(username) DO UPDATE SET wins = wins + 1;
        """;
        final String upsertLoss = """
            INSERT INTO leaderboard(username, wins, losses)
            VALUES(?, 0, 1)
            ON CONFLICT(username) DO UPDATE SET losses = losses + 1;
        """;
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(isWin ? upsertWin : upsertLoss)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] updateLeaderboard error: " + e.getMessage());
        }
    }

    // ====== Một vài helper hữu ích (tùy bạn dùng/không) ======

    /** Lấy top N leaderboard */
    public static ResultSet getTopLeaderboard(int limit) throws SQLException {
        final String sql = "SELECT username, wins, losses FROM leaderboard ORDER BY wins DESC, losses ASC LIMIT ?";
        PreparedStatement ps = getConnection().prepareStatement(sql);
        ps.setInt(1, limit);
        return ps.executeQuery(); // nhớ đóng ở nơi gọi
    }

    /** Lấy lịch sử theo username (gần nhất trước) */
    public static ResultSet getHistory(String username, int limit) throws SQLException {
        final String sql = """
            SELECT player1_username, player2_username, winner_username, played_at
            FROM matches
            WHERE player1_username = ? OR player2_username = ?
            ORDER BY played_at DESC
            LIMIT ?;
        """;
        PreparedStatement ps = getConnection().prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, username);
        ps.setInt(3, limit);
        return ps.executeQuery(); // nhớ đóng ở nơi gọi
    }
}
