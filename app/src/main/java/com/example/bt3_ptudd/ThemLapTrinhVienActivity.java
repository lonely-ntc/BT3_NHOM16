package com.example.bt3_ptudd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ThemLapTrinhVienActivity extends AppCompatActivity {

    EditText edtNamKN;
    Button btnXacNhanLapTrinh;

    String ma, ten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_lap_trinh_vien);

        edtNamKN = findViewById(R.id.edtNamKN);
        btnXacNhanLapTrinh = findViewById(R.id.btnXacNhanLapTrinh);

        ma = getIntent().getStringExtra("ma");
        ten = getIntent().getStringExtra("ten");

        btnXacNhanLapTrinh.setOnClickListener(v -> {
            int namkn = Integer.parseInt(edtNamKN.getText().toString());
            Intent result = new Intent();
            result.putExtra("ma", ma);
            result.putExtra("ten", ten);
            result.putExtra("namkn", namkn);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
