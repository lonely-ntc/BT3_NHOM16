package com.example.bt3_ptudd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class ThemTesterActivity extends AppCompatActivity {

    RadioGroup rgLoaiKT;
    Button btnXacNhanTester;

    String ma, ten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tester);

        rgLoaiKT = findViewById(R.id.rgLoaiKT);
        btnXacNhanTester = findViewById(R.id.btnXacNhanTester);

        ma = getIntent().getStringExtra("ma");
        ten = getIntent().getStringExtra("ten");

        btnXacNhanTester.setOnClickListener(v -> {
            int id = rgLoaiKT.getCheckedRadioButtonId();
            RadioButton rb = findViewById(id);
            String loai = rb.getText().toString();

            Intent result = new Intent();
            result.putExtra("ma", ma);
            result.putExtra("ten", ten);
            result.putExtra("loai", loai);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
