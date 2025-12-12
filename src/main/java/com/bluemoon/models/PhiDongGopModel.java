package com.bluemoon.models;

import java.sql.Date;

public class PhiDongGopModel {
    private int id;
    private String maHoKhau;
    private String tenPhi;
    private float soTien;
    private Date ngayDongGop;

    public PhiDongGopModel() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public String getTenPhi() { return tenPhi; }
    public void setTenPhi(String tenPhi) { this.tenPhi = tenPhi; }

    public float getSoTien() { return soTien; }
    public void setSoTien(float soTien) { this.soTien = soTien; }

    public Date getNgayDongGop() { return ngayDongGop; }
    public void setNgayDongGop(Date ngayDongGop) { this.ngayDongGop = ngayDongGop; }
}