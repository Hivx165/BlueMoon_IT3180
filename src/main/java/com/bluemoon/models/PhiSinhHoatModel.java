package com.bluemoon.models;

public class PhiSinhHoatModel {
    private int id;
    private String maHoKhau;
    private int thang;
    private int nam;
    private double tienDien;
    private double tienNuoc;
    private double tongTien;
    private int daDong; // 0: Chưa đóng, 1: Đã đóng

    public PhiSinhHoatModel() {}

    public PhiSinhHoatModel(int id, String maHoKhau, int thang, int nam, double tienDien, double tienNuoc, double tongTien, int daDong) {
        this.id = id;
        this.maHoKhau = maHoKhau;
        this.thang = thang;
        this.nam = nam;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tongTien = tongTien;
        this.daDong = daDong;
    }

    // Helper để hiển thị lên bảng (TableColumn)
    public String getTrangThaiHienThi() {
        return (daDong == 1) ? "Đã đóng" : "Chưa đóng";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getTienDien() { return tienDien; }
    public void setTienDien(double tienDien) { this.tienDien = tienDien; }

    public double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(double tienNuoc) { this.tienNuoc = tienNuoc; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public int getDaDong() { return daDong; }
    public void setDaDong(int daDong) { this.daDong = daDong; }
}