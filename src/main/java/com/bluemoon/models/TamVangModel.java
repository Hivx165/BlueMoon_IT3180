package com.bluemoon.models;

import java.time.LocalDate;

public class TamVangModel {
    private String soCCCD;
    private String hoTen;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String lyDo;
    private LocalDate NgaySinh;
    // No-args constructor
    public TamVangModel() {
        // Default constructor
    }

    // Full-args constructor
    public TamVangModel(String soCCCD, String hoTen, LocalDate tuNgay, LocalDate denNgay, String lyDo) {
        this.soCCCD = soCCCD;
        this.hoTen = hoTen;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
        this.lyDo = lyDo;
    }

    // Getters and setters
    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDate getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(LocalDate tuNgay) {
        this.tuNgay = tuNgay;
    }

    public LocalDate getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(LocalDate denNgay) {
        this.denNgay = denNgay;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }
}