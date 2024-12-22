package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LaporanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        // Ambil role dari Intent
        String role = getIntent().getStringExtra("role");

        // Referensi tombol Back
        TextView btnBack = findViewById(R.id.btnBack);

        // Sembunyikan tombol Back jika role adalah Owner
        if ("Owner".equalsIgnoreCase(role)) {
            btnBack.setVisibility(View.GONE);
        }

        // Set onClickListener untuk tombol Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaporanActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnLaporanPendapatan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke Laporan Pendapatan Activity
                Intent intent = new Intent(LaporanActivity.this, LaporanPendapatanActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnLaporanPengeluaran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke Laporan Pengeluaran Activity
                Intent intent = new Intent(LaporanActivity.this, LaporanPengeluaranActivity.class);
                startActivity(intent);
            }
        });
    }
}
