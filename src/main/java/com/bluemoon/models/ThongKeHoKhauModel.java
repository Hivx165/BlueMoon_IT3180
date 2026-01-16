package com.bluemoon.models;

public class ThongKeHoKhauModel {
    private String maHoKhau;
    private double phiSinhHoat; // Tổng tiền điện/nước đã đóng
    private double phiDichVu;   // Tổng tiền dịch vụ
    private double phiDongGop;  // Tổng tiền đóng góp
    private double tongCong;    // Tổng 3 loại trên

    public ThongKeHoKhauModel(String maHoKhau, double phiSinhHoat, double phiDichVu, double phiDongGop) {
        this.maHoKhau = maHoKhau;
        this.phiSinhHoat = phiSinhHoat;
        this.phiDichVu = phiDichVu;
        this.phiDongGop = phiDongGop;
        this.tongCong = phiSinhHoat + phiDichVu + phiDongGop;
    }

    // Getters
    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public double getPhiSinhHoat() { return phiSinhHoat; }
    public void setPhiSinhHoat(double phiSinhHoat) { this.phiSinhHoat = phiSinhHoat; }

    public double getPhiDichVu() { return phiDichVu; }
    public void setPhiDichVu(double phiDichVu) { this.phiDichVu = phiDichVu; }

    public double getPhiDongGop() { return phiDongGop; }
    public void setPhiDongGop(double phiDongGop) { this.phiDongGop = phiDongGop; }

    public double getTongCong() { return tongCong; }
    public void setTongCong(double tongCong) { this.tongCong = tongCong; }
}