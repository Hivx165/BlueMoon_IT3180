-- 1. Tạo Database
-- Kiểm tra nếu chưa có thì tạo mới
IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'bluemoon_db')
BEGIN
    CREATE DATABASE bluemoon_db;
END
GO

USE bluemoon_db;
GO

-- ==========================================
-- NHÓM 1: QUẢN LÝ NGƯỜI DÙNG VÀ DÂN CƯ
-- ==========================================

-- 2. Bảng User (Quản trị viên)
-- Dùng NVARCHAR để lưu tiếng Việt có dấu
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[users]') AND type in (N'U'))
BEGIN
    CREATE TABLE users (
        UserName VARCHAR(30) NOT NULL,
        Password VARCHAR(30) NOT NULL,
        HoTen NVARCHAR(100),
        Email VARCHAR(100),
        SoDT VARCHAR(15),
        DiaChi NVARCHAR(255),
        Tuoi INT,
        PRIMARY KEY (UserName)
    );
END
GO

-- 3. Bảng Hộ Khẩu
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[hokhau]') AND type in (N'U'))
BEGIN
    CREATE TABLE hokhau (
        MaHoKhau VARCHAR(10) NOT NULL,
        DiaChi NVARCHAR(100),
        NgayLap DATE,
        NgayChuyenDi DATE,
        LyDoChuyen NVARCHAR(100),
        DienTichHo FLOAT,
        SoXeMay INT DEFAULT 0,
        SoOTo INT DEFAULT 0,
        SoXeDap INT DEFAULT 0,
        PRIMARY KEY (MaHoKhau)
    );
END
GO

-- 4. Bảng Nhân Khẩu
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[nhankhau]') AND type in (N'U'))
BEGIN
    CREATE TABLE nhankhau (
        SoCMND_CCCD VARCHAR(15) NOT NULL,
        MaHoKhau VARCHAR(10) NOT NULL,
        HoTen NVARCHAR(100),
        Tuoi INT,
        GioiTinh NVARCHAR(10),
        SoDT VARCHAR(15),
        QuanHe NVARCHAR(30),
        TamTru INT DEFAULT 0,
        TamVang INT DEFAULT 0,
        PRIMARY KEY (SoCMND_CCCD),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
    );
END
GO

-- 5. Bảng Tạm Trú
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[tamtru]') AND type in (N'U'))
BEGIN
    CREATE TABLE tamtru (
        MaTamTru VARCHAR(10) NOT NULL,
        SoCMND_CCCD VARCHAR(15) NOT NULL,
        LyDo NVARCHAR(100),
        TuNgay DATE,
        DenNgay DATE,
        PRIMARY KEY (MaTamTru),
        FOREIGN KEY (SoCMND_CCCD) REFERENCES nhankhau(SoCMND_CCCD) ON DELETE CASCADE
    );
END
GO

-- 6. Bảng Tạm Vắng
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[tamvang]') AND type in (N'U'))
BEGIN
    CREATE TABLE tamvang (
        MaTamVang VARCHAR(10) NOT NULL,
        SoCMND_CCCD VARCHAR(15) NOT NULL,
        NoiTamTru NVARCHAR(100),
        TuNgay DATE,
        DenNgay DATE,
        PRIMARY KEY (MaTamVang),
        FOREIGN KEY (SoCMND_CCCD) REFERENCES nhankhau(SoCMND_CCCD) ON DELETE CASCADE
    );
END
GO

-- ==========================================
-- NHÓM 2: QUẢN LÝ CÁC KHOẢN PHÍ
-- ==========================================

-- 7. Bảng Danh sách các khoản phí đóng góp
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[danhsachphidonggop]') AND type in (N'U'))
BEGIN
    CREATE TABLE danhsachphidonggop (
        TenPhi NVARCHAR(50) NOT NULL,
        SoTienGoiY FLOAT,
        PRIMARY KEY (TenPhi)
    );
END
GO

-- 8. Bảng Phí Đóng Góp
-- Sử dụng IDENTITY(1,1) thay cho AUTO_INCREMENT
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[phidonggop]') AND type in (N'U'))
BEGIN
    CREATE TABLE phidonggop (
        ID INT IDENTITY(1,1) NOT NULL,
        MaHoKhau VARCHAR(10) NOT NULL,
        TenPhi NVARCHAR(50) NOT NULL,
        SoTien FLOAT,
        NgayDongGop DATE,
        PRIMARY KEY (ID),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE,
        FOREIGN KEY (TenPhi) REFERENCES danhsachphidonggop(TenPhi) ON DELETE CASCADE
    );
END
GO

-- 9. Bảng Phí Dịch Vụ
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[phidichvu]') AND type in (N'U'))
BEGIN
    CREATE TABLE phidichvu (
        ID INT IDENTITY(1,1) NOT NULL,
        MaHoKhau VARCHAR(10) NOT NULL,
        GiaPhi FLOAT,
        TienNopMoiThang FLOAT,
        Nam INT NOT NULL,
        Thang1 FLOAT DEFAULT 0, Thang2 FLOAT DEFAULT 0, Thang3 FLOAT DEFAULT 0,
        Thang4 FLOAT DEFAULT 0, Thang5 FLOAT DEFAULT 0, Thang6 FLOAT DEFAULT 0,
        Thang7 FLOAT DEFAULT 0, Thang8 FLOAT DEFAULT 0, Thang9 FLOAT DEFAULT 0,
        Thang10 FLOAT DEFAULT 0, Thang11 FLOAT DEFAULT 0, Thang12 FLOAT DEFAULT 0,
        PRIMARY KEY (ID),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
    );
END
GO

-- 10. Bảng Phí Quản Lý
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[phiquanly]') AND type in (N'U'))
BEGIN
    CREATE TABLE phiquanly (
        ID INT IDENTITY(1,1) NOT NULL,
        MaHoKhau VARCHAR(10) NOT NULL,
        GiaPhi FLOAT,
        TienNopMoiThang FLOAT,
        Nam INT NOT NULL,
        Thang1 FLOAT DEFAULT 0, Thang2 FLOAT DEFAULT 0, Thang3 FLOAT DEFAULT 0,
        Thang4 FLOAT DEFAULT 0, Thang5 FLOAT DEFAULT 0, Thang6 FLOAT DEFAULT 0,
        Thang7 FLOAT DEFAULT 0, Thang8 FLOAT DEFAULT 0, Thang9 FLOAT DEFAULT 0,
        Thang10 FLOAT DEFAULT 0, Thang11 FLOAT DEFAULT 0, Thang12 FLOAT DEFAULT 0,
        PRIMARY KEY (ID),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
    );
END
GO

-- 11. Bảng Phí Gửi Xe
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[phiguixe]') AND type in (N'U'))
BEGIN
    CREATE TABLE phiguixe (
        ID INT IDENTITY(1,1) NOT NULL,
        MaHoKhau VARCHAR(10) NOT NULL,
        GiaXeMay FLOAT, GiaOTo FLOAT, GiaXeDap FLOAT,
        TienNopMoiThang FLOAT,
        Nam INT NOT NULL,
        Thang1 FLOAT DEFAULT 0, Thang2 FLOAT DEFAULT 0, Thang3 FLOAT DEFAULT 0,
        Thang4 FLOAT DEFAULT 0, Thang5 FLOAT DEFAULT 0, Thang6 FLOAT DEFAULT 0,
        Thang7 FLOAT DEFAULT 0, Thang8 FLOAT DEFAULT 0, Thang9 FLOAT DEFAULT 0,
        Thang10 FLOAT DEFAULT 0, Thang11 FLOAT DEFAULT 0, Thang12 FLOAT DEFAULT 0,
        PRIMARY KEY (ID),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
    );
END
GO

-- 12. Bảng Phí Sinh Hoạt
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[phisinhhoat]') AND type in (N'U'))
BEGIN
    CREATE TABLE phisinhhoat (
        ID INT IDENTITY(1,1) NOT NULL,
        MaHoKhau VARCHAR(10) NOT NULL,
        Nam INT NOT NULL,
        Thang1 FLOAT DEFAULT 0, Thang2 FLOAT DEFAULT 0, Thang3 FLOAT DEFAULT 0,
        Thang4 FLOAT DEFAULT 0, Thang5 FLOAT DEFAULT 0, Thang6 FLOAT DEFAULT 0,
        Thang7 FLOAT DEFAULT 0, Thang8 FLOAT DEFAULT 0, Thang9 FLOAT DEFAULT 0,
        Thang10 FLOAT DEFAULT 0, Thang11 FLOAT DEFAULT 0, Thang12 FLOAT DEFAULT 0,
        PRIMARY KEY (ID),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
    );
END
GO

-- 13. Bảng Cập Nhật Phí Sinh Hoạt
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[capnhatphisinhhoat]') AND type in (N'U'))
BEGIN
    CREATE TABLE capnhatphisinhhoat (
        ID INT IDENTITY(1,1) NOT NULL,
        MaHoKhau VARCHAR(10) NOT NULL,
        Thang INT NOT NULL,
        Nam INT NOT NULL,
        TienDien FLOAT,
        TienNuoc FLOAT,
        TienInternet FLOAT,
        NgayCapNhat DATE,
        PRIMARY KEY (ID),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
    );
END
GO

-- 14. Bảng Thanh Toán
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[thanhtoan]') AND type in (N'U'))
BEGIN
    CREATE TABLE thanhtoan (
        ID INT IDENTITY(1,1) NOT NULL,
        MaHoKhau VARCHAR(10) NOT NULL,
        SoTienThanhToan FLOAT,
        NgayThanhToan DATETIME,
        NoiDung NVARCHAR(255),
        PRIMARY KEY (ID),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
    );
END
GO

-- Chèn dữ liệu mẫu cho Admin
-- Kiểm tra tránh trùng lặp
IF NOT EXISTS (SELECT * FROM users WHERE UserName = 'admin')
BEGIN
    INSERT INTO users (UserName, Password, HoTen) VALUES ('admin', '1', N'Quản Trị Viên');
END
GO