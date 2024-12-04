package com.example.myfarmfinance;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

public class MasterDasboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_dasboard);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterDasboardActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnMasterUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke Master User Activity
                Intent intent = new Intent(MasterDasboardActivity.this, MasterUserActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnMasterJenisPendapatan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke Master Jenis Pendapatan Activity
                Intent intent = new Intent(MasterDasboardActivity.this, MasterJenisPendapatanActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnMasterJenisPengeluaran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke Master Jenis Pengeluaran Activity
                Intent intent = new Intent(MasterDasboardActivity.this, MasterJenisPengeluaranActivity.class);
                startActivity(intent);
            }
        });
    }
}