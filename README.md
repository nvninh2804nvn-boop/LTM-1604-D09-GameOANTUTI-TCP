<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>

<h2 align="center">
   🎮 Hệ thống trò chơi Oẳn Tù Tì (TCP Socket + Java Swing)
</h2>

<div align="center">
    <p align="center">
        <img src="src/assets/aiotlab_logo.png" alt="AIoTLab Logo" width="160"/>
        <img src="src/assets/fitdnu_logo.png" alt="FIT DNU Logo" width="170"/>
        <img src="src/assets/dnu_logo.png" alt="DaiNam University Logo" width="190"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

---

## 📖 1. Giới thiệu hệ thống
Ứng dụng **Game Oẳn Tù Tì (Kéo – Búa – Bao)** được xây dựng theo mô hình **Client – Server** sử dụng **TCP Socket** trong Java.  

- **Server**: quản lý kết nối, tạo phòng chơi, xử lý logic và gửi kết quả cho client.  
- **Client**: giao diện người dùng (Java Swing) cho phép đăng nhập, tham gia/tạo phòng, chọn Búa – Bao – Kéo và hiển thị kết quả.  

👉 Hệ thống đảm bảo tính **công bằng – minh bạch – dễ mở rộng** với các tính năng lưu lịch sử và bảng xếp hạng.

---

## 🔧 2. Công nghệ sử dụng
<div align="center">

[![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)  
[![Swing](https://img.shields.io/badge/Java%20Swing-blue?style=for-the-badge)](https://docs.oracle.com/javase/tutorial/uiswing/)  
[![TCP Socket](https://img.shields.io/badge/TCP%20Socket-darkgreen?style=for-the-badge)](https://en.wikipedia.org/wiki/Transmission_Control_Protocol)  

</div>

---

## 📂 3. Cấu trúc thư mục

---

## 🔄 4. Luồng hoạt động
1. Server khởi động, mở cổng lắng nghe.  
2. Client kết nối tới server, thực hiện đăng nhập bằng tài khoản trong `accounts.txt`.  
3. Người chơi tạo phòng hoặc tham gia phòng.  
4. Trong một ván:  
   - Client gửi lựa chọn (Búa / Bao / Kéo).  
   - Server xử lý kết quả, gửi lại cho tất cả người chơi.  
5. Client hiển thị kết quả, lưu lịch sử và cập nhật bảng xếp hạng.  

---

## 🖼️ 5. Một số hình ảnh hệ thống  

### 🔑 Giao diện đăng nhập Client  
![Login](src/assets/login.png.png)  

### 🎮 Giao diện chơi game  
![Game](src/assets/game.png.png)  

### 📜 Giao diện xem lịch sử  
![History](src/assets/history.png.png)  

### 🏆 Giao diện bảng xếp hạng  
![Leaderboard](src/assets/leaderboard.png.png)  

---

## ⚙️ 6. Hướng dẫn chạy
- **Server**

## 📞 7. Liên hệ
- 👤 Họ và tên: NGUYỄN VIỆT NINH 
- 🎓 Lớp: CNTT 16-04
- 📧 Email: nvninh2804nvn.@gmail.com* 
- 📱 Điện thoại: 0969782404  

