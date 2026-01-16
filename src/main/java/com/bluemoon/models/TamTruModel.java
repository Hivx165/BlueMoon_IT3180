package com.bluemoon.models;

import java.time.LocalDate;

public class TamTruModel {
    private String soCCCD;
    private String hoTen;
    private String diaChiTamTru;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String ghiChu;
    private LocalDate NgaySinh;
    // Default constructor
    public TamTruModel() {}

    // Parameterized constructor
    public TamTruModel(String soCCCD, String hoTen, String diaChiTamTru, LocalDate tuNgay, LocalDate denNgay, String ghiChu) {
        this.soCCCD = soCCCD;
        this.hoTen = hoTen;
        this.diaChiTamTru = diaChiTamTru;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
        this.ghiChu = ghiChu;
    }

    // Getters and setters
    public String getSoCCCD() { return soCCCD; }
    public void setSoCCCD(String soCCCD) { this.soCCCD = soCCCD; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getDiaChiTamTru() { return diaChiTamTru; }
    public void setDiaChiTamTru(String diaChiTamTru) { this.diaChiTamTru = diaChiTamTru; }
    public LocalDate getTuNgay() { return tuNgay; }
    public void setTuNgay(LocalDate tuNgay) { this.tuNgay = tuNgay; }
    public LocalDate getDenNgay() { return denNgay; }
    public void setDenNgay(LocalDate denNgay) { this.denNgay = denNgay; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}