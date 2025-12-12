package com.bluemoon.models;

public class NhanKhauModel {
    private String soCMND_CCCD;
    private String maHoKhau;
    private String hoTen;
    private int tuoi;
    private String gioiTinh;
    private String soDT;
    private String quanHe; // Quan hệ với chủ hộ
    private int isTamTru;  // 1: Có, 0: Không
    private int isTamVang; // 1: Có, 0: Không

    // Constructor
    public NhanKhauModel(String soCMND_CCCD, String maHoKhau, String hoTen, int tuoi, String gioiTinh, String soDT, String quanHe, int isTamTru, int isTamVang) {
        this.soCMND_CCCD = soCMND_CCCD;
        this.maHoKhau = maHoKhau;
        this.hoTen = hoTen;
        this.tuoi = tuoi;
        this.gioiTinh = gioiTinh;
        this.soDT = soDT;
        this.quanHe = quanHe;
        this.isTamTru = isTamTru;
        this.isTamVang = isTamVang;
    }

    public NhanKhauModel() {}

    // Getters and Setters
    public String getSoCMND_CCCD() { return soCMND_CCCD; }
    public void setSoCMND_CCCD(String soCMND_CCCD) { this.soCMND_CCCD = soCMND_CCCD; }

    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public int getTuoi() { return tuoi; }
    public void setTuoi(int tuoi) { this.tuoi = tuoi; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }

    public String getQuanHe() { return quanHe; }
    public void setQuanHe(String quanHe) { this.quanHe = quanHe; }

    public int getIsTamTru() { return isTamTru; }
    public void setIsTamTru(int isTamTru) { this.isTamTru = isTamTru; }

    public int getIsTamVang() { return isTamVang; }
    public void setIsTamVang(int isTamVang) { this.isTamVang = isTamVang; }
}