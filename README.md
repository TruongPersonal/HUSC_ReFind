# 🎓 HUSC ReFind

HUSC ReFind là nền tảng web hiện đại hỗ trợ tìm kiếm, thông báo và quản lý trao trả đồ thất lạc dành riêng cho sinh viên và cán bộ Trường Đại học Khoa học - Đại học Huế một cách nhanh chóng, minh bạch và an toàn.

## ✨ Tính năng nổi bật

- **Trợ lý AI thông minh 24/7:** Tích hợp mô hình ngôn ngữ lớn thế hệ mới **Google Gemini (Gemini 3.7 Flash / Gemini 3.6 Flash)** theo kiến trúc RAG, hỗ trợ sinh viên tra cứu đồ thất lạc thời gian thực từ cơ sở dữ liệu, hướng dẫn quy trình nhận đồ tại Phòng Bảo vệ và cảnh báo phòng chống lừa đảo chuộc đồ.
- **Đăng tin báo mất đồ:** Sinh viên bị mất đồ dễ dàng đăng bài tìm kiếm kèm hình ảnh minh họa, mô tả chi tiết đặc điểm, chọn danh mục và khu vực thất lạc trong trường.
- **Tìm kiếm & Bộ lọc thông minh:** Tìm kiếm nhanh chóng theo từ khóa, lọc linh hoạt theo trạng thái (*Đang tìm, Đang giữ, Đã nhận*), danh mục đồ dùng (*Thẻ/Giấy tờ, Bóp/Ví, Điện tử...*) và khu vực (*Nhà A-K, Thư viện, Căng tin, Nhà xe...*).
- **Lưu tin & Theo dõi:** Đánh dấu lưu lại các bài đăng quan tâm để theo dõi tiến trình tìm kiếm hoặc nhận diện đồ đạc nhanh chóng.
- **Quản lý tiếp nhận & Phòng bảo vệ:** Quản trị viên tiếp nhận đồ nhặt được, phân loại vào tủ lưu trữ an toàn, gắn ghi chú vị trí và xác nhận trao trả chính xác cho chính chủ.
- **Bảo mật & Phân quyền chặt chẽ:** Đăng ký tài khoản qua email sinh viên HUSC (`@husc.edu.vn`), xác thực mã OTP qua email, mã hóa mật khẩu an toàn và phân quyền rõ ràng (*Sinh viên, Quản trị viên*).
- **Thống kê & Báo cáo trực quan:** Dashboard quản trị với biểu đồ tương tác (*Chart.js*) thống kê chi tiết tỷ lệ xử lý, mật độ đồ thất lạc theo từng khu vực và phân bố danh mục.
- **Giao diện hiện đại & Đáp ứng:** Thiết kế chỉn chu, chuyên nghiệp, tối ưu hiển thị mượt mà và tương thích tốt trên cả máy tính lẫn thiết bị di động.

## 🛠️ Công nghệ sử dụng

| Thành phần | Công nghệ |
| :--- | :--- |
| **Frontend** | HTML5, CSS3 (Custom Design System), JavaScript (ES6+), Bootstrap 5, Bootstrap Icons |
| **Biểu đồ & Trực quan hóa** | Chart.js |
| **Trí tuệ nhân tạo (AI)** | Google AI Studio (Gemini 3.7 Flash API), Dynamic Context RAG, Prompt Engineering |
| **Backend** | Java Servlet (Jakarta EE / Tomcat 10+), JSP, JSTL |
| **Kiến trúc** | MVC (Model - View - Controller), DAO Pattern |
| **Cơ sở dữ liệu** | MySQL (JDBC Driver) |
| **Bảo mật & Tiện ích** | SHA-256 Password Hashing, Session Management, OTP Verification (Email Service), CSRF Protection, XSS Filtering |
| **Cấu hình môi trường** | Universal Env Loader (`.env` & OS Environment Variables) |
| **Lưu trữ tệp** | Local / Server File Storage (`assets/uploads/items`) |

## 📄 Ghi chú

- Dự án này được phát triển phục vụ cho báo cáo đồ án môn học **Java nâng cao**.
- **Sinh viên:** Ngô Quang Trường (23t1020573) - Ths. Huỳnh Bảo Quốc Dũng lớp học phần 2025-2026.3.TIN4013.001
