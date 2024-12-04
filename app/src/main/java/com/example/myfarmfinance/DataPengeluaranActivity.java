package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.DataPendapatan;
import com.example.myfarmfinance.models.DataPengeluaran;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DataPengeluaranActivity extends AppCompatActivity {

    private RecyclerView rvDataPengeluaran;
    private PengeluaranAdapter adapter;
    private List<DataPengeluaran> pengeluaranList;
    private FloatingActionButton fabAdd; // Deklarasikan FloatingActionButton di tingkat kelas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pengeluaran);

        // Inisialisasi FloatingActionButton
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mulai aktivitas InsertPendapatanActivity
                Intent intent = new Intent(DataPengeluaranActivity.this, InsertJenisPendapatanActivity.class);
                startActivity(intent);
            }
        });

        rvDataPengeluaran = findViewById(R.id.rvDataPengeluaran);
        rvDataPengeluaran.setLayoutManager(new LinearLayoutManager(this));

        pengeluaranList = new ArrayList<>();
        pengeluaranList.add(new DataPengeluaran(1, "2024-12-01", "Pembelian Bahan Baku", "Bahan untuk pembuatan produk", 500000.0));
        pengeluaranList.add(new DataPengeluaran(2, "2024-12-02", "Gaji Karyawan", "Pembayaran gaji karyawan bulan Desember", 2000000.0));
        pengeluaranList.add(new DataPengeluaran(3, "2024-12-03", "Transportasi", "Biaya transportasi pengiriman produk", 300000.0));
        pengeluaranList.add(new DataPengeluaran(4, "2024-12-04", "Pemeliharaan Mesin", "Biaya pemeliharaan mesin pertanian", 150000.0));
        pengeluaranList.add(new DataPengeluaran(5, "2024-12-05", "Listrik dan Air", "Pembayaran tagihan listrik dan air untuk usaha", 100000.0));

        // Inisialisasi adapter dan pasang ke RecyclerView
        adapter = new PengeluaranAdapter(this, pengeluaranList);
        rvDataPengeluaran.setAdapter(adapter);
    }
}