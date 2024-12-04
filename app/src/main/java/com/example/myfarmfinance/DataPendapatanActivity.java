package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.DataPendapatan;
import com.example.myfarmfinance.models.JenisPendapatan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class DataPendapatanActivity extends AppCompatActivity {

    private RecyclerView rvDataPendapatan;
    private PendapatanAdapter adapter;
    private List<DataPendapatan> pendapatanList;
    private FloatingActionButton fabAdd; // Deklarasikan FloatingActionButton di tingkat kelas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendapatan);

        // Inisialisasi FloatingActionButton
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mulai aktivitas InsertPendapatanActivity
                Intent intent = new Intent(DataPendapatanActivity.this, InsertJenisPendapatanActivity.class);
                startActivity(intent);
            }
        });

        rvDataPendapatan = findViewById(R.id.rvDataPendapatan);
        rvDataPendapatan.setLayoutManager(new LinearLayoutManager(this));

        pendapatanList = new ArrayList<>();
        pendapatanList.add(new DataPendapatan(1, "2024-12-01", "Penjualan Produk", "Petani A", 1000000.0));
        pendapatanList.add(new DataPendapatan(2, "2024-12-02", "Penjualan Hasil Pertanian", "Petani B", 1500000.0));

        // Inisialisasi adapter dan pasang ke RecyclerView
        adapter = new PendapatanAdapter(this, pendapatanList);
        rvDataPendapatan.setAdapter(adapter);
    }


}