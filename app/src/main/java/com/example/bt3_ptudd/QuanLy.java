package com.example.bt3_ptudd;

public class QuanLy extends NhanVien {
    int capBac;

    public QuanLy(String maNV, String hoTen, int capBac) {
        super(maNV, hoTen);
        this.capBac = capBac;
    }

    @Override
    public String toString() {
        return super.toString() + " | Quản lý (Cấp bậc: " + capBac + ")";
    }
}
