package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.DataPengeluaran;
import com.example.myfarmfinance.repositories.DataPengeluaranRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DataPengeluaranActivity extends AppCompatActivity {

    private RecyclerView rvDataPengeluaran;
    private PengeluaranAdapter adapter;
    private List<DataPengeluaran> pengeluaranList;
    private DataPengeluaranRepository dataPengeluaranRepository;
    private FloatingActionButton fabAdd; // Deklarasikan FloatingActionButton di tingkat kelas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pengeluaran);

        // Back button functionality
        findViewById(R.id.btnBack).setOnClickListener(view -> {
            startActivity(new Intent(DataPengeluaranActivity.this, Dashboard.class));
            finish();
        });

        // Inisialisasi FloatingActionButton untuk navigasi ke InsertDataPengeluaranActivity
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigasi ke activity untuk memasukkan data pengeluaran baru
                Intent intent = new Intent(DataPengeluaranActivity.this, InsertDataPengeluaranActivity.class);
                startActivity(intent);
            }
        });

        rvDataPengeluaran = findViewById(R.id.rvDataPengeluaran);
        rvDataPengeluaran.setLayoutManager(new LinearLayoutManager(this));

        pengeluaranList = new ArrayList<>();
        dataPengeluaranRepository = new DataPengeluaranRepository(this);

        // Inisialisasi adapter dan pasang ke RecyclerView
        adapter = new PengeluaranAdapter(this, pengeluaranList, dataPengeluaranRepository);
        rvDataPengeluaran.setAdapter(adapter);

        // Ambil data dari Firebase
        fetchFromFirebase();
    }

    // Method untuk mengambil data pengeluaran dari Firebase
    private void fetchFromFirebase() {
        DataPengeluaranRepository dataPengeluaranRepository = new DataPengeluaranRepository(this);
        dataPengeluaranRepository.getList("Tanggal",new DataPengeluaranRepository.OnListFetchedListener() {
            @Override
            public void onFetched(List<DataPengeluaran> fetched) {
                // Clear the existing list and add the fetched
                pengeluaranList.clear();
                pengeluaranList.addAll(fetched);

                // Notify adapter about data changes
                adapter.notifyDataSetChanged();
            }
        });
    }
}