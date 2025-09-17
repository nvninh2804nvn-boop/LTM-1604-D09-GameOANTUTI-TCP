<p align="center">
  <img src="LTM/src/assets/aiotlab_logo.png" alt="AIoT Lab" width="150"/>
  <img src="LTM/src/assets/fitdnu_logo.png" alt="FIT DNU" width="200"/>
  <img src="LTM/src/assets/dnu_logo.png" alt="DaiNam University" width="200"/>
</p>

<h1 align="center">🎓 Khoa Công nghệ Thông tin – Đại học Đại Nam</h1>
<h2 align="center">🕹️ Ứng dụng Oẳn Tù Tì Online (TCP Socket + Java Swing)</h2>

<p align="center">
  <a href="https://www.java.com/"><img src="https://img.shields.io/badge/Java-17+-red?style=for-the-badge&logo=java&logoColor=white"/></a>
  <a href="#"><img src="https://img.shields.io/badge/TCP-Socket-blue?style=for-the-badge"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Swing-UI-orange?style=for-the-badge"/></a>
  <a href="https://www.eclipse.org/"><img src="https://img.shields.io/badge/Eclipse-IDE-purple?style=for-the-badge&logo=eclipseide&logoColor=white"/></a>
</p>

---

## 📖 1. Giới thiệu hệ thống
Ứng dụng **Oẳn Tù Tì qua mạng (TCP Socket)** được xây dựng nhằm mô phỏng trò chơi quen thuộc, cho phép người chơi thi đấu trực tuyến qua **LAN** hoặc **Internet**.

### 🔹 Hệ thống hỗ trợ:
- Tạo phòng chơi hoặc tham gia phòng theo mã / ngẫu nhiên.  
- Thực hiện chọn **✊ Kéo / ✋ Bao / ✌ Búa** và trả về kết quả tức thì.  
- Đảm bảo tính **ổn định – uy tín – công bằng**.  

### 🎯 Mục tiêu chính:
- Nắm vững kiến thức **lập trình mạng với TCP Socket trong Java**.  
- Thực hành xây dựng **ứng dụng Client–Server nhiều người dùng**.  
- Ứng dụng vào **bài tập lớn, đồ án, hệ thống game online cơ bản**.  

---

## 🛠️ 2. Công nghệ sử dụng
- **Ngôn ngữ lập trình**: Java  
- **Giao tiếp mạng**: TCP Socket (`ServerSocket`, `Socket`, `I/O Streams`)  
- **Mô hình kiến trúc**: Client–Server  
- **Giao diện người dùng**:  
  - Java Swing (kết hợp **FlatLaf** để UI hiện đại)  
  - Có thể mở rộng bằng **JavaFX**  
- **Quản lý dự án**: Maven / Gradle (tuỳ chọn)  
- **IDE khuyến nghị**: IntelliJ IDEA, Eclipse, NetBeans  

---

## 🖼️ 3. Một số hình ảnh hệ thống

### 🔑 Đăng nhập
<img src="LTM/src/assets/login.png.png" alt="Login UI" width="500"/>

### 🎮 Chơi game
<img src="LTM/src/assets/game.png.png" alt="Game UI" width="500"/>

### 📂 Lịch sử
<img src="LTM/src/assets/history.png.png" alt="History UI" width="500"/>

### 🏆 Bảng xếp hạng
<img src="LTM/src/assets/leaderboard.png.png" alt="Leaderboard UI" width="500"/>

---

## ⚙️ 4. Các bước cài đặt
1. Cài đặt **JDK 17+ hoặc JDK 21**.  
2. Cài **IDE** (Eclipse / IntelliJ / NetBeans).  
3. Clone project:
   ```bash
   git clone https://github.com/nhinhnhinh/LTM-1604-D09-Game-TCP.git
   cd LTM-1604-D09-Game-TCP
Chạy:

Server: LTM/src/may_chu/AppServer.java

Client: LTM/src/nguoi_choi/ClientApp.java (chạy 2 client để test).

📂 5. Cấu trúc thư mục
css
Copy code
📦 LTM
 ┣ 📂 bin
 ┣ 📂 db
 ┣ 📂 src
 ┃ ┣ 📂 assets
 ┃ ┃ ┣ aiotlab_logo.png
 ┃ ┃ ┣ dnu_logo.png
 ┃ ┃ ┣ fitdnu_logo.png
 ┃ ┃ ┣ login.png.png
 ┃ ┃ ┣ game.png.png
 ┃ ┃ ┣ history.png.png
 ┃ ┃ ┗ leaderboard.png.png
 ┃ ┣ 📂 may_chu
 ┃ ┗ 📂 nguoi_choi
 ┣ accounts.txt
 ┗ README.md
📬 6. Liên hệ
👤 Sinh viên thực hiện: Nguyễn Việt Ninh
🎓 Khoa: Công nghệ Thông tin – Đại học Đại Nam
🌐 Website Khoa CNTT – DNU
📧 Email: nvninh2804@gmail.com
📱 Fanpage: AIoTLab – FIT DNU

<p align="center"> <img src="LTM/src/assets/dnu_logo.png" alt="DaiNam Logo" width="200"/> </p> ```
