package com.example.bt3_ptudd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ThemQuanLyActivity extends AppCompatActivity {

    EditText edtCapBac;
    Button btnXacNhan;

    String ma, ten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_quan_ly);

        edtCapBac = findViewById(R.id.edtCapBac);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        ma = getIntent().getStringExtra("ma");
        ten = getIntent().getStringExtra("ten");

        btnXacNhan.setOnClickListener(v -> {
            int capbac = Integer.parseInt(edtCapBac.getText().toString());
            Intent result = new Intent();
            result.putExtra("ma", ma);
            result.putExtra("ten", ten);
            result.putExtra("capbac", capbac);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
