package com.bluemoon.models;

import java.sql.Date;

public class TamVangModel {
    private String maTamVang;
    private String soCMND_CCCD;
    private String noiTamTru;
    private Date tuNgay;
    private Date denNgay;

    public TamVangModel() {}

    public TamVangModel(String maTamVang, String soCMND_CCCD, String noiTamTru, Date tuNgay, Date denNgay) {
        this.maTamVang = maTamVang;
        this.soCMND_CCCD = soCMND_CCCD;
        this.noiTamTru = noiTamTru;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
    }

    // Getters and Setters
    public String getMaTamVang() { return maTamVang; }
    public void setMaTamVang(String maTamVang) { this.maTamVang = maTamVang; }

    public String getSoCMND_CCCD() { return soCMND_CCCD; }
    public void setSoCMND_CCCD(String soCMND_CCCD) { this.soCMND_CCCD = soCMND_CCCD; }

    public String getNoiTamTru() { return noiTamTru; }
    public void setNoiTamTru(String noiTamTru) { this.noiTamTru = noiTamTru; }

    public Date getTuNgay() { return tuNgay; }
    public void setTuNgay(Date tuNgay) { this.tuNgay = tuNgay; }

    public Date getDenNgay() { return denNgay; }
    public void setDenNgay(Date denNgay) { this.denNgay = denNgay; }
}