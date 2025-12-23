package com.bluemoon.models;

public class PhiSinhHoatModel {
    private int id;
    private String maHoKhau;
    private int nam;
    // Lưu số tiền tổng hợp của từng tháng
    private float thang1, thang2, thang3, thang4, thang5, thang6;
    private float thang7, thang8, thang9, thang10, thang11, thang12;

    public PhiSinhHoatModel() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public float getThang1() { return thang1; }
    public void setThang1(float thang1) { this.thang1 = thang1; }
    // ... Bạn hãy generate tiếp getter/setter cho thang2 đến thang12 tương tự
    public float getThang12() { return thang12; }
    public void setThang12(float thang12) { this.thang12 = thang12; }
}