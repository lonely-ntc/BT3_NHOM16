package com.example.bt3_ptudd;

public class NhanVien {
    String maNV;
    String hoTen;

    public NhanVien(String maNV, String hoTen) {
        this.maNV = maNV;
        this.hoTen = hoTen;
    }

    @Override
    public String toString() {
        return "Mã: " + maNV + " | Tên: " + hoTen;
    }
}
