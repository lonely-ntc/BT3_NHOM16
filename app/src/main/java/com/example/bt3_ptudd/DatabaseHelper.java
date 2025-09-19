package com.example.bt3_ptudd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "nhanvien.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "NhanVien";

    public interface OnDataChangedListener {
        void onDataChanged();
    }

    private OnDataChangedListener listener;

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.listener = listener;
    }

    private void notifyChange() {
        if (listener != null) listener.onDataChanged();
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "maNV TEXT PRIMARY KEY, " +
                "hoTen TEXT, " +
                "chucVu TEXT, " +
                "capBac INTEGER, " +
                "loaiKiemThu TEXT, " +
                "namKN INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Thêm nhân viên (ghi đè ngay lập tức)
    public void themNhanVien(NhanVien nv) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = taoContentValues(nv);
            values.put("maNV", nv.maNV);
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        notifyChange();
    }

    // Sửa nhân viên
    public void suaNhanVien(NhanVien nv) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = taoContentValues(nv);
            db.update(TABLE_NAME, values, "maNV=?", new String[]{nv.maNV});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        notifyChange();
    }

    // Xóa nhân viên
    public void xoaNhanVien(String maNV) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, "maNV=?", new String[]{maNV});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        notifyChange();
    }

    // Lấy toàn bộ nhân viên
    public ArrayList<NhanVien> getTatCaNhanVien() {
        ArrayList<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY maNV", null);

        if (cursor.moveToFirst()) {
            do {
                String ma = cursor.getString(cursor.getColumnIndexOrThrow("maNV"));
                String ten = cursor.getString(cursor.getColumnIndexOrThrow("hoTen"));
                String chucVu = cursor.getString(cursor.getColumnIndexOrThrow("chucVu"));

                if ("Quản lý".equals(chucVu)) {
                    int capbac = cursor.getInt(cursor.getColumnIndexOrThrow("capBac"));
                    list.add(new QuanLy(ma, ten, capbac));
                } else if ("Tester".equals(chucVu)) {
                    String loai = cursor.getString(cursor.getColumnIndexOrThrow("loaiKiemThu"));
                    list.add(new Tester(ma, ten, loai));
                } else if ("Lập trình viên".equals(chucVu)) {
                    int namKN = cursor.getInt(cursor.getColumnIndexOrThrow("namKN"));
                    list.add(new LapTrinhVien(ma, ten, namKN));
                } else {
                    list.add(new NhanVien(ma, ten) { });
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    private ContentValues taoContentValues(NhanVien nv) {
        ContentValues values = new ContentValues();
        values.put("hoTen", nv.hoTen);

        if (nv instanceof QuanLy) {
            values.put("chucVu", "Quản lý");
            values.put("capBac", ((QuanLy) nv).capBac);
            values.putNull("loaiKiemThu");
            values.putNull("namKN");
        } else if (nv instanceof Tester) {
            values.put("chucVu", "Tester");
            values.put("loaiKiemThu", ((Tester) nv).loaiKiemThu);
            values.putNull("capBac");
            values.putNull("namKN");
        } else if (nv instanceof LapTrinhVien) {
            values.put("chucVu", "Lập trình viên");
            values.put("namKN", ((LapTrinhVien) nv).namKN);
            values.putNull("capBac");
            values.putNull("loaiKiemThu");
        }
        return values;
    }
}
