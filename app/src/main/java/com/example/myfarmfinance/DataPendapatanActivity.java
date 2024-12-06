package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.DataPendapatan;
import com.example.myfarmfinance.models.JenisPendapatan;
import com.example.myfarmfinance.repositories.DataPendapatanRepository;
import com.example.myfarmfinance.repositories.JenisPendapatanRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.List;

public class DataPendapatanActivity extends AppCompatActivity {

    private RecyclerView rvDataPendapatan;
    private PendapatanAdapter adapter;
    private List<DataPendapatan> pendapatanList;
    private DataPendapatanRepository dataPendapatanRepository;
    private FloatingActionButton fabAdd; // Deklarasikan FloatingActionButton di tingkat kelas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendapatan);

        // Back button functionality
        findViewById(R.id.btnBack).setOnClickListener(view -> {
            startActivity(new Intent(DataPendapatanActivity.this, Dashboard.class));
            finish();
        });


        // Inisialisasi FloatingActionButton untuk navigasi ke InsertDataPendapatanActivity
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigasi ke activity untuk memasukkan data pendapatan baru
                Intent intent = new Intent(DataPendapatanActivity.this, InsertDataPendapatanActivity.class);
                startActivity(intent);
            }
        });

        // Inisialisasi RecyclerView dan atur LayoutManager
        rvDataPendapatan = findViewById(R.id.rvDataPendapatan);
        rvDataPendapatan.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi list pendapatan dan repository
        pendapatanList = new ArrayList<>();
        dataPendapatanRepository = new DataPendapatanRepository(this);

        // Inisialisasi adapter dan pasang ke RecyclerView
        adapter = new PendapatanAdapter(this, pendapatanList, dataPendapatanRepository);
        rvDataPendapatan.setAdapter(adapter);

        // Ambil data dari Firebase
       fetchFromFirebase();
    }

    // Method untuk mengambil data pendapatan dari Firebase
    private void fetchFromFirebase() {
        DataPendapatanRepository dataPendapatanRepository = new DataPendapatanRepository(this);
        dataPendapatanRepository.getList("Tanggal",new DataPendapatanRepository.OnListFetchedListener() {
            @Override
            public void onFetched(List<DataPendapatan> fetched) {
                // Clear the existing list and add the fetched
                pendapatanList.clear();
                pendapatanList.addAll(fetched);

                // Notify adapter about data changes
                adapter.notifyDataSetChanged();
            }
        });
    }
}
