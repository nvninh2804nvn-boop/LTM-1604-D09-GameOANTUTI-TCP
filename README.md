<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>

<h2 align="center">
   GAME OẲN TÙ TÌ QUA MẠNG
</h2>

<div align="center">
    <p align="center">
        <img src="LTM/src/assets/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="LTM/src/assets/fitdnu_logo.png" alt="FIT DNU Logo" width="180"/>
        <img src="LTM/src/assets/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

---

## 📖 1. Giới thiệu hệ thống
Ứng dụng **Game Oẳn Tù Tì (Kéo – Búa – Bao)** được xây dựng theo mô hình **Client – Server** sử dụng **TCP Socket** trong Java.  

- **Server**: quản lý kết nối, tạo phòng chơi, xử lý logic và gửi kết quả cho client.  
- **Client**: giao diện người dùng (Java Swing) cho phép đăng nhập, tham gia/tạo phòng, chọn Búa – Bao – Kéo và hiển thị kết quả.  

👉 Hệ thống đảm bảo **công bằng – minh bạch – dễ mở rộng**, có các tính năng lưu lịch sử và bảng xếp hạng.

---

## 🔧 2. Công nghệ sử dụng
<div align="center">

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)  
[![Swing](https://img.shields.io/badge/Java%20Swing-5382A1?style=for-the-badge&logo=coffeescript&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)  
[![TCP Socket](https://img.shields.io/badge/TCP%20Socket-004880?style=for-the-badge&logo=socketdotio&logoColor=white)](https://en.wikipedia.org/wiki/Transmission_Control_Protocol)  
[![FlatLaf](https://img.shields.io/badge/FlatLaf-FF6F00?style=for-the-badge&logo=apache&logoColor=white)](https://www.formdev.com/flatlaf/)  

</div>

### ✨ Mô tả ngắn gọn
- **Java**: Ngôn ngữ lập trình chính để xây dựng cả Client và Server.  
- **Java Swing**: Thư viện tạo giao diện người dùng trực quan.  
- **TCP Socket**: Giao thức truyền thông tin hai chiều giữa Client ↔ Server.  
- **FlatLaf**: Thư viện làm đẹp giao diện Swing hiện đại.  

---

## 📂 3. Cấu trúc thư mục

---

## 🔄 4. Luồng hoạt động
1. Server khởi động, mở cổng lắng nghe.  
2. Client kết nối tới server, đăng nhập bằng tài khoản trong `accounts.txt`.  
3. Người chơi tạo hoặc tham gia phòng.  
4. Trong một ván:  
   - Client gửi lựa chọn (Búa / Bao / Kéo).  
   - Server xử lý kết quả, gửi lại cho tất cả người chơi.  
5. Client hiển thị kết quả, lưu lịch sử và cập nhật bảng xếp hạng.  

---

## 🖼️ 5. Một số hình ảnh hệ thống  

### 🔑 Giao diện đăng nhập Client  
![Login](LTM/src/assets/login.png.png)  

### 🎮 Giao diện chơi game  
![Game](LTM/src/assets/game.png.png)  

### 📜 Giao diện xem lịch sử  
![History](LTM/src/assets/history.png.png)  

### 🏆 Giao diện bảng xếp hạng  
![Leaderboard](LTM/src/assets/leaderboard.png.png)  

---

## ⚙️ 6. Hướng dẫn chạy
*(Cập nhật sau nếu cần hướng dẫn build/run cụ thể)*

---

## 📬 Liên hệ 
> 🎓 **Khoa**: Công nghệ Thông tin – Đại học Đại Nam  
> 🌐 [Website Khoa CNTT – DNU](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)  
> 📧 Email: **nvninh2804@gmail.com**  
> 📱 Fanpage: **AIoTLab – FIT DNU**  

<p align="center">
© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved
</p>
