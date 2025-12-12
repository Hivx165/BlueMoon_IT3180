package com.bluemoon.models;

public class UserModel {
    private String userName;
    private String password;
    private String hoTen;
    private String email;
    private String soDT;
    private String diaChi;
    private int tuoi;

    public UserModel() {}

    public UserModel(String userName, String password, String hoTen, String email, String soDT, String diaChi, int tuoi) {
        this.userName = userName;
        this.password = password;
        this.hoTen = hoTen;
        this.email = email;
        this.soDT = soDT;
        this.diaChi = diaChi;
        this.tuoi = tuoi;
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public int getTuoi() { return tuoi; }
    public void setTuoi(int tuoi) { this.tuoi = tuoi; }
}