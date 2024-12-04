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

import com.example.myfarmfinance.models.JenisPengeluaran;

import com.example.myfarmfinance.repositories.JenisPengeluaranRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MasterJenisPengeluaranActivity extends AppCompatActivity {

    private RecyclerView rvPengeluaran;
    private JenisPengeluaranAdapter adapter;
    private List<JenisPengeluaran> jenisPengeluaranList;
    private JenisPengeluaranRepository jenisPengeluaranRepository;
    private FloatingActionButton fabAdd; // Deklarasikan FloatingActionButton di tingkat kelas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_jenis_pengeluaran);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddUserActivity when the FAB is clicked
                Intent intent = new Intent(MasterJenisPengeluaranActivity.this, MasterDasboardActivity.class);
                startActivity(intent);
            }
        });
        // Inisialisasi RecyclerView
        rvPengeluaran = findViewById(R.id.rvPengeluaran);
        rvPengeluaran.setLayoutManager(new LinearLayoutManager(this));


        // Inisialisasi FloatingActionButton
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mulai aktivitas InsertPendapatanActivity
                Intent intent = new Intent(MasterJenisPengeluaranActivity.this, InsertJenisPengeluaranActivity.class);
                startActivity(intent);
            }
        });

        // Contoh data
        jenisPengeluaranList = new ArrayList<>();
        jenisPengeluaranRepository = new JenisPengeluaranRepository(this);

        // Inisialisasi adapter dan pasang ke RecyclerView
        adapter = new JenisPengeluaranAdapter(this, jenisPengeluaranList, jenisPengeluaranRepository);
        rvPengeluaran.setAdapter(adapter);

        // Fetch data from Firebase
       fetchFromFirebase();
    }

    private void fetchFromFirebase() {
        JenisPengeluaranRepository jenisPengeluaranRepository = new JenisPengeluaranRepository(this);
        jenisPengeluaranRepository.getList("Nama",new JenisPengeluaranRepository.OnListFetchedListener() {
            @Override
            public void onFetched(List<JenisPengeluaran> fetched) {
                // Clear the existing list and add the fetched
                jenisPengeluaranList.clear();
                jenisPengeluaranList.addAll(fetched);

                // Notify adapter about data changes
                adapter.notifyDataSetChanged();
            }
        });
    }
}