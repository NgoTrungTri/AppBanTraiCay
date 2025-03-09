package com.btlon.bantraicay.models;

import java.util.Date;

public class DonHang {
    private String maDonHang;
    private String ngayTao;
    private double tongTien;
    private String trangThai;
    private String maKhachHang; // Lưu mã khách hàng thay vì đối tượng KhachHang

    // Constructor không đối số bắt buộc cho Firestore
    public DonHang() {}

    // Constructor đầy đủ
    public DonHang(String maDonHang, String ngayTao, double tongTien, String trangThai, String maKhachHang) {
        this.maDonHang = maDonHang;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.maKhachHang = maKhachHang;
    }

    // Getter và Setter
    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }
}
