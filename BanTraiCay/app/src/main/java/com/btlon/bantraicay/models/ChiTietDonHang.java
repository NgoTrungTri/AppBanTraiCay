package com.btlon.bantraicay.models;

public class ChiTietDonHang {
    private String id;
    private double dinhLuong;
    private double tongTienSp;
    private String maDonHang; // Lưu mã đơn hàng thay vì đối tượng DonHang
    private String maSanPham; // Lưu mã sản phẩm thay vì đối tượng SanPham

    // Constructor không đối số bắt buộc cho Firestore
    public ChiTietDonHang() {}

    // Constructor đầy đủ
    public ChiTietDonHang(String id, double dinhLuong, double tongTienSp, String maDonHang, String maSanPham) {
        this.id = id;
        this.dinhLuong = dinhLuong;
        this.tongTienSp = tongTienSp;
        this.maDonHang = maDonHang;
        this.maSanPham = maSanPham;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDinhLuong() {
        return dinhLuong;
    }

    public void setDinhLuong(double dinhLuong) {
        this.dinhLuong = dinhLuong;
    }

    public double getTongTienSp() {
        return tongTienSp;
    }

    public void setTongTienSp(double tongTienSp) {
        this.tongTienSp = tongTienSp;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }
}
