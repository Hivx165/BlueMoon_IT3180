package com.bluemoon.models;

import java.sql.Date;

public class TamTruModel {
    private String maTamTru;
    private String soCMND_CCCD;
    private String lyDo;
    private Date tuNgay;
    private Date denNgay;

    public TamTruModel() {}

    public TamTruModel(String maTamTru, String soCMND_CCCD, String lyDo, Date tuNgay, Date denNgay) {
        this.maTamTru = maTamTru;
        this.soCMND_CCCD = soCMND_CCCD;
        this.lyDo = lyDo;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
    }

    // Getters and Setters
    public String getMaTamTru() { return maTamTru; }
    public void setMaTamTru(String maTamTru) { this.maTamTru = maTamTru; }

    public String getSoCMND_CCCD() { return soCMND_CCCD; }
    public void setSoCMND_CCCD(String soCMND_CCCD) { this.soCMND_CCCD = soCMND_CCCD; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public Date getTuNgay() { return tuNgay; }
    public void setTuNgay(Date tuNgay) { this.tuNgay = tuNgay; }

    public Date getDenNgay() { return denNgay; }
    public void setDenNgay(Date denNgay) { this.denNgay = denNgay; }
}