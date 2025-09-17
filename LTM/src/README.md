 🎮 GAME OẢN TÙ TÌ TCP

![AIoTLab Logo](assets/aiotlab.png)  
![Khoa CNTT Logo](assets/fit.png)  
![Đại học Đại Nam Logo](assets/dainam.png)  

[AIoTLab](https://www.facebook.com/DNUAIoTLab) | [Khoa Công nghệ Thông tin](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin) | [Trường Đại học Đại Nam](https://dainam.edu.vn)

---

📘 1. Giới thiệu
Hệ thống được xây dựng nhằm mô phỏng trò chơi Oẳn Tù Tì (Rock–Paper–Scissors) giữa hai người chơi thông qua Java TCP Socket.  
Gồm 2 phần:
- Máy chủ (Server): quản lý tài khoản, kết nối, ghép cặp, xử lý trận đấu, lưu lịch sử, bảng xếp hạng.  
- Máy khách (Client): giao diện Swing thân thiện, cho phép đăng nhập, chơi, xem lịch sử, thoát phòng.  

👉 Ưu điểm:
- Kết nối TCP ổn định.  
- Giao diện trực quan, dùng emoji ✊✋✌.  
- Có lưu lịch sử và bảng xếp hạng.  
- Hỗ trợ thoát phòng an toàn.  

---

 🛠 2. Công nghệ sử dụng
- Ngôn ngữ: Java (JDK 17+ / JDK 21)  
- Giao diện: Java Swing  
- Kết nối: TCP Socket  
- Lưu trữ: File txt, csv  
- IDE khuyến nghị: Eclipse IDE  

---
 📸 3. Ảnh giao diện

 🔑 Đăng nhập
![Giao diện đăng nhập](assets/login.png)

 🎮 Chơi game
![Giao diện chơi](assets/game.png)
  📂 Lịch sử
![Lịch sử trận đấu](assets/history.png)
🏆 Bảng xếp hạng
![Bảng xếp hạng](assets/leaderboard.png)

---
 🚀 4. Cài đặt & chạy
1. Cài **JDK 17+** (hoặc JDK 21).  
2. Clone hoặc copy project về máy.  
3. Mở project trong Eclipse.  
4. Chạy **Server**: `may_chu/UngDungMayChu.java`.  
5. Chạy **Client**: `khach_hang/UngDungKhach.java` (mở 2 client để test).  
6. Tài khoản lưu tại `db/taikhoan.txt`, lịch sử lưu tại `db/lichsu.csv`.  

📦 LTM (thư mục gốc của project)

khach_hang/ (code phía client)

UngDungKhach.java → chạy client (main)

GiaoDienTroChoi.java → giao diện Swing

KetNoiKhach.java → xử lý kết nối TCP

TienIch.java → tiện ích (hash mật khẩu, hỗ trợ)

may_chu/ (code phía server)

UngDungMayChu.java → chạy server (main)

XuLyKhach.java → xử lý kết nối từng client

QuanLyTroChoi.java → quản lý logic ghép phòng & trận

XuLyLuotChoi.java → xử lý luật oẳn tù tì

QuanLyTaiKhoan.java → quản lý tài khoản (đăng ký, lưu file)

QuanLyLichSu.java → quản lý lịch sử và bảng xếp hạng

db/ (thư mục lưu trữ dữ liệu)

taikhoan.txt → lưu tài khoản (username:hash)

lichsu.csv → lưu lịch sử trận đấu

assets/ (ảnh giao diện để chèn README)

login.png

game.png

history.png

leaderboard.png

README.md (file mô tả project)
🏆 6. Các tính năng chính
- 🔒 Đăng ký / Đăng nhập (hash mật khẩu SHA-256).  
- 👫 Ghép cặp tự động giữa 2 người chơi.  
- ✊✋✌ Chơi Oẳn Tù Tì với kết quả trực quan (emoji + màu sắc).  
- 📜 Lưu lịch sử đầy đủ vào file CSV.  
- 🏆 Bảng xếp hạng từ lịch sử thực tế.  
- 🚪 Hỗ trợ thoát phòng.  

---

✍️ Sinh viên thực hiện: Nguyễn Việt Ninh – CNTT 16-04  
📅 Môn học: Lập trình mạng (LTM) 