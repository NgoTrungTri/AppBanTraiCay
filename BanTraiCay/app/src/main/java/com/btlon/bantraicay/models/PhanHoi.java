package com.btlon.bantraicay.models;

import java.util.Date;

public class PhanHoi {
    private String id;
    private String noiDung;
    private Date thoiGian;
    private String maBinhLuan; // Mã bình luận

    // Constructor không đối số bắt buộc cho Firestore
    public PhanHoi() {}

    // Constructor đầy đủ
    public PhanHoi(String id, String noiDung, Date thoiGian, String maBinhLuan) {
        this.id = id;
        this.noiDung = noiDung;
        this.thoiGian = thoiGian;
        this.maBinhLuan = maBinhLuan;
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

    public String getMaBinhLuan() {
        return maBinhLuan;
    }

    public void setMaBinhLuan(String maBinhLuan) {
        this.maBinhLuan = maBinhLuan;
    }
}
