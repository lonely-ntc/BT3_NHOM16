package com.example.bt3_ptudd;

public class Tester extends NhanVien {
    String loaiKiemThu;

    public Tester(String maNV, String hoTen, String loaiKiemThu) {
        super(maNV, hoTen);
        this.loaiKiemThu = loaiKiemThu;
    }

    @Override
    public String toString() {
        return super.toString() + " | Tester (" + loaiKiemThu + ")";
    }
}
