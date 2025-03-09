package com.btlon.bantraicay.models;

public class KhachHang {
    private String maKhachHang;
    private String ten;
    private String email;
    private String diaChi;
    private String soDienThoai;
    private String tenDangNhap;
    private String matKhau;
    private String avatarUrl;

    // Constructors
    public KhachHang() {
        // Default constructor required for calls to DataSnapshot.getValue(KhachHang.class)
    }

    public KhachHang(String maKhachHang, String ten, String email, String diaChi, String soDienThoai, String tenDangNhap, String matKhau, String avatarUrl) {
        this.maKhachHang = maKhachHang;
        this.ten = ten;
        this.email = email;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
