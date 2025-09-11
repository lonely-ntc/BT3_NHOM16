package com.example.bt3_ptudd;

public class LapTrinhVien extends NhanVien {
    int namKN;

    public LapTrinhVien(String maNV, String hoTen, int namKN) {
        super(maNV, hoTen);
        this.namKN = namKN;
    }

    @Override
    public String toString() {
        return super.toString() + " | Lập trình viên (" + namKN + " năm KN)";
    }
}
