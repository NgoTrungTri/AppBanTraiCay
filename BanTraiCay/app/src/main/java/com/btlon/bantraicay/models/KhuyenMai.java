package com.btlon.bantraicay.models;

import java.util.Date;

public class KhuyenMai {
    private String maKhuyenMai;
    private String ten;
    private double giaTri; // Giá trị từ 0 đến 1
    private Date ngayBatDau;
    private Date ngayKetThuc;

    // Constructor không đối số bắt buộc cho Firestore
    public KhuyenMai() {}

    // Constructor đầy đủ
    public KhuyenMai(String maKhuyenMai, String ten, double giaTri, Date ngayBatDau, Date ngayKetThuc) {
        this.maKhuyenMai = maKhuyenMai;
        this.ten = ten;
        setGiaTri(giaTri); // Sử dụng setter để đảm bảo giá trị trong khoảng từ 0 đến 1
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    // Getters và Setters
    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public double getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(double giaTri) {
        if (giaTri < 0 || giaTri > 1) {
            throw new IllegalArgumentException("Giá trị giảm giá phải nằm trong khoảng từ 0 đến 1.");
        }
        this.giaTri = giaTri;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
