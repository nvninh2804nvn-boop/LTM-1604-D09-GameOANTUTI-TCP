package may_chu;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Entry của server TCP. */
public class MayChu {
    public static final int PORT = 7777;

    /** Danh bạ người online: username -> handler */
    public static final Map<String, XuLyKhach> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        // đảm bảo DB & file sẵn sàng
        QuanLyTaiKhoan.ensureStorage();
        Database.createTables();

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT + " ...");
            while (true) {
                Socket socket = server.accept();
                new Thread(new XuLyKhach(socket), "ClientHandler").start();
            }
        }
    }
}
