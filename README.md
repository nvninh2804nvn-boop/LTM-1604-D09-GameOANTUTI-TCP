<h1 align="center">🎓 Khoa Công nghệ Thông tin – Đại học Đại Nam</h1>
<h2 align="center">🕹️ GAME OẲN TÙ TÌ ONLINE (TCP SOCKET + JAVA SWING)</h2>

---

## 📘 1. Giới thiệu
Hệ thống mô phỏng trò chơi **Oẳn Tù Tì (Rock – Paper – Scissors)** giữa 2 người chơi thông qua **Java TCP Socket**.

### 🔹 Thành phần:
- **Server (máy chủ) 🖥️**
  - Quản lý tài khoản, kết nối, ghép cặp, xử lý trận đấu.
  - Lưu lịch sử & bảng xếp hạng.
- **Client (máy khách) 💻**
  - Giao diện Swing.
  - Đăng ký / đăng nhập, chơi, xem lịch sử, thoát phòng.

### 👉 Ưu điểm:
✅ Giao tiếp TCP ổn định  
✅ Giao diện trực quan với emoji ✊✋✌  
✅ Lưu lịch sử (CSV) & bảng xếp hạng  
✅ Thoát phòng an toàn  

---

## 🛠 2. Công nghệ sử dụng
- **Ngôn ngữ**: Java (JDK 17+ hoặc JDK 21)  
- **Giao diện**: Java Swing  
- **Kết nối**: TCP Socket  
- **Lưu trữ**: File `.txt`, `.csv`  
- **IDE**: Eclipse IDE  

---

## 📸 3. Ảnh giao diện

### 🔑 Đăng nhập
<img src="LTM/assets/login.png" alt="Login UI" width="500"/>

### 🎮 Chơi game
<img src="LTM/assets/game.png" alt="Game UI" width="500"/>

### 📂 Lịch sử
<img src="LTM/assets/history.png" alt="History UI" width="500"/>

### 🏆 Bảng xếp hạng
<img src="LTM/assets/leaderboard.png" alt="Leaderboard UI" width="500"/>

---

## 🚀 4. Cài đặt & chạy

### Cài môi trường
- JDK 17+ (hoặc JDK 21).  
- Eclipse IDE.  

### Clone project
```bash
git clone https://github.com/nhinhnhinh/LTM-1604-D09-Game-TCP.git
cd LTM-1604-D09-Game-TCP
Chạy chương trình
Server: LTM/src/server/AppServer.java

Client: LTM/src/client/ClientApp.java (mở 2 client để test).

File dữ liệu
Tài khoản: LTM/db/accounts.txt

Lịch sử: LTM/db/history.csv

📂 5. Cấu trúc thư mục
bash
Copy code
📦 LTM
 ┣ 📂 src
 ┃ ┣ 📂 client
 ┃ ┃ ┣ ClientApp.java          # Chạy client
 ┃ ┃ ┣ GameUI.java             # Giao diện Swing
 ┃ ┃ ┣ ClientConnection.java   # Xử lý kết nối TCP
 ┃ ┃ ┗ Utils.java              # Tiện ích (hash mật khẩu)
 ┃ ┗ 📂 server
 ┃    ┣ AppServer.java         # Chạy server
 ┃    ┣ ClientHandler.java     # Xử lý kết nối client
 ┃    ┣ GameLogic.java         # Luật Oẳn Tù Tì
 ┃    ┣ AccountManager.java    # Quản lý tài khoản
 ┃    ┣ HistoryManager.java    # Quản lý lịch sử + leaderboard
 ┃    ┗ GameManager.java       # Quản lý logic game
 ┣ 📂 db
 ┃ ┣ accounts.txt              # Lưu tài khoản
 ┃ ┗ history.csv               # Lưu lịch sử
 ┣ 📂 assets                   # Ảnh giao diện (README)
 ┃ ┣ login.png
 ┃ ┣ game.png
 ┃ ┣ history.png
 ┃ ┗ leaderboard.png
 ┗ README.md
🏆 6. Tính năng chính
🔒 Đăng ký / Đăng nhập (hash mật khẩu SHA-256).

👫 Ghép cặp tự động giữa 2 người chơi.

✊✋✌ Oẳn Tù Tì với emoji + màu sắc.

📜 Lưu lịch sử trận đấu.

🏆 Bảng xếp hạng dựa trên lịch sử.

🚪 Thoát phòng an toàn.

✍️ 7. Thông tin sinh viên
Họ tên: Nguyễn Việt Ninh

Lớp: CNTT 16-04

Môn học: Lập trình mạng (LTM)

Trường: Đại học Đại Nam
