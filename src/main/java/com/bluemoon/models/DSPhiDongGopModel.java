package com.bluemoon.models;

public class DSPhiDongGopModel {
    private String tenPhi;
    private float soTienGoiY;

    public DSPhiDongGopModel() {}

    public DSPhiDongGopModel(String tenPhi, float soTienGoiY) {
        this.tenPhi = tenPhi;
        this.soTienGoiY = soTienGoiY;
    }

    // Getters and Setters
    public String getTenPhi() { return tenPhi; }
    public void setTenPhi(String tenPhi) { this.tenPhi = tenPhi; }

    public float getSoTienGoiY() { return soTienGoiY; }
    public void setSoTienGoiY(float soTienGoiY) { this.soTienGoiY = soTienGoiY; }
}