package com.btlon.bantraicay.models;

public class SanPham {
    private String maSanPham;
    private String tenSP;
    private double dinhLuong; // Đơn vị định lượng sản phẩm, ví dụ: kg, g, lít
    private double gia;
    private String hinhAnh; // URL hoặc đường dẫn hình ảnh
    private String moTa;
    private String maLoaiTraiCay; // Chỉ lưu mã loại thay vì toàn bộ đối tượng

    // Constructor không đối số bắt buộc cho Firestore
    public SanPham() {}

    // Constructor đầy đủ
    public SanPham(String maSanPham, String tenSP, double dinhLuong, double gia, String hinhAnh, String moTa, String maLoaiTraiCay) {
        this.maSanPham = maSanPham;
        this.tenSP = tenSP;
        this.dinhLuong = dinhLuong;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.maLoaiTraiCay = maLoaiTraiCay; // Lưu mã loại
    }

    // Getter và Setter
    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public double getDinhLuong() {
        return dinhLuong;
    }

    public void setDinhLuong(double dinhLuong) {
        this.dinhLuong = dinhLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getMaLoaiTraiCay() {
        return maLoaiTraiCay; // Trả về mã loại thay vì đối tượng
    }

    public void setMaLoaiTraiCay(String maLoaiTraiCay) {
        this.maLoaiTraiCay = maLoaiTraiCay; // Lưu mã loại thay vì đối tượng
    }
}
