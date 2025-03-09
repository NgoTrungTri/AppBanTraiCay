package com.btlon.bantraicay.models;

public class DanhGia {
    private String maDanhGia;
    private int soSao;
    private String maSanPham; // Lưu mã sản phẩm thay vì đối tượng SanPham
    private String maKhachHang; // Lưu mã khách hàng thay vì đối tượng KhachHang

    // Constructor không đối số bắt buộc cho Firestore
    public DanhGia() {}

    // Constructor đầy đủ
    public DanhGia(String maDanhGia, int soSao, String maSanPham, String maKhachHang) {
        this.maDanhGia = maDanhGia;
        this.soSao = soSao;
        this.maSanPham = maSanPham;
        this.maKhachHang = maKhachHang;
    }

    // Getters và Setters
    public String getMaDanhGia() {
        return maDanhGia;
    }

    public void setMaDanhGia(String maDanhGia) {
        this.maDanhGia = maDanhGia;
    }

    public int getSoSao() {
        return soSao;
    }

    public void setSoSao(int soSao) {
        if (soSao < 1 || soSao > 5) {
            throw new IllegalArgumentException("Số sao phải nằm trong khoảng từ 1 đến 5.");
        }
        this.soSao = soSao;
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
