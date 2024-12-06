package com.example.myfarmfinance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfarmfinance.models.DataPengeluaran;
import com.example.myfarmfinance.models.JenisPengeluaran;
import com.example.myfarmfinance.repositories.DataPengeluaranRepository;
import com.example.myfarmfinance.repositories.JenisPengeluaranRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InsertDataPengeluaranActivity extends AppCompatActivity {

    private List<JenisPengeluaran> jenisPengeluaranList;
    private JenisPengeluaranRepository jenisPengeluaranRepository;
    private DataPengeluaranRepository dataPengeluaranRepository;
    private Spinner spinnerJenisPengeluaran;
    private Button btnSave;
    private EditText etId, etTanggal, etKeterangan, etJumlahPengeluaran;
    private TextView btnBack;

    private String selectedJenisPengeluaranId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data_pengeluaran);

        // Initialize UI elements
        etId = findViewById(R.id.etId);
        etTanggal = findViewById(R.id.etTanggal);
        etKeterangan = findViewById(R.id.etKeterangan);
        etJumlahPengeluaran = findViewById(R.id.etJumlahPengeluaran);
        spinnerJenisPengeluaran = findViewById(R.id.spinnerJenisPengeluaran);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Initialize repositories
        jenisPengeluaranRepository = new JenisPengeluaranRepository(this);
        dataPengeluaranRepository = new DataPengeluaranRepository(this);

        // Load existing data if editing
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            loadData(id);
        }

        // Back button functionality
        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(InsertDataPengeluaranActivity.this, DataPengeluaranActivity.class));
            finish();
        });

        // Setup date picker
        setupDatePicker();

        // Fetch and populate spinner data
        fetchJenisPengeluaranData();

        // Save button functionality
        btnSave.setOnClickListener(v -> saveData(id));
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            etTanggal.setText(format.format(calendar.getTime()));
        };

        etTanggal.setOnClickListener(v -> new DatePickerDialog(
                InsertDataPengeluaranActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show());
    }

    private void fetchJenisPengeluaranData() {
        jenisPengeluaranRepository.getList("nama", fetchedList -> {
            if (fetchedList == null || fetchedList.isEmpty()) {
                Toast.makeText(this, "Data jenis pengeluaran tidak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }

            jenisPengeluaranList = new ArrayList<>(fetchedList);

            List<String> jenisPengeluaranNames = new ArrayList<>();
            for (JenisPengeluaran item : jenisPengeluaranList) {
                jenisPengeluaranNames.add(item.getNama());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(InsertDataPengeluaranActivity.this, R.layout.spinner_item, jenisPengeluaranNames);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerJenisPengeluaran.setAdapter(adapter);

            spinnerJenisPengeluaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedJenisPengeluaranId = jenisPengeluaranList.get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedJenisPengeluaranId = null;
                }
            });
        });
    }

    private void loadData(String id) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("data_pengeluaran").child(id);
        dataRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                DataPengeluaran dataPengeluaran = snapshot.getValue(DataPengeluaran.class);
                if (dataPengeluaran != null) {
                    etId.setText(dataPengeluaran.getId());
                    etTanggal.setText(dataPengeluaran.getTanggal());
                    etKeterangan.setText(dataPengeluaran.getKeterangan());
                    etJumlahPengeluaran.setText(String.valueOf(dataPengeluaran.getJumlahPengeluaran()));

                    // Preselect spinner value
                    if (jenisPengeluaranList != null && !jenisPengeluaranList.isEmpty()) {
                        for (int i = 0; i < jenisPengeluaranList.size(); i++) {
                            if (jenisPengeluaranList.get(i).getId().equals(dataPengeluaran.getJenisPengeluaranId())) {
                                spinnerJenisPengeluaran.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
        });
    }


    private void saveData(String id) {
        String tanggal = etTanggal.getText().toString().trim();
        String keterangan = etKeterangan.getText().toString().trim();
        String jumlahPengeluaran = etJumlahPengeluaran.getText().toString().trim();
        String jenisPengeluaranNama = spinnerJenisPengeluaran.getSelectedItem().toString();

        if (tanggal.isEmpty() || keterangan.isEmpty() || jumlahPengeluaran.isEmpty() || selectedJenisPengeluaranId == null) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        double total;
        try {
            total = Double.parseDouble(jumlahPengeluaran);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Total pendapatan harus berupa angka!", Toast.LENGTH_SHORT).show();
            return;
        }

        DataPengeluaran dataPengeluaran = new DataPengeluaran(
                id == null ? null : id,
                tanggal,
                selectedJenisPengeluaranId,
                jenisPengeluaranNama,
                keterangan,
                total
        );

        if (id == null) {
            dataPengeluaranRepository.insert(dataPengeluaran);
        } else {
            dataPengeluaranRepository.update(dataPengeluaran);
        }

        Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
        resetInputFields();
        startActivity(new Intent(InsertDataPengeluaranActivity.this, DataPengeluaranActivity.class));
        finish();
    }


    private void resetInputFields() {
        etTanggal.setText("");
        etKeterangan.setText("");
        etJumlahPengeluaran.setText("");
       //spinnerJenisPengeluaran.setSelection(0);
    }
}
