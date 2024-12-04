package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfarmfinance.models.JenisPendapatan;
import com.example.myfarmfinance.models.User;
import com.example.myfarmfinance.repositories.JenisPendapatanRepository;
import com.example.myfarmfinance.repositories.UserRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class InsertJenisPendapatanActivity extends AppCompatActivity {
    private EditText etId, etNamaPendapatan;
    private Button btnSave;

    private List<JenisPendapatan> jenisPendapatanList;
    private JenisPendapatanRepository jenisPendapatanRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_jenis_pendapatan);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsertJenisPendapatanActivity.this, MasterJenisPendapatanActivity.class);
                startActivity(intent);
            }
        });

        // Inisialisasi View
        etId = findViewById(R.id.etId);
        etNamaPendapatan = findViewById(R.id.etNamaPendapatan);
        btnSave = findViewById(R.id.btnSave);

        String Id = getIntent().getStringExtra("id");

        // Jika Id tidak null, berarti ini mode edit
        if (Id != null) {
            loadData(Id, etNamaPendapatan);
        }

        // Inisialisasi
        jenisPendapatanList = new ArrayList<>();
        jenisPendapatanRepository = new JenisPendapatanRepository(this);

        // Listener untuk tombol simpan
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id = etId.getText().toString().trim();
                String namaPendapatan = etNamaPendapatan.getText().toString().trim();

                if (namaPendapatan.isEmpty()) {
                    // Validasi jika input kosong
                    Toast.makeText(InsertJenisPendapatanActivity.this, "Nama Pendapatan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    if (Id == null || Id.isEmpty()) {
                        JenisPendapatan newData = new JenisPendapatan(null, namaPendapatan);
                        jenisPendapatanRepository.insert(newData); // Insert
                    }else{
                        JenisPendapatan updateData = new JenisPendapatan(Id, namaPendapatan);
                        jenisPendapatanRepository.update(updateData); // update
                    }

                    resetInputFields();
                    Intent intent = new Intent(InsertJenisPendapatanActivity.this, MasterJenisPendapatanActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void resetInputFields() {
        etId.setText("");
        etNamaPendapatan.setText("");

    }

    private void loadData(String Id, EditText etNamaPendapatan) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("jenis_pendapatan").child(Id);
        dataRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                JenisPendapatan jenisPendapatan = snapshot.getValue(JenisPendapatan.class);
                if (jenisPendapatan != null) {
                    etId.setText(jenisPendapatan.getId());
                    etNamaPendapatan.setText(jenisPendapatan.getNama());
                }
            }
        });
    }
}
