package may_chu;

import java.sql.*;

public class Database {
    private static Connection conn;

    /** Mở kết nối SQLite (db/game.db) */
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection("jdbc:sqlite:db/game.db");
                try (Statement st = conn.createStatement()) {
                    st.execute("PRAGMA journal_mode=WAL;");
                    st.execute("PRAGMA foreign_keys=ON;");
                }
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("[DB] connect error: " + e.getMessage());
            return null;
        }
    }

    /** Tạo bảng (users/matches/leaderboard) nếu chưa có */
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
        final String matches = """
            CREATE TABLE IF NOT EXISTS matches (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player1_username TEXT NOT NULL,
                player2_username TEXT NOT NULL,
                winner_username TEXT,                -- NULL = Hòa
                played_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;
        final String leaderboard = """
            CREATE TABLE IF NOT EXISTS leaderboard (
                username TEXT PRIMARY KEY,
                wins INTEGER NOT NULL DEFAULT 0,
                losses INTEGER NOT NULL DEFAULT 0
            );
        """;

        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute(users); st.execute(matches); st.execute(leaderboard);
        } catch (SQLException e) {
            System.err.println("[DB] create tables: " + e.getMessage());
        }
    }

    /* === Users (tùy chọn dùng) === */
    public static boolean addUser(String username, String password) {
        final String sql = "INSERT INTO users(username,password,last_login) VALUES(?,?,CURRENT_TIMESTAMP)";
        try (Connection c=getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1, username); ps.setString(2, password); ps.executeUpdate(); return true;
        } catch (SQLException e) { return false; }
    }

    public static boolean checkUserExists(String username, String password) {
        final String sql = "SELECT 1 FROM users WHERE username=? AND password=? LIMIT 1";
        try (Connection c=getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1, username); ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                boolean ok = rs.next();
                if (ok) try (PreparedStatement upd=c.prepareStatement("UPDATE users SET last_login=CURRENT_TIMESTAMP WHERE username=?")) {
                    upd.setString(1, username); upd.executeUpdate();
                }
                return ok;
            }
        } catch (SQLException e) { return false; }
    }

    /* === Matches + Leaderboard === */
    public static void saveMatchHistory(String p1, String p2, String winnerOrNull) {
        final String sql = "INSERT INTO matches(player1_username,player2_username,winner_username) VALUES(?,?,?)";
        try (Connection c=getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1, p1); ps.setString(2, p2);
            if (winnerOrNull==null || winnerOrNull.isBlank()) ps.setNull(3, Types.VARCHAR);
            else ps.setString(3, winnerOrNull);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] saveMatchHistory: " + e.getMessage());
        }
    }

    public static void updateLeaderboard(String username, boolean isWin) {
        final String upsertWin  = """
            INSERT INTO leaderboard(username,wins,losses) VALUES(?,1,0)
            ON CONFLICT(username) DO UPDATE SET wins=wins+1;
        """;
        final String upsertLoss = """
            INSERT INTO leaderboard(username,wins,losses) VALUES(?,0,1)
            ON CONFLICT(username) DO UPDATE SET losses=losses+1;
        """;
        try (Connection c=getConnection();
             PreparedStatement ps=c.prepareStatement(isWin? upsertWin: upsertLoss)) {
            ps.setString(1, username); ps.executeUpdate();
        } catch (SQLException e) { System.err.println("[DB] updateLeaderboard: " + e.getMessage()); }
    }

    public static ResultSet getTopLeaderboard(int limit) throws SQLException {
        final String sql = "SELECT username,wins,losses FROM leaderboard ORDER BY wins DESC, losses ASC LIMIT ?";
        PreparedStatement ps = getConnection().prepareStatement(sql);
        ps.setInt(1, limit);
        return ps.executeQuery(); // nhớ đóng tại nơi gọi
    }

    public static ResultSet getHistory(String username, int limit) throws SQLException {
        final String sql = """
            SELECT player1_username,player2_username,winner_username,played_at
            FROM matches
            WHERE player1_username=? OR player2_username=?
            ORDER BY played_at DESC
            LIMIT ?;
        """;
        PreparedStatement ps = getConnection().prepareStatement(sql);
        ps.setString(1, username); ps.setString(2, username); ps.setInt(3, limit);
        return ps.executeQuery(); // nhớ đóng tại nơi gọi
    }
}
