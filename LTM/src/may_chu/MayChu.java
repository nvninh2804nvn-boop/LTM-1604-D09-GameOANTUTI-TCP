package may_chu;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class MayChu {
    // Danh sách client online: username -> handler
    public static ConcurrentHashMap<String, XuLyKhach> clients = new ConcurrentHashMap<>();

    // Quản lý tài khoản / trò chơi (dùng chung)
    public static QuanLyTaiKhoan qlTaiKhoan = new QuanLyTaiKhoan();
    public static QuanLyTroChoi qlTroChoi = new QuanLyTroChoi();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(7777)) {
            System.out.println("🚀 Server started on port 7777...");
            // Tạo bảng DB nếu chưa có
            Database.createTables();

            while (true) {
                Socket socket = server.accept();
                System.out.println("🔗 New client connected: " + socket.getRemoteSocketAddress());
                new Thread(new XuLyKhach(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

