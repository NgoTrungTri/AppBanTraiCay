package com.btlon.bantraicay.models;

import java.util.Date;

public class BinhLuan {
    private String id;
    private String noiDung;
    private Date thoiGian;
    private String maSanPham; // Lưu mã sản phẩm thay vì đối tượng SanPham
    private String maKhachHang; // Lưu mã khách hàng thay vì đối tượng KhachHang

    // Constructor không đối số bắt buộc cho Firestore
    public BinhLuan() {}

    // Constructor đầy đủ
    public BinhLuan(String id, String noiDung, Date thoiGian, String maSanPham, String maKhachHang) {
        this.id = id;
        this.noiDung = noiDung;
        this.thoiGian = thoiGian;
        this.maSanPham = maSanPham;
        this.maKhachHang = maKhachHang;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }
}
