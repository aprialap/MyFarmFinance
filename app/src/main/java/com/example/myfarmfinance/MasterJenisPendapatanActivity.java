package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.JenisPendapatan;
import com.example.myfarmfinance.repositories.JenisPendapatanRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MasterJenisPendapatanActivity extends AppCompatActivity {

    private RecyclerView rvPendapatan;
    private JenisPendapatanAdapter adapter;
    private List<JenisPendapatan> jenisPendapatanList;

    private JenisPendapatanRepository jenisPendapatanRepository;
    private FloatingActionButton fabAdd; // Deklarasikan FloatingActionButton di tingkat kelas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_jenis_pendapatan);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddUserActivity when the FAB is clicked
                Intent intent = new Intent(MasterJenisPendapatanActivity.this, MasterDasboardActivity.class);
                startActivity(intent);
            }
        });

        // Inisialisasi RecyclerView
        rvPendapatan = findViewById(R.id.rvPendapatan);
        rvPendapatan.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi FloatingActionButton
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mulai aktivitas InsertPendapatanActivity
                Intent intent = new Intent(MasterJenisPendapatanActivity.this, InsertJenisPendapatanActivity.class);
                startActivity(intent);
            }
        });

        jenisPendapatanList = new ArrayList<>();
        jenisPendapatanRepository = new JenisPendapatanRepository(this);

        // Inisialisasi adapter dan pasang ke RecyclerView
        adapter = new JenisPendapatanAdapter(this, jenisPendapatanList, jenisPendapatanRepository);
        rvPendapatan.setAdapter(adapter);

        // Fetch data from Firebase
        fetchFromFirebase();
    }

    private void fetchFromFirebase() {
        JenisPendapatanRepository jenisPendapatanRepository = new JenisPendapatanRepository(this);
        jenisPendapatanRepository.getList("Nama",new JenisPendapatanRepository.OnListFetchedListener() {
            @Override
            public void onFetched(List<JenisPendapatan> fetched) {
                // Clear the existing list and add the fetched
                jenisPendapatanList.clear();
                jenisPendapatanList.addAll(fetched);

                // Notify adapter about data changes
                adapter.notifyDataSetChanged();
            }
        });
    }
}
