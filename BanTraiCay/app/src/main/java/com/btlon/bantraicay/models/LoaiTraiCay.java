package com.btlon.bantraicay.models;

public class LoaiTraiCay {
    private String maLoai;
    private String tenLoai;

    // Constructor không đối số bắt buộc cho Firestore
    public LoaiTraiCay() {}

    // Constructor đầy đủ
    public LoaiTraiCay(String maLoai, String tenLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }

    // Getter và Setter
    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    @Override
    public String toString() {
        return tenLoai; // Trả về tên loại trái cây để hiển thị trong Spinner
    }
}
