<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   GAME OẲN TÙ TÌ QUA MẠNG (TCP)
</h2>
<div align="center">
    <p align="center">
        <img src="src/assets/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="src/assets/fitdnu_logo.png" alt="FIT DNU Logo" width="180"/>
        <img src="src/assets/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

---

## 📖 1. Giới thiệu hệ thống
Ứng dụng **Oẳn Tù Tì qua mạng (TCP Socket)** được xây dựng nhằm mô phỏng trò chơi quen thuộc, cho phép người chơi thi đấu trực tuyến qua LAN hoặc Internet.

### 🔹 Hệ thống hỗ trợ:
- Tạo phòng chơi hoặc tham gia phòng theo mã / ngẫu nhiên.  
- Thực hiện chọn ✊ Đá / ✋ Bao / ✌ Kéo và trả về kết quả tức thì.  
- Đảm bảo tính ổn định – uy tín – công bằng.  

### 🎯 Mục tiêu chính:
- Nắm vững kiến thức lập trình mạng với TCP Socket trong Java.  
- Thực hành xây dựng ứng dụng Client–Server nhiều người dùng.  
- Ứng dụng vào bài tập lớn, đồ án, hệ thống game online cơ bản.  

---

## 🛠️ 2. Công nghệ sử dụng
[![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![TCP Socket](https://img.shields.io/badge/TCP--Socket-008080?style=for-the-badge)](#)
[![Java Swing](https://img.shields.io/badge/Java%20Swing-6DB33F?style=for-the-badge)](#)
[![Eclipse](https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipseide&logoColor=white)](#)

- Ngôn ngữ lập trình: Java  
- Giao tiếp mạng: TCP Socket (`ServerSocket`, `Socket`, `I/O Streams`)  
- Mô hình kiến trúc: Client–Server  
- Giao diện người dùng:  
  - Java Swing (kết hợp FlatLaf để UI hiện đại)  
  - Có thể mở rộng bằng JavaFX  
- Quản lý dự án: Maven / Gradle (tuỳ chọn)  
- IDE khuyến nghị: IntelliJ IDEA, Eclipse, NetBeans  

---

## 🖼️ 3. Một số hình ảnh hệ thống

### 📝 Đăng ký
<p align="center">
  <img src="src/assets/Dangki.png.png" alt="Register UI" width="500"/>
  <br>
  <em>Màn hình đăng ký – tạo tài khoản mới để tham gia hệ thống.</em>
</p>

---

### 🔑 Đăng nhập
<p align="center">
  <img src="src/assets/Dangnhap.png.png" alt="Login UI" width="500"/>
  <br>
  <em>Màn hình đăng nhập – người chơi nhập tài khoản và mật khẩu để vào hệ thống.</em>
</p>

---

### 🏠 Trang chủ
<p align="center">
  <img src="src/assets/trangchu.png.png" alt="Home UI" width="500"/>
  <br>
  <em>Màn hình trang chủ – hiển thị danh sách người chơi online, mời đấu hoặc ghép ngẫu nhiên.</em>
</p>

---

### 🎮 Chơi game
<p align="center">
  <img src="src/assets/choi.png.png" alt="Game UI" width="500"/>
  <br>
  <em>Màn hình chơi game – chọn ✊ Đá / ✋ Bao / ✌ Kéo để thi đấu trực tuyến với đối thủ.</em>
</p>

---

### 🏆 Bảng xếp hạng
<p align="center">
  <img src="src/assets/top.png.png" alt="Leaderboard UI" width="500"/>
  <br>
  <em>Màn hình bảng xếp hạng – thống kê thành tích, xếp hạng người chơi theo số trận thắng.</em>
</p>

---

### 📂 Lịch sử
<p align="center">
  <img src="src/assets/Lichsu.png.png" alt="History UI" width="500"/>
  <br>
  <em>Màn hình lịch sử – hiển thị kết quả các trận đã chơi: đối thủ, kết quả, thời gian.</em>
</p>

---

### 🔒 Đổi mật khẩu
<p align="center">
  <img src="src/assets/doimatkhau.png.png" alt="Change Password UI" width="500"/>
  <br>
  <em>Màn hình đổi mật khẩu – người chơi thay đổi mật khẩu tài khoản của mình.</em>
</p>

---

## ⚙️ 4. Cài đặt
1. Cài đặt **JDK 17+ hoặc JDK 21**  
2. Cài **IDE** (Eclipse / IntelliJ / NetBeans)  
3. Clone project:
   ```bash
   git clone https://github.com/nvninh2804nvn-boop/LTM-1604-D09-Game-TCP.git
   cd LTM-1604-D09-Game-TCP
