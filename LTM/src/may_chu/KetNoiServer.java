package may_chu;

import java.io.*;
import java.net.*;
import java.util.*;

public class KetNoiServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;

    public KetNoiServer() {
        clients = new ArrayList<>();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(12345);  // Chạy server trên cổng 12345
            System.out.println("Server đang chờ kết nối...");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private String username;
        private boolean isPlaying = false;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = (String) in.readObject();
                    processMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void processMessage(String message) {
            String[] parts = message.split(":");
            String command = parts[0];

            switch (command) {
                case "LOGIN":
                    handleLogin(parts[1], parts[2]);
                    break;
                case "REGISTER":
                    handleRegister(parts[1], parts[2]);
                    break;
                case "MOVE":
                    handleMove(parts[1]);
                    break;
                case "LEAVE":
                    handleLeave();
                    break;
                default:
                    break;
            }
        }

        private void handleLogin(String username, String password) {
            if (Database.checkUserExists(username, password)) {
                this.username = username;
                try {
                    out.writeObject("LOGIN_SUCCESS:" + username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    out.writeObject("LOGIN_FAIL");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleRegister(String username, String password) {
            if (!Database.checkUserExists(username, password)) {
                Database.addUser(username, password);
                try {
                    out.writeObject("REGISTER_SUCCESS");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    out.writeObject("REGISTER_FAIL");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleMove(String move) {
            String result = calculateResult(move);
            try {
                out.writeObject("GAME_RESULT:" + result);
                // Lưu lịch sử trận đấu và cập nhật bảng xếp hạng
                Database.saveMatchHistory(username, "opponent", result);  // Giả sử đối thủ là "opponent"
                boolean isWin = result.equals("Thắng!");
                Database.updateLeaderboard(username, isWin);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String calculateResult(String move) {
            String opponentMove = "ROCK";  // Giả sử đối thủ chọn Đá
            if (move.equals(opponentMove)) {
                return "Hòa!";
            } else if (move.equals("ROCK") && opponentMove.equals("SCISSORS") || 
                       move.equals("PAPER") && opponentMove.equals("ROCK") ||
                       move.equals("SCISSORS") && opponentMove.equals("PAPER")) {
                return "Thắng!";
            } else {
                return "Thua!";
            }
        }

        private void handleLeave() {
            try {
                out.writeObject("GAME_LEFT");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
