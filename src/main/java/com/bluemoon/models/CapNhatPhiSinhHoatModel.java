package com.bluemoon.models;

import java.sql.Date;

public class CapNhatPhiSinhHoatModel {
    private int id;
    private String maHoKhau;
    private int thang;
    private int nam;
    private float tienDien;
    private float tienNuoc;
    private float tienInternet;
    private Date ngayCapNhat;

    public CapNhatPhiSinhHoatModel() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public float getTienDien() { return tienDien; }
    public void setTienDien(float tienDien) { this.tienDien = tienDien; }

    public float getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(float tienNuoc) { this.tienNuoc = tienNuoc; }

    public float getTienInternet() { return tienInternet; }
    public void setTienInternet(float tienInternet) { this.tienInternet = tienInternet; }

    public Date getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(Date ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }
}