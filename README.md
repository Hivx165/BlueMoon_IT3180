# 🏢 BLUEMOON – HỆ THỐNG QUẢN LÝ CƯ DÂN CHUNG CƯ

## 📌 Thông tin chung

* **Tên dự án:** BlueMoon – Apartment Resident Management System
* **Môn học:** IT3180 – Phát triển phần mềm hướng đối tượng
* **Loại ứng dụng:** Desktop Application
* **Ngôn ngữ:** Java
* **Giao diện:** JavaFX (FXML)
* **Cơ sở dữ liệu:** SQL Server
* **IDE:** IntelliJ IDEA

---

## 🎯 Mục tiêu dự án

Dự án **BlueMoon** được xây dựng nhằm mô phỏng một hệ thống quản lý cư dân chung cư, giúp ban quản lý:

* Quản lý thông tin nhân khẩu, hộ khẩu một cách tập trung
* Theo dõi và quản lý các khoản phí của cư dân
* Thống kê và tổng hợp dữ liệu phục vụ công tác quản lý

## 🧱 Kiến trúc hệ thống

Ứng dụng được xây dựng theo mô hình **MVC (Model – View – Controller)**:

* **Model:** Đại diện cho dữ liệu và nghiệp vụ (Nhân khẩu, Hộ khẩu, Phí, …)
* **View:** Giao diện người dùng (FXML)
* **Controller:** Xử lý sự kiện, điều phối dữ liệu giữa View và Model

Mô hình MVC giúp chương trình:

* Dễ bảo trì
* Dễ mở rộng chức năng
* Phân tách rõ ràng trách nhiệm

---

## 📂 Cấu trúc thư mục

```
BlueMoon_IT3180/
├── database/
│   └── bluemoon_db_schema.sql
├── lib/
│   ├── mysql-connector-java.jar
│   ├── mssql-jdbc.jar
│   └── jfx-incubator-richtext.jar
├── src/
│   └── main/
│       ├── java/
│       │   └── bluemoon/
│       │       ├── controller/
│       │       ├── model/
│       │       ├── service/
│       │       └── Main.java
│       └── resources/
│           ├── css/
│           └── views/
├── README.md
└── .idea/
```

### Giải thích nhanh

* `controller/`: Xử lý logic và sự kiện giao diện
* `model/`: Các lớp biểu diễn dữ liệu
* `service/`: Kết nối và làm việc với cơ sở dữ liệu
* `views/`: Các file giao diện FXML
* `database/`: Script tạo và khởi tạo CSDL

---

## 🧩 Chức năng chính

### 👤 Quản lý nhân khẩu

* Thêm, xoá thông tin nhân khẩu
* Hiển thị và tìm kiếm danh sách nhân khẩu

### 🏠 Quản lý hộ khẩu

* Quản lý thông tin hộ gia đình
* Gán nhân khẩu vào hộ khẩu tương ứng

### 💰 Quản lý các loại phí

* Phí quản lý
* Phí dịch vụ
* Phí đóng góp
* Theo dõi tình trạng đóng phí

### 📊 Thống kê

* Thống kê số lượng nhân khẩu
* Thống kê tổng các khoản phí

---

## ⚙️ Hướng dẫn cài đặt và chạy chương trình

### 1️⃣ Yêu cầu hệ thống

* JDK 11 hoặc cao hơn
* IntelliJ IDEA
* MySQL hoặc SQL Server

### 2️⃣ Cấu hình cơ sở dữ liệu

* Chạy file SQL trong thư mục `database/` để tạo CSDL
* Cập nhật thông tin kết nối trong `DatabaseService.java`

```java
String url = "jdbc:mysql://localhost:3306/bluemoon";
String user = "root";
String password = "password";
```

### 3️⃣ Chạy chương trình

* Mở project bằng IntelliJ IDEA
* Thêm các thư viện `.jar` trong thư mục `lib/` vào Project Structure
* Chạy file `App.java`

---

## 🔮 Hướng phát triển

* Phân quyền người dùng (Admin / Nhân viên)
* Xuất báo cáo PDF, Excel
* Biểu đồ thống kê trực quan
* Cải thiện giao diện người dùng

---

## 👨‍💻 Nhóm thực hiện

* Sinh viên thực hiện đồ án môn **IT3180**
* Mục đích: Học tập và nghiên cứu

---

## 📄 Bản quyền

Dự án được sử dụng cho **mục đích học tập**, không dùng cho thương mại.
