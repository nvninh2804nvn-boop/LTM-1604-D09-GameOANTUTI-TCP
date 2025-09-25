package may_chu;

import java.io.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/** Lưu kép: SQLite (chính) + CSV mirror (db/history.csv). */
public class QuanLyLichSu {
    private static final String CSV = "db/history.csv";
    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            File f = new File(CSV);
            File dir = f.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            if (!f.exists()) f.createNewFile();
        } catch (IOException ignore) {}
        Database.createTables(); // bảo đảm bảng có sẵn
    }

    /** Lưu cả vào CSV và SQLite. result = Thắng/Hòa/Thua của 'player' đối đầu 'opponent'. */
    public static synchronized void saveHistory(String player, String opponent, String result) {
        // CSV
        try (BufferedWriter w = new BufferedWriter(new FileWriter(CSV, true))) {
            w.write(player + "," + opponent + "," + result + "," + DF.format(new Date()));
            w.newLine();
        } catch (IOException e) {
            System.err.println("[CSV] save error: " + e.getMessage());
        }

        // SQLite
        String winner = null;
        if ("Thắng".equals(result)) winner = player;
        else if ("Thua".equals(result)) winner = opponent; // đối phương thắng
        Database.saveMatchHistory(player, opponent, winner);

        // cập nhật bảng xếp hạng
        if ("Thắng".equals(result)) {
            Database.updateLeaderboard(player, true);
            Database.updateLeaderboard(opponent, false);
        } else if ("Thua".equals(result)) {
            Database.updateLeaderboard(player, false);
            Database.updateLeaderboard(opponent, true);
        } // Hòa -> không cộng
    }

    /** Đọc lịch sử (ưu tiên SQLite; fallback CSV). */
    public static List<String> readHistory(String user) {
        // thử SQLite
        try (ResultSet rs = Database.getHistory(user, 200)) {
            List<String> out = new ArrayList<>();
            while (rs.next()) {
                String p1 = rs.getString(1), p2 = rs.getString(2),
                       winner = rs.getString(3), playedAt = rs.getString(4);
                String opponent = p1.equals(user) ? p2 : p1;
                String result;
                if (winner == null) result = "Hòa";
                else if (winner.equals(user)) result = "Thắng";
                else result = "Thua";
                out.add(opponent + " → " + result + " (" + playedAt + ")");
            }
            return out;
        } catch (Exception ignore) { }

        // fallback CSV
        List<String> out = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(CSV))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 4 && p[0].equals(user)) {
                    out.add(p[1] + " → " + p[2] + " (" + p[3] + ")");
                }
            }
        } catch (IOException e) { System.err.println("[CSV] read error: " + e.getMessage()); }
        return out;
    }

    /** Leaderboard từ SQLite; fallback tính từ CSV. */
    public static LinkedHashMap<String,Integer> getLeaderboard() {
        // SQLite
        try (ResultSet rs = Database.getTopLeaderboard(100)) {
            LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
            while (rs.next()) map.put(rs.getString(1), rs.getInt(2));
            return map;
        } catch (Exception ignore) {}

        // CSV
        Map<String,Integer> wins = new HashMap<>();
        try (BufferedReader r = new BufferedReader(new FileReader(CSV))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 3 && "Thắng".equals(p[2])) {
                    wins.put(p[0], wins.getOrDefault(p[0], 0) + 1);
                }
            }
        } catch (IOException e) { System.err.println("[CSV] leaderboard: " + e.getMessage()); }
        List<Map.Entry<String,Integer>> list = new ArrayList<>(wins.entrySet());
        list.sort((a,b)-> b.getValue()-a.getValue());
        LinkedHashMap<String,Integer> res = new LinkedHashMap<>();
        for (var e : list) res.put(e.getKey(), e.getValue());
        return res;
    }
}
