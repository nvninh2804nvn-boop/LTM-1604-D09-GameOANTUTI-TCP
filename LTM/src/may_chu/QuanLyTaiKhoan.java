package may_chu;

import java.io.*;
import java.util.*;

public class QuanLyTaiKhoan {
    private static final String ACC_FILE = "db/accounts.txt";

    public static Map<String, String> loadAccounts() {
        Map<String, String> accounts = new HashMap<>();
        File file = new File(ACC_FILE);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) accounts.put(parts[0], parts[1]);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Không thể đọc accounts.txt");
        }
        return accounts;
    }

    public static void saveAccounts(Map<String, String> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACC_FILE))) {
            for (var entry : accounts.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi accounts.txt: " + e.getMessage());
        }
    }

	public static boolean login(String string, String string2) {
		// TODO Auto-generated method stub
		return false;
	}
}
