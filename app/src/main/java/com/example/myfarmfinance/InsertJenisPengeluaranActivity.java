package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfarmfinance.models.JenisPendapatan;
import com.example.myfarmfinance.models.JenisPengeluaran;
import com.example.myfarmfinance.repositories.JenisPendapatanRepository;
import com.example.myfarmfinance.repositories.JenisPengeluaranRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class InsertJenisPengeluaranActivity extends AppCompatActivity {
    private EditText etId, etNamaPengeluaran;

    private List<JenisPengeluaran> jenisPengeluaranList;
    private JenisPengeluaranRepository jenisPengeluaranRepository;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_jenis_pengeluaran);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsertJenisPengeluaranActivity.this, MasterJenisPengeluaranActivity.class);
                startActivity(intent);
            }
        });

        // Inisialisasi View
        etId = findViewById(R.id.etId);
        etNamaPengeluaran = findViewById(R.id.etNamaPengeluaran);
        btnSave = findViewById(R.id.btnSave);

        String Id = getIntent().getStringExtra("id");

        // Jika Id tidak null, berarti ini mode edit
        if (Id != null) {
            loadData(Id, etNamaPengeluaran);
        }

        // Inisialisasi
        jenisPengeluaranList = new ArrayList<>();
        jenisPengeluaranRepository = new JenisPengeluaranRepository(this);

        // Listener untuk tombol simpan
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id = etId.getText().toString().trim();
                String namaPengeluaran = etNamaPengeluaran.getText().toString().trim();

                if (namaPengeluaran.isEmpty()) {
                    // Validasi jika input kosong
                    Toast.makeText(InsertJenisPengeluaranActivity.this, "Nama pengeluaran tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    if (Id == null || Id.isEmpty()) {
                        JenisPengeluaran newData = new JenisPengeluaran(null, namaPengeluaran);
                        jenisPengeluaranRepository.insert(newData); // Insert
                    }else{
                        JenisPengeluaran updateData = new JenisPengeluaran(Id, namaPengeluaran);
                        jenisPengeluaranRepository.update(updateData); // update
                    }

                    resetInputFields();
                    Intent intent = new Intent(InsertJenisPengeluaranActivity.this, MasterJenisPengeluaranActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void resetInputFields() {
        etId.setText("");
        etNamaPengeluaran.setText("");

    }

    private void loadData(String Id, EditText etNamaPengeluaran) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("jenis_pengeluaran").child(Id);
        dataRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                JenisPengeluaran jenisPengeluaran = snapshot.getValue(JenisPengeluaran.class);
                if (jenisPengeluaran != null) {
                    etId.setText(jenisPengeluaran.getId());
                    etNamaPengeluaran.setText(jenisPengeluaran.getNama());
                }
            }
        });
    }
}
