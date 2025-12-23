package com.bluemoon.models;

public class PhiCoDinhModel {
    private int id;
    private String maHoKhau;
    private float giaPhi; // Đơn giá
    private float tienNopMoiThang;
    private int nam;
    // Các tháng
    private float thang1, thang2, thang3, thang4, thang5, thang6;
    private float thang7, thang8, thang9, thang10, thang11, thang12;

    public PhiCoDinhModel() {}

    // Getters and Setters cho các trường chính
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public float getGiaPhi() { return giaPhi; }
    public void setGiaPhi(float giaPhi) { this.giaPhi = giaPhi; }

    public float getTienNopMoiThang() { return tienNopMoiThang; }
    public void setTienNopMoiThang(float tienNopMoiThang) { this.tienNopMoiThang = tienNopMoiThang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    // Bạn cần generate thêm Getter/Setter cho thang1 -> thang12
    public float getThang1() { return thang1; }
    public void setThang1(float thang1) { this.thang1 = thang1; }
    // ... làm tương tự cho đến thang 12
}