package com.bluemoon.models;

import java.sql.Timestamp;

public class ThanhToanModel {
    private int id;
    private String maHoKhau;
    private float soTienThanhToan;
    private Timestamp ngayThanhToan; // Dùng Timestamp cho DATETIME
    private String noiDung;

    public ThanhToanModel() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public float getSoTienThanhToan() { return soTienThanhToan; }
    public void setSoTienThanhToan(float soTienThanhToan) { this.soTienThanhToan = soTienThanhToan; }

    public Timestamp getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(Timestamp ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
}