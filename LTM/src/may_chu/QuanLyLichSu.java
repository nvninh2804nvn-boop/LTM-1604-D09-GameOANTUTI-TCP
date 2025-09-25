package may_chu;

import java.sql.*;
import java.util.*;

public class QuanLyLichSu {

    /** Đọc lịch sử của 1 người chơi */
    public static List<String> readHistory(String username) {
        List<String> history = new ArrayList<>();
        try (ResultSet rs = Database.getHistory(username, 20)) { // lấy 20 trận gần nhất
            while (rs.next()) {
                String p1 = rs.getString("player1_username");
                String p2 = rs.getString("player2_username");
                String winner = rs.getString("winner_username");
                String playedAt = rs.getString("played_at");

                String opponent = p1.equals(username) ? p2 : p1;
                String result;
                if (winner == null) result = "Hòa";
                else if (winner.equals(username)) result = "Thắng";
                else result = "Thua";

                history.add(opponent + " → " + result + " (" + playedAt + ")");
            }
        } catch (SQLException e) {
            System.err.println("[QuanLyLichSu] Lỗi đọc lịch sử: " + e.getMessage());
        }
        return history;
    }

    /** Lấy bảng xếp hạng (top N) */
    public static Map<String, Integer> getLeaderboard(int topN) {
        Map<String, Integer> board = new LinkedHashMap<>();
        try (ResultSet rs = Database.getTopLeaderboard(topN)) {
            while (rs.next()) {
                String user = rs.getString("username");
                int wins = rs.getInt("wins");
                board.put(user, wins);
            }
        } catch (SQLException e) {
            System.err.println("[QuanLyLichSu] Lỗi đọc leaderboard: " + e.getMessage());
        }
        return board;
    }

    // Các hàm loadAccounts/saveAccounts cũ bỏ hẳn, vì QuanLyTaiKhoan đã dùng DB.
}
