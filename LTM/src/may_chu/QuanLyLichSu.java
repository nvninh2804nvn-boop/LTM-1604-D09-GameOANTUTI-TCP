package may_chu;

import java.io.*;
import java.util.*;

public class QuanLyLichSu {
    private static final String FILE = "db/history.csv";

    /** Lưu lịch sử trận đấu */
    public static void saveHistory(String player, String opponent, String result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            writer.write(player + "," + opponent + "," + result);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Lỗi ghi lịch sử: " + e.getMessage());
        }
    }

    /** Đọc lịch sử của 1 người chơi */
    public static List<String> readHistory(String player) {
        List<String> history = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(player)) {
                    history.add(parts[1] + " → " + parts[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc lịch sử: " + e.getMessage());
        }
        return history;
    }

    /** Tính leaderboard: Top người thắng nhiều nhất */
    public static Map<String, Integer> getLeaderboard() {
        Map<String, Integer> wins = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String player = parts[0];
                    String result = parts[2];
                    if ("Thắng".equals(result)) {
                        wins.put(player, wins.getOrDefault(player, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc leaderboard: " + e.getMessage());
        }

        // Sắp xếp giảm dần theo số trận thắng
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wins.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        // Trả về map có thứ tự (LinkedHashMap)
        Map<String, Integer> sorted = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : list) {
            sorted.put(e.getKey(), e.getValue());
        }
        return sorted;
    }

    // Vẫn giữ để dùng cho AccountManager
    public static Map<String, String> loadAccounts() {
        return QuanLyTaiKhoan.loadAccounts();
    }

    public static void saveAccounts(Map<String, String> accs) {
        QuanLyTaiKhoan.saveAccounts(accs);
    }
}
