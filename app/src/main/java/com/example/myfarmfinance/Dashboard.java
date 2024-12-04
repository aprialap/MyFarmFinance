package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Menentukan tombol berdasarkan ID
        Button btnDataPendapatan = findViewById(R.id.btnDataPendapatan);
        Button btnDataPengeluaran = findViewById(R.id.btnDataPengeluaran);
        Button btnLaporan = findViewById(R.id.btnLaporan);
        Button btnPengeluaran = findViewById(R.id.btnMaster);

        // Set listener untuk tombol Data Pendapatan
        btnDataPendapatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk membuka DataPendapatanActivity
                Intent intent = new Intent(Dashboard.this, DataPendapatanActivity.class);
                startActivity(intent);
            }
        });

        // Set listener untuk tombol Data Pengeluaran
        btnDataPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk membuka DataPengeluaranActivity
                Intent intent = new Intent(Dashboard.this, DataPengeluaranActivity.class);
                startActivity(intent);
            }
        });

        // Set listener untuk tombol Laporan
        btnLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk membuka LaporanActivity
                Intent intent = new Intent(Dashboard.this, LaporanActivity.class);
                startActivity(intent);
            }
        });

        // Set listener untuk tombol Master
        btnPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk membuka MasterActivity
                Intent intent = new Intent(Dashboard.this, MasterDasboardActivity.class);
                startActivity(intent);
            }
        });

    }
}