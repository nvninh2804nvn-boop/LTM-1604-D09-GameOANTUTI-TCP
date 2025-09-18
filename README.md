<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>

<h1 align="center">
   GAME OẲN TÙ TÌ QUA MẠNG
</h1>

<div align="center">
    <p>
        <img src="LTM/src/assets/aiotlab_logo.png" alt="AIoTLab Logo" width="150"/>
        <img src="LTM/src/assets/fitdnu_logo.png" alt="FIT DNU Logo" width="160"/>
        <img src="LTM/src/assets/dnu_logo.png" alt="DaiNam University Logo" width="180"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-28a745?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![FIT](https://img.shields.io/badge/Faculty%20of%20IT-007bff?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DNU](https://img.shields.io/badge/DaiNam%20University-f97316?style=for-the-badge)](https://dainam.edu.vn)
</div>

---

## 1. 📖 Giới thiệu hệ thống
Ứng dụng *Game Oẳn Tù Tì (Kéo – Búa – Bao)* được xây dựng theo mô hình Client – Server sử dụng TCP Socket trong Java.  

- Server: quản lý kết nối, tạo phòng chơi, xử lý logic và gửi kết quả cho client.  
- Client: giao diện người dùng (Java Swing) cho phép đăng nhập, tham gia/tạo phòng, chọn Búa – Bao – Kéo và hiển thị kết quả.  

👉 Hệ thống hướng tới sự công bằng, minh bạch, dễ mở rộng, có hỗ trợ lưu lịch sử và bảng xếp hạng.

---

## 2. 🔧 Công nghệ sử dụng
<div align="center">

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)  
[![Swing](https://img.shields.io/badge/Java%20Swing-5382A1?style=for-the-badge)](https://docs.oracle.com/javase/tutorial/uiswing/)  
[![TCP Socket](https://img.shields.io/badge/TCP%20Socket-004880?style=for-the-badge)](https://en.wikipedia.org/wiki/Transmission_Control_Protocol)  
[![FlatLaf](https://img.shields.io/badge/FlatLaf-FF6F00?style=for-the-badge)](https://www.formdev.com/flatlaf/)  

</div>

**Mô tả ngắn gọn**  
- Java: ngôn ngữ lập trình chính để xây dựng cả Client và Server.  
- Java Swing: thư viện tạo giao diện người dùng.  
- TCP Socket: giao thức truyền thông tin hai chiều giữa Client ↔ Server.  
- FlatLaf: thư viện làm đẹp giao diện Swing hiện đại.  

---

## 3. 📂 Cấu trúc thư mục

---

## 🔄 Luồng hoạt động
1. Server khởi động và mở cổng lắng nghe.  
2. Client kết nối tới server, đăng nhập bằng tài khoản trong `accounts.txt`.  
3. Người chơi tạo phòng hoặc tham gia phòng.  
4. Trong mỗi ván:  
   - Client gửi lựa chọn (Búa / Bao / Kéo).  
   - Server xử lý kết quả và gửi lại cho các client.  
5. Client hiển thị kết quả, lưu lịch sử và cập nhật bảng xếp hạng.  

---

## 🖼️ Hình ảnh hệ thống
<div align="center">

### 🔑 Đăng nhập
<img src="LTM/src/assets/login.png.png" width="400"/>  

### 🎮 Chơi game
<img src="LTM/src/assets/game.png.png" width="400"/>  

### 📜 Lịch sử
<img src="LTM/src/assets/history.png.png" width="400"/>  

### 🏆 Bảng xếp hạng
<img src="LTM/src/assets/leaderboard.png.png" width="400"/>  

</div>

---

## ⚙️ Hướng dẫn chạy
*(Sẽ cập nhật chi tiết về build và chạy server/client sau)*

---

## 📬 Liên hệ
> 🎓 **Khoa Công nghệ Thông tin – Đại học Đại Nam**  
> 🌐 [Website Khoa CNTT – DNU](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)  
> 📧 Email: nvninh2804@gmail.com  
> 📱 Fanpage: AIoTLab – FIT DNU  

<div align="center">
© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.
</div>
