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

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        edtMaNV = findViewById(R.id.edtMaNV);
        edtHoTen = findViewById(R.id.edtHoTen);
        spnChucVu = findViewById(R.id.spnChucVu);
        btnThem = findViewById(R.id.btnThem);
        btnDong = findViewById(R.id.btnDong);
        lvNhanVien = findViewById(R.id.lvNhanVien);

        dbHelper = new DatabaseHelper(this);
        dsNhanVien = new ArrayList<>();
        dsHienThi = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsHienThi);
        lvNhanVien.setAdapter(adapter);

        // Khi DB thay đổi => tải lại
        dbHelper.setOnDataChangedListener(() -> loadData());

        // Load dữ liệu ban đầu
        loadData();

        // Spinner chức vụ
        String[] chucvus = {"Quản lý", "Tester", "Lập trình viên"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, chucvus);
        spnChucVu.setAdapter(spnAdapter);

        // Thêm nhân viên
        btnThem.setOnClickListener(v -> themNhanVien());

        btnDong.setOnClickListener(v -> finish());

        // Nhấn giữ item để sửa/xóa
        lvNhanVien.setOnItemLongClickListener((parent, view, position, id) -> {
            moMenuSuaXoa(position);
            return true;
        });
    }

    private void loadData() {
        dsNhanVien.clear();
        dsNhanVien.addAll(dbHelper.getTatCaNhanVien());
        capNhatDanhSach();
    }

    private void themNhanVien() {
        String ma = edtMaNV.getText().toString().trim();
        String ten = edtHoTen.getText().toString().trim();
        String chucvu = spnChucVu.getSelectedItem().toString();

        if (ma.isEmpty() || ten.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i;
        if (chucvu.equals("Quản lý")) {
            i = new Intent(this, ThemQuanLyActivity.class);
        } else if (chucvu.equals("Tester")) {
            i = new Intent(this, ThemTesterActivity.class);
        } else {
            i = new Intent(this, ThemLapTrinhVienActivity.class);
        }
        i.putExtra("ma", ma);
        i.putExtra("ten", ten);
        startActivityForResult(i, chucvu.equals("Quản lý") ? 1 : chucvu.equals("Tester") ? 2 : 3);
    }

    private void moMenuSuaXoa(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thao tác");
        String[] options = {"Sửa", "Xóa"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) suaNhanVien(position);
            else xoaNhanVien(position);
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

        // Spinner sửa chức vụ
        String[] chucvus = {"Quản lý", "Tester", "Lập trình viên"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, chucvus);
        spnSuaChucVu.setAdapter(spnAdapter);

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

        spnSuaChucVu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view1, int pos, long id) {
                edtSuaCapBac.setVisibility(View.GONE);
                rgSuaTester.setVisibility(View.GONE);
                edtSuaNamKN.setVisibility(View.GONE);
                if (pos == 0) edtSuaCapBac.setVisibility(View.VISIBLE);
                else if (pos == 1) rgSuaTester.setVisibility(View.VISIBLE);
                else edtSuaNamKN.setVisibility(View.VISIBLE);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        builder.setView(view);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String tenMoi = edtSuaTen.getText().toString().trim();
            if (tenMoi.isEmpty()) {
                Toast.makeText(this, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            String chucvuMoi = spnSuaChucVu.getSelectedItem().toString();
            NhanVien nvMoi = null;

            try {
                if (chucvuMoi.equals("Quản lý")) {
                    int capbac = Integer.parseInt(edtSuaCapBac.getText().toString());
                    nvMoi = new QuanLy(nv.maNV, tenMoi, capbac);
                } else if (chucvuMoi.equals("Tester")) {
                    int id = rgSuaTester.getCheckedRadioButtonId();
                    if (id == -1) {
                        Toast.makeText(this, "Chọn loại kiểm thử!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RadioButton rb = view.findViewById(id);
                    nvMoi = new Tester(nv.maNV, tenMoi, rb.getText().toString());
                } else {
                    int namkn = Integer.parseInt(edtSuaNamKN.getText().toString());
                    nvMoi = new LapTrinhVien(nv.maNV, tenMoi, namkn);
                }

                if (nvMoi != null) dbHelper.suaNhanVien(nvMoi);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Thông tin nhập chưa hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void xoaNhanVien(int position) {
        NhanVien nv = dsNhanVien.get(position);
        dbHelper.xoaNhanVien(nv.maNV);
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
        if (resultCode == RESULT_OK && data != null) {
            String ma = data.getStringExtra("ma");
            String ten = data.getStringExtra("ten");

            if (requestCode == 1) {
                int capbac = data.getIntExtra("capbac", 1);
                dbHelper.themNhanVien(new QuanLy(ma, ten, capbac));
            } else if (requestCode == 2) {
                String loai = data.getStringExtra("loai");
                dbHelper.themNhanVien(new Tester(ma, ten, loai));
            } else if (requestCode == 3) {
                int namkn = data.getIntExtra("namkn", 0);
                dbHelper.themNhanVien(new LapTrinhVien(ma, ten, namkn));
            }
        }
    }
}
