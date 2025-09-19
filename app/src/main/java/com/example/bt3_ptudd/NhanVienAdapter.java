package com.example.bt3_ptudd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder>
        implements DatabaseHelper.OnDataChangedListener {

    private ArrayList<NhanVien> list;
    private final LayoutInflater inflater;
    private final DatabaseHelper db;

    public NhanVienAdapter(Context context, DatabaseHelper db) {
        this.inflater = LayoutInflater.from(context);
        this.db = db;
        this.list = db.getTatCaNhanVien();

        // Đăng ký lắng nghe thay đổi dữ liệu từ database
        db.setOnDataChangedListener(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVien nv = list.get(position);
        holder.txtLine1.setText(nv.maNV + " - " + nv.hoTen);
        holder.txtLine2.setText(getMoTa(nv));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String getMoTa(NhanVien nv) {
        if (nv instanceof QuanLy) {
            return "Quản lý - Cấp bậc: " + ((QuanLy) nv).capBac;
        } else if (nv instanceof Tester) {
            return "Tester - Loại: " + ((Tester) nv).loaiKiemThu;
        } else if (nv instanceof LapTrinhVien) {
            return "Lập trình viên - Kinh nghiệm: " + ((LapTrinhVien) nv).namKN + " năm";
        }
        return "Nhân viên";
    }

    @Override
    public void onDataChanged() {
        // Mỗi khi database có thay đổi, tải lại danh sách và cập nhật adapter
        this.list = db.getTatCaNhanVien();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLine1, txtLine2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLine1 = itemView.findViewById(android.R.id.text1);
            txtLine2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
