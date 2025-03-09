package com.btlon.bantraicay.models;

public class KhuyenMai_SanPham {
    private String id;
    private String maKhuyenMai; // Mã khuyến mãi
    private String maSanPham;   // Mã sản phẩm

    // Constructor không đối số bắt buộc cho Firestore
    public KhuyenMai_SanPham() {}

    // Constructor đầy đủ
    public KhuyenMai_SanPham(String id, String maKhuyenMai, String maSanPham) {
        this.id = id;
        this.maKhuyenMai = maKhuyenMai;
        this.maSanPham = maSanPham;
    }

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }
}
