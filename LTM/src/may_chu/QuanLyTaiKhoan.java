package may_chu;

import java.io.*;
import java.util.*;

/** Lưu user:pass bằng file db/accounts.txt cho đơn giản. */
public class QuanLyTaiKhoan {
    private static final String ACC_FILE = "db/accounts.txt";

    /** Đảm bảo db/ và file tồn tại. */
    public static void ensureStorage() {
        try {
            File f = new File(ACC_FILE);
            File dir = f.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            if (!f.exists()) f.createNewFile();
        } catch (IOException e) {
            System.err.println("[ACC] ensureStorage error: " + e.getMessage());
        }
    }

    /** Đọc accounts -> map (giữ thứ tự để dễ diff). */
    public static Map<String, String> loadAccounts() {
        ensureStorage();
        Map<String, String> map = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ACC_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                int i = line.indexOf(':');
                if (i > 0) {
                    String user = line.substring(0, i).trim();
                    String pass = line.substring(i + 1);
                    if (!user.isEmpty()) map.put(user, pass);
                }
            }
        } catch (IOException e) {
            System.err.println("[ACC] load error: " + e.getMessage());
        }
        return map;
    }

    /** Ghi accounts theo alphabet để không “đảo” lung tung. */
    public static void saveAccounts(Map<String, String> accounts) {
        ensureStorage();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ACC_FILE))) {
            var users = new ArrayList<>(accounts.keySet());
            users.sort(String.CASE_INSENSITIVE_ORDER);
            for (String u : users) {
                bw.write(u + ":" + accounts.get(u));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("[ACC] save error: " + e.getMessage());
        }
    }
}
