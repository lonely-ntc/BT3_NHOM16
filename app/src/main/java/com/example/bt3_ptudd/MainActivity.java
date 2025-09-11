package com.example.bt3_ptudd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtMaNV, edtHoTen;
    Spinner spnChucVu;
    Button btnThem, btnDong;
    ListView lvNhanVien;

    ArrayList<NhanVien> dsNhanVien;
    ArrayAdapter<String> adapter;
    ArrayList<String> dsHienThi;
    int viTriDangChon = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtMaNV = findViewById(R.id.edtMaNV);
        edtHoTen = findViewById(R.id.edtHoTen);
        spnChucVu = findViewById(R.id.spnChucVu);
        btnThem = findViewById(R.id.btnThem);
        btnDong = findViewById(R.id.btnDong);
        lvNhanVien = findViewById(R.id.lvNhanVien);

        String[] chucvus = {"Quản lý", "Tester", "Lập trình viên"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, chucvus);
        spnChucVu.setAdapter(spnAdapter);

        dsNhanVien = new ArrayList<>();
        dsHienThi = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsHienThi);
        lvNhanVien.setAdapter(adapter);

        btnThem.setOnClickListener(v -> {
            String ma = edtMaNV.getText().toString().trim();
            String ten = edtHoTen.getText().toString().trim();
            String chucvu = spnChucVu.getSelectedItem().toString();

            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (chucvu.equals("Quản lý")) {
                Intent i = new Intent(MainActivity.this, ThemQuanLyActivity.class);
                i.putExtra("ma", ma);
                i.putExtra("ten", ten);
                startActivityForResult(i, 1);
            } else if (chucvu.equals("Tester")) {
                Intent i = new Intent(MainActivity.this, ThemTesterActivity.class);
                i.putExtra("ma", ma);
                i.putExtra("ten", ten);
                startActivityForResult(i, 2);
            } else {
                Intent i = new Intent(MainActivity.this, ThemLapTrinhVienActivity.class);
                i.putExtra("ma", ma);
                i.putExtra("ten", ten);
                startActivityForResult(i, 3);
            }
        });

        btnDong.setOnClickListener(v -> finish());

        lvNhanVien.setOnItemLongClickListener((parent, view, position, id) -> {
            viTriDangChon = position;
            moMenuSuaXoa(position);
            return true;
        });
    }

    private void moMenuSuaXoa(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thao tác");
        String[] options = {"Sửa", "Xóa"};

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) suaNhanVien(position);
            else if (which == 1) xoaNhanVien(position);
        });

        builder.show();
    }

    private void suaNhanVien(int position) {
        NhanVien nv = dsNhanVien.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa thông tin");

        View view = getLayoutInflater().inflate(R.layout.dialog_edit_nhanvien, null);
        EditText edtSuaTen = view.findViewById(R.id.edtSuaTen);
        Spinner spnSuaChucVu = view.findViewById(R.id.spnSuaChucVu);
        EditText edtSuaCapBac = view.findViewById(R.id.edtSuaCapBac);
        RadioGroup rgSuaTester = view.findViewById(R.id.rgSuaTester);
        EditText edtSuaNamKN = view.findViewById(R.id.edtSuaNamKN);

        edtSuaTen.setText(nv.hoTen);

        String[] chucvus = {"Quản lý", "Tester", "Lập trình viên"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, chucvus);
        spnSuaChucVu.setAdapter(spnAdapter);

        // Hiện dữ liệu cũ
        if (nv instanceof QuanLy) {
            spnSuaChucVu.setSelection(0);
            edtSuaCapBac.setVisibility(View.VISIBLE);
            edtSuaCapBac.setText(String.valueOf(((QuanLy) nv).capBac));
        } else if (nv instanceof Tester) {
            spnSuaChucVu.setSelection(1);
            rgSuaTester.setVisibility(View.VISIBLE);
            if (((Tester) nv).loaiKiemThu.equals("Hệ thống"))
                rgSuaTester.check(R.id.rbSuaHeThong);
            else
                rgSuaTester.check(R.id.rbSuaChucNang);
        } else if (nv instanceof LapTrinhVien) {
            spnSuaChucVu.setSelection(2);
            edtSuaNamKN.setVisibility(View.VISIBLE);
            edtSuaNamKN.setText(String.valueOf(((LapTrinhVien) nv).namKN));
        }

        // Khi đổi chức vụ
        spnSuaChucVu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view1, int pos, long id) {
                edtSuaCapBac.setVisibility(View.GONE);
                rgSuaTester.setVisibility(View.GONE);
                edtSuaNamKN.setVisibility(View.GONE);
                if (pos == 0) edtSuaCapBac.setVisibility(View.VISIBLE);
                else if (pos == 1) rgSuaTester.setVisibility(View.VISIBLE);
                else if (pos == 2) edtSuaNamKN.setVisibility(View.VISIBLE);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        builder.setView(view);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String tenMoi = edtSuaTen.getText().toString().trim();
            String chucvuMoi = spnSuaChucVu.getSelectedItem().toString();
            NhanVien nvMoi = null;

            if (chucvuMoi.equals("Quản lý")) {
                int capbac = Integer.parseInt(edtSuaCapBac.getText().toString());
                nvMoi = new QuanLy(nv.maNV, tenMoi, capbac);
            } else if (chucvuMoi.equals("Tester")) {
                int id = rgSuaTester.getCheckedRadioButtonId();
                if (id != -1) {
                    RadioButton rb = view.findViewById(id);
                    String loai = rb.getText().toString();
                    nvMoi = new Tester(nv.maNV, tenMoi, loai);
                }
            } else {
                int namkn = Integer.parseInt(edtSuaNamKN.getText().toString());
                nvMoi = new LapTrinhVien(nv.maNV, tenMoi, namkn);
            }

            if (nvMoi != null) {
                dsNhanVien.set(position, nvMoi);
                capNhatDanhSach();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void xoaNhanVien(int position) {
        dsNhanVien.remove(position);
        capNhatDanhSach();
        Toast.makeText(this, "Đã xóa nhân viên", Toast.LENGTH_SHORT).show();
    }

    private void capNhatDanhSach() {
        dsHienThi.clear();
        for (NhanVien nv : dsNhanVien) dsHienThi.add(nv.toString());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String ma = data.getStringExtra("ma");
            String ten = data.getStringExtra("ten");

            if (requestCode == 1) {
                int capbac = data.getIntExtra("capbac", 1);
                dsNhanVien.add(new QuanLy(ma, ten, capbac));
            } else if (requestCode == 2) {
                String loai = data.getStringExtra("loai");
                dsNhanVien.add(new Tester(ma, ten, loai));
            } else if (requestCode == 3) {
                int namkn = data.getIntExtra("namkn", 0);
                dsNhanVien.add(new LapTrinhVien(ma, ten, namkn));
            }
            capNhatDanhSach();
        }
    }
}
