package com.bluemoon.models;

import java.sql.Date; // Sử dụng java.sql.Date để tương thích JDBC

public class HoKhauModel {
    private String maHoKhau;
    private String diaChi;
    private Date ngayLap;
    private Date ngayChuyenDi;
    private String lyDoChuyen;
    private float dienTichHo;
    private int soXeMay;
    private int soOTo;
    private int soXeDap;

    public HoKhauModel() {}

    // Getters and Setters
    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public Date getNgayLap() { return ngayLap; }
    public void setNgayLap(Date ngayLap) { this.ngayLap = ngayLap; }

    public Date getNgayChuyenDi() { return ngayChuyenDi; }
    public void setNgayChuyenDi(Date ngayChuyenDi) { this.ngayChuyenDi = ngayChuyenDi; }

    public String getLyDoChuyen() { return lyDoChuyen; }
    public void setLyDoChuyen(String lyDoChuyen) { this.lyDoChuyen = lyDoChuyen; }

    public float getDienTichHo() { return dienTichHo; }
    public void setDienTichHo(float dienTichHo) { this.dienTichHo = dienTichHo; }

    public int getSoXeMay() { return soXeMay; }
    public void setSoXeMay(int soXeMay) { this.soXeMay = soXeMay; }

    public int getSoOTo() { return soOTo; }
    public void setSoOTo(int soOTo) { this.soOTo = soOTo; }

    public int getSoXeDap() { return soXeDap; }
    public void setSoXeDap(int soXeDap) { this.soXeDap = soXeDap; }
}