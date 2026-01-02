-- =============================================
-- PHẦN 1: KHỞI TẠO DATABASE
-- =============================================
USE master;
GO

-- Nếu Database đã tồn tại thì xóa đi để tạo lại cho sạch
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'BlueMoonDB')
BEGIN
    ALTER DATABASE BlueMoonDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE BlueMoonDB;
END
GO

-- Tạo Database mới
CREATE DATABASE BlueMoonDB;
GO

USE BlueMoonDB;
GO

-- =============================================
-- PHẦN 2: TẠO CÁC BẢNG (TABLES)
-- =============================================

-- 1. Bảng Tài khoản đăng nhập
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin' -- Mở rộng sau này nếu cần phân quyền
);

-- 2. Bảng Hộ Khẩu (Cốt lõi)
CREATE TABLE hokhau (
    MaHoKhau VARCHAR(20) PRIMARY KEY,
    DiaChi NVARCHAR(100) NOT NULL,
    NgayLap DATE DEFAULT GETDATE(),
    DienTichHo FLOAT DEFAULT 0 -- Quan trọng để tính phí dịch vụ
);

-- 3. Bảng Nhân Khẩu (Cốt lõi)
CREATE TABLE nhankhau (
    SoCMND_CCCD VARCHAR(20) PRIMARY KEY,
    MaHoKhau VARCHAR(20),
    HoTen NVARCHAR(50) NOT NULL,
    NgaySinh DATE NOT NULL,
    GioiTinh NVARCHAR(10),
    QueQuan NVARCHAR(100),
    NgheNghiep NVARCHAR(50),
    SoDT VARCHAR(15), -- Số điện thoại
    QuanHe NVARCHAR(30), -- Quan hệ với chủ hộ (Vợ, Con, Chủ hộ...)
    TamTru INT DEFAULT 0, -- 1: Là dân tạm trú
    TamVang INT DEFAULT 0, -- 1: Đang tạm vắng
    GhiChu NVARCHAR(200),
    
    -- Liên kết khóa ngoại với bảng Hộ Khẩu
    FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE SET NULL
);

-- 4. Bảng Phí Dịch Vụ (Thu theo diện tích, cố định hàng tháng/năm)
CREATE TABLE phidichvu (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    MaHoKhau VARCHAR(20),
    GiaPhi FLOAT, -- Đơn giá trên 1m2
    TienNopMoiThang FLOAT, -- = Diện tích * Giá phí
    Nam INT,
    
    FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
);

-- 5. Bảng Phí Quản Lý (Phí vận hành, bảo trì...)
CREATE TABLE phiquanly (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    MaHoKhau VARCHAR(20),
    GiaPhi FLOAT,
    TienNopMoiThang FLOAT,
    Nam INT,
    
    FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
);

-- 6. Bảng Đóng Góp (Tự nguyện: Vì người nghèo, bão lụt...)
CREATE TABLE donggop (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    MaHoKhau VARCHAR(20),
    TenKhoanDongGop NVARCHAR(100),
    SoTien FLOAT,
    NgayDong DATE DEFAULT GETDATE(),
    
    FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
);

-- 7. Bảng Phí Gửi Xe (Mới thêm)
CREATE TABLE phiguixe (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    MaHoKhau VARCHAR(20),
    LoaiXe NVARCHAR(50), -- Xe máy, Ô tô, Xe đạp điện
    BienSo VARCHAR(20),
    PhiThang FLOAT,
    NgayDangKy DATE DEFAULT GETDATE(),
    
    FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
);

-- 8. Bảng Phí Sinh Hoạt (Điện, Nước...) (Mới thêm)
CREATE TABLE phisinhhoat (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    MaHoKhau VARCHAR(20),
    Thang INT,
    Nam INT,
    TienDien FLOAT DEFAULT 0,
    TienNuoc FLOAT DEFAULT 0,
    TongTien FLOAT, -- Sẽ tính bằng code hoặc Trigger
    DaDong INT DEFAULT 0, -- 0: Chưa đóng, 1: Đã đóng
    
    FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
);

-- =============================================
-- PHẦN 3: THÊM DỮ LIỆU MẪU (SEED DATA)
-- =============================================

-- 1. Admin
INSERT INTO users (username, password) VALUES ('admin', '123');

-- 2. Hộ Khẩu (3 Hộ điển hình)
INSERT INTO hokhau (MaHoKhau, DiaChi, NgayLap, DienTichHo) VALUES 
('HK01', N'Phòng 101 - Tòa A', '2023-01-10', 80.5),
('HK02', N'Phòng 205 - Tòa B', '2023-02-15', 100.0),
('HK03', N'Phòng 505 - Tòa C', '2023-03-20', 65.0);

-- 3. Nhân Khẩu (Gia đình HK01: 2 người, HK02: 3 người, HK03: 1 người)
INSERT INTO nhankhau (SoCMND_CCCD, MaHoKhau, HoTen, NgaySinh, GioiTinh, QueQuan, NgheNghiep, QuanHe) VALUES 
-- Hộ 1
('001099123456', 'HK01', N'Nguyễn Văn An', '1980-01-01', N'Nam', N'Hà Nội', N'Kỹ sư', N'Chủ hộ'),
('001099654321', 'HK01', N'Trần Thị Bích', '1982-05-10', N'Nữ', N'Nam Định', N'Giáo viên', N'Vợ'),
-- Hộ 2
('036098000111', 'HK02', N'Lê Văn Cường', '1975-08-20', N'Nam', N'Nghệ An', N'Bác sĩ', N'Chủ hộ'),
('036098000222', 'HK02', N'Phạm Thị Duyên', '1978-11-11', N'Nữ', N'Hà Tĩnh', N'Kế toán', N'Vợ'),
('000000000001', 'HK02', N'Lê Văn Em', '2005-06-01', N'Nam', N'Nghệ An', N'Học sinh', N'Con'),
-- Hộ 3
('090909090909', 'HK03', N'Hoàng Thị F', '1995-12-25', N'Nữ', N'Sài Gòn', N'Freelancer', N'Chủ hộ');

-- 4. Dữ liệu Phí Dịch Vụ (Giả sử năm 2025)
-- Giá chung cư giả định: 5.000 VNĐ / m2
INSERT INTO phidichvu (MaHoKhau, GiaPhi, TienNopMoiThang, Nam) VALUES 
('HK01', 5000, 402500, 2025), -- 80.5 * 5000
('HK02', 5000, 500000, 2025), -- 100 * 5000
('HK03', 5000, 325000, 2025); -- 65 * 5000

-- 5. Dữ liệu Phí Quản Lý
-- Giá giả định: 7.000 VNĐ / m2
INSERT INTO phiquanly (MaHoKhau, GiaPhi, TienNopMoiThang, Nam) VALUES 
('HK01', 7000, 563500, 2025),
('HK02', 7000, 700000, 2025);

-- 6. Dữ liệu Đóng Góp
INSERT INTO donggop (MaHoKhau, TenKhoanDongGop, SoTien, NgayDong) VALUES 
('HK01', N'Ủng hộ đồng bào lũ lụt', 200000, '2025-10-15'),
('HK02', N'Quỹ khuyến học', 500000, '2025-09-05'),
('HK01', N'Tết thiếu nhi 1/6', 100000, '2025-06-01');

-- 7. Dữ liệu Phí Gửi Xe
INSERT INTO phiguixe (MaHoKhau, LoaiXe, BienSo, PhiThang, NgayDangKy) VALUES 
('HK01', N'Xe máy Honda Vision', '29E1-123.45', 70000, '2024-01-01'),
('HK02', N'Ô tô Mazda 3', '30H-999.99', 1200000, '2024-02-01'),
('HK03', N'Xe đạp điện', 'Không có', 30000, '2024-03-01');

-- 8. Dữ liệu Phí Sinh Hoạt (Điện/Nước)
INSERT INTO phisinhhoat (MaHoKhau, Thang, Nam, TienDien, TienNuoc, TongTien) VALUES 
('HK01', 11, 2025, 450000, 80000, 530000),
('HK02', 11, 2025, 1200000, 150000, 1350000),
('HK03', 11, 2025, 300000, 50000, 350000);

GO

USE BlueMoonDB;
GO

-- Tạo bảng Đóng góp (Nếu chưa có)
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[donggop]') AND type in (N'U'))
BEGIN
    CREATE TABLE donggop (
        ID INT IDENTITY(1,1) PRIMARY KEY,
        MaHoKhau VARCHAR(20),
        TenKhoanDongGop NVARCHAR(100),
        SoTien FLOAT,
        NgayDong DATE DEFAULT GETDATE(),
        FOREIGN KEY (MaHoKhau) REFERENCES hokhau(MaHoKhau) ON DELETE CASCADE
    );
    
    -- Thêm dữ liệu mẫu để test
    INSERT INTO donggop (MaHoKhau, TenKhoanDongGop, SoTien, NgayDong) VALUES 
    ('HK01', N'Ủng hộ đồng bào lũ lụt', 200000, '2025-10-15'),
    ('HK02', N'Quỹ khuyến học', 500000, '2025-09-05');
END
GO