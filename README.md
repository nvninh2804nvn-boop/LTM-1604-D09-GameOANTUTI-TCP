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
Ứng dụng *Game Oẳn Tù Tì (Kéo – Búa – Bao)* được phát triển theo mô hình **Client – Server** sử dụng **TCP Socket** trong Java.  
- **Server**: quản lý kết nối, tạo phòng chơi, nhận lựa chọn từ người chơi, xử lý kết quả.  
- **Client**: giao diện người dùng (Java Swing), cho phép đăng nhập, tham gia hoặc tạo phòng, chọn Búa/Bao/Kéo và xem kết quả.  

👉 Hệ thống hướng đến tính **minh bạch – công bằng – dễ mở rộng** với tính năng lưu lịch sử và bảng xếp hạng.

---

## 🔧 2. Công nghệ sử dụng
<div align="center">

[![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)  
[![Swing](https://img.shields.io/badge/Java%20Swing-blue?style=for-the-badge)](https://docs.oracle.com/javase/tutorial/uiswing/)  
[![TCP Socket](https://img.shields.io/badge/TCP%20Socket-darkgreen?style=for-the-badge)](https://en.wikipedia.org/wiki/Transmission_Control_Protocol)  

</div>

---

## 📂 3. Cấu trúc thư mục dự án


## 🔄 4. Luồng hoạt động
1. **Server** khởi động, lắng nghe kết nối từ client.  
2. **Client** mở ứng dụng, thực hiện đăng nhập bằng tài khoản trong `accounts.txt`.  
3. Người chơi có thể **tạo phòng** hoặc **tham gia phòng** có sẵn.  
4. Khi đủ người chơi:  
   - Client gửi lựa chọn (Búa / Bao / Kéo) đến server.  
   - Server xử lý kết quả và gửi trả lại cho toàn bộ client.  
5. **Client** hiển thị kết quả và có thể tiếp tục chơi hoặc thoát.  

---

## 🖼️ 5. Một số hình ảnh hệ thống  

### 🖥️ Giao diện đăng nhập Client  
![Login](src/assets/client_login.png)  

### 🎮 Giao diện chơi game  
![Game](src/assets/client_game.png)  

### 🖥️ Giao diện Server quản lý kết nối  
![Server](src/assets/server_dashboard.png)  

---

## ⚙️ 6. Hướng dẫn chạy
- **Server**
  ```bash
  cd src/may_chu
  javac AppServer.java
  java AppServer
Client

bash
Sao chép mã
cd src/nguoi_choi
javac ClientApp.java
java ClientApp
📞 7. Liên hệ cá nhân
👤 Họ và tên: NGUYỄN VIỆT NINH

🎓 Lớp: CNTT 16-04

📧 Email: dnagbinh12@gmail.com

📱 Số điện thoại: 0822968881
