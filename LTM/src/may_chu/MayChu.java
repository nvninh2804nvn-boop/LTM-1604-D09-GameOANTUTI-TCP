package may_chu;

import java.net.*;

public class MayChu {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(7777);
        System.out.println("Server started on port 7777...");
        while (true) {
            Socket socket = server.accept();
            System.out.println("New client connected");
            new Thread(new XuLyKhach(socket)).start();
        }
    }
}
