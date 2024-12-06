package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LaporanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaporanActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnLaporanPendapatan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke Master User Activity
                Intent intent = new Intent(LaporanActivity.this, LaporanPendapatanActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnLaporanPengeluaran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke Master User Activity
                Intent intent = new Intent(LaporanActivity.this, LaporanPengeluaranActivity.class);
                startActivity(intent);
            }
        });

    }
}