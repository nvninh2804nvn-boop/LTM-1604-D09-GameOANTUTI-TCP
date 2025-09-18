<div align="center">
  <h2>
    🎓 <span style="color:#0073e6;">Faculty of Information Technology (DaiNam University)</span>
  </h2>
  <hr style="width: 80%;"/>
  <h2>NETWORK PROGRAMMING</h2>
  <hr style="width: 80%;"/>
  
  <p>
    <img src="LTM/src/assets/aiotlab_logo.png" alt="AIoT Lab" width="150" style="margin: 0 20px;"/>
    <img src="LTM/src/assets/fitdnu_logo.png" alt="FIT DNU" width="200" style="margin: 0 20px;"/>
    <img src="LTM/src/assets/dnu_logo.png" alt="DaiNam University" width="200" style="margin: 0 20px;"/>
  </p>

  <p>
    <a href="#"><span style="background:#8cc63f; color:white; padding:8px 15px; border-radius:5px; font-weight:bold;">AIOTLAB</span></a>
    <a href="#"><span style="background:#0073e6; color:white; padding:8px 15px; border-radius:5px; font-weight:bold;">FACULTY OF INFORMATION TECHNOLOGY</span></a>
    <a href="#"><span style="background:#f36f21; color:white; padding:8px 15px; border-radius:5px; font-weight:bold;">DAINAM UNIVERSITY</span></a>
  </p>
</div>


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

> ## ⚙️ Cài đặt
> 1. Cài đặt **JDK 17+ hoặc JDK 21**  
> 2. Cài **IDE** (Eclipse / IntelliJ / NetBeans)  
> 3. Clone project:
>    ```bash
>    git clone https://github.com/nvninh2804nvn-boop/LTM-1604-D09-Game-TCP.git
>    cd LTM-1604-D09-Game-TCP
>    ```
> 4. Chạy chương trình:
>    - **Server**: `LTM/src/may_chu/AppServer.java`  
>    - **Client**: `LTM/src/nguoi_choi/ClientApp.java` (mở 2 client để test)  

---

> ## 📂 Cấu trúc thư mục
> ```
> 📦 LTM
> ┣ 📂 bin
> ┣ 📂 db
> ┣ 📂 src
> ┃ ┣ 📂 assets
> ┃ ┃ ┣ aiotlab_logo.png
> ┃ ┃ ┣ dnu_logo.png
> ┃ ┃ ┣ fitdnu_logo.png
> ┃ ┃ ┣ login.png.png
> ┃ ┃ ┣ game.png.png
> ┃ ┃ ┣ history.png.png
> ┃ ┃ ┗ leaderboard.png.png
> ┃ ┣ 📂 may_chu
> ┃ ┗ 📂 nguoi_choi
> ┣ accounts.txt
> ┗ README.md
> ```

---

> ## 📬 Liên hệ 
> 🎓 **Khoa**: Công nghệ Thông tin – Đại học Đại Nam  
> 🌐 [Website Khoa CNTT – DNU](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)  
> 📧 Email: **nvninh2804@gmail.com**  
> 📱 Fanpage: **AIoTLab – FIT DNU**  
>
> <p align="center">
© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved
> </p>

