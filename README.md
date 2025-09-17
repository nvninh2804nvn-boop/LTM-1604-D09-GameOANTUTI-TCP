h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Khoa Công nghệ Thông tin – Trường Đại học Đại Nam
    </a>
</h2>

<h2 align="center">
   🕹️ GAME OẲN TÙ TÌ ONLINE (TCP SOCKET + JAVA SWING)
</h2>

---

## 📘 1. Giới thiệu  

Hệ thống mô phỏng **trò chơi Oẳn Tù Tì (Rock – Paper – Scissors)** giữa 2 người chơi thông qua **Java TCP Socket**.  

### Thành phần:
- **Server (máy chủ)** 🖥️  
   - Quản lý tài khoản, kết nối, ghép cặp, xử lý trận đấu.  
   - Lưu lịch sử & bảng xếp hạng.  

- **Client (máy khách)** 💻  
   - Giao diện Swing.  
   - Đăng ký / đăng nhập, chơi, xem lịch sử, thoát phòng.  

👉 **Ưu điểm:**  
✅ Giao tiếp TCP ổn định  
✅ Giao diện dễ dùng, trực quan với emoji ✊✋✌  
✅ Lưu lịch sử (CSV) & bảng xếp hạng  
✅ Có thoát phòng an toàn  

---

## 🛠 2. Công nghệ sử dụng  

- **Ngôn ngữ:** Java (JDK 17+ hoặc JDK 21)  
- **Giao diện:** Java Swing  
- **Kết nối:** TCP Socket  
- **Lưu trữ:** File txt, csv  
- **IDE:** Eclipse IDE  

---

## 📸 3. Ảnh giao diện  

> ⚠️ Đặt ảnh trong thư mục `LTM/assets/` rồi mới hiện được nhé  

### 🔑 Đăng nhập  
![Đăng nhập](LTM/assets/login.png)

### 🎮 Chơi game  
![Chơi game](LTM/assets/game.png)

### 📂 Lịch sử  
![Lịch sử](LTM/assets/history.png)

### 🏆 Bảng xếp hạng  
![Bảng xếp hạng](LTM/assets/leaderboard.png)

---

## 🚀 4. Cài đặt & chạy  

1. **Cài môi trường**  
   - JDK 17+ (hoặc JDK 21).  
   - Eclipse IDE.  

2. **Clone project**  
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
📦 LTM

src/

client/

ClientApp.java – chạy client

GameUI.java – giao diện Swing

ClientConnection.java – xử lý kết nối TCP

Utils.java – tiện ích (hash mật khẩu)

server/

AppServer.java – chạy server

ClientHandler.java – xử lý kết nối client

GameLogic.java – luật Oẳn Tù Tì

AccountManager.java – quản lý tài khoản

HistoryManager.java – quản lý lịch sử + leaderboard

GameManager.java – quản lý logic game (nếu có)

db/

accounts.txt – lưu tài khoản

history.csv – lưu lịch sử

assets/ (chứa ảnh README)

login.png, game.png, history.png, leaderboard.png

README.md – mô tả project

🏆 6. Tính năng chính
🔒 Đăng ký / Đăng nhập (hash mật khẩu SHA-256).

👫 Ghép cặp tự động giữa 2 người chơi.

✊✋✌ Oẳn Tù Tì với emoji + màu sắc.

📜 Lưu lịch sử trận đấu.

🏆 Bảng xếp hạng dựa trên lịch sử.

🚪 Thoát phòng.

✍️ 7. Thông tin
Sinh viên: Nguyễn Việt Ninh – CNTT 16-04

Môn học: Lập trình mạng (LTM)

Trường: Đại học Đại Nam
