package com.bluemoon.models;

import java.time.LocalDate;

public class NhanKhauModel {
    private String soCMND_CCCD;
    private String maHoKhau;
    private String hoTen;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String queQuan;
    private String ngheNghiep;
    private String soDT;
    private String quanHe;
    private int tamTru;  // 1: Có, 0: Không
    private int tamVang; // 1: Có, 0: Không
    private String ghiChu;

    // Constructor
    public NhanKhauModel(String soCMND_CCCD, String maHoKhau, String hoTen, LocalDate ngaySinh, String gioiTinh,
                         String queQuan, String ngheNghiep, String soDT, String quanHe, int tamTru, int tamVang, String ghiChu) {
        this.soCMND_CCCD = soCMND_CCCD;
        this.maHoKhau = maHoKhau;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.queQuan = queQuan;
        this.ngheNghiep = ngheNghiep;
        this.soDT = soDT;
        this.quanHe = quanHe;
        this.tamTru = tamTru;
        this.tamVang = tamVang;
        this.ghiChu = ghiChu;
    }

    public NhanKhauModel() {}

    // Getters and Setters
    public String getSoCMND_CCCD() { return soCMND_CCCD; }
    public void setSoCMND_CCCD(String soCMND_CCCD) { this.soCMND_CCCD = soCMND_CCCD; }

    public String getMaHoKhau() { return maHoKhau; }
    public void setMaHoKhau(String maHoKhau) { this.maHoKhau = maHoKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }

    public String getNgheNghiep() { return ngheNghiep; }
    public void setNgheNghiep(String ngheNghiep) { this.ngheNghiep = ngheNghiep; }

    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }

    public String getQuanHe() { return quanHe; }
    public void setQuanHe(String quanHe) { this.quanHe = quanHe; }

    public int getTamTru() { return tamTru; }
    public void setTamTru(int tamTru) { this.tamTru = tamTru; }

    public int getTamVang() { return tamVang; }
    public void setTamVang(int tamVang) { this.tamVang = tamVang; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}