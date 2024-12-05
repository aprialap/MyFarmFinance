package com.example.myfarmfinance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfarmfinance.models.DataPendapatan;
import com.example.myfarmfinance.models.JenisPendapatan;
import com.example.myfarmfinance.repositories.DataPendapatanRepository;
import com.example.myfarmfinance.repositories.JenisPendapatanRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InsertDataPendapatanActivity extends AppCompatActivity {

    private List<JenisPendapatan> jenisPendapatanList;
    private JenisPendapatanRepository jenisPendapatanRepository;
    private DataPendapatanRepository dataPendapatanRepository;
    private Spinner spinnerJenisPendapatan;

    private Button btnSave;
    private EditText etId, etTanggal, etSumberPendapatan, etTotalPendapatan;
    private TextView btnBack;

    private String selectedJenisPendapatanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data_pendapatan);

        // Initialize UI elements
        etId = findViewById(R.id.etId);
        etTanggal = findViewById(R.id.etTanggal);
        etSumberPendapatan = findViewById(R.id.etSumberPendapatan);
        etTotalPendapatan = findViewById(R.id.etTotalPendapatan);
        spinnerJenisPendapatan = findViewById(R.id.spinnerJenisPendapatan);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Initialize repositories
        jenisPendapatanRepository = new JenisPendapatanRepository(this);
        dataPendapatanRepository = new DataPendapatanRepository(this);

        // Load existing data if editing
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            loadData(id);
        }

        // Back button functionality
        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(InsertDataPendapatanActivity.this, DataPendapatanActivity.class));
            finish();
        });

        // Setup date picker
        setupDatePicker();

        // Fetch and populate spinner data
        fetchJenisPendapatanData();

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
                InsertDataPendapatanActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show());
    }

    private void fetchJenisPendapatanData() {
        jenisPendapatanRepository.getList("nama", fetchedList -> {
            if (fetchedList == null || fetchedList.isEmpty()) {
                Toast.makeText(this, "Data jenis pendapatan tidak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }

            jenisPendapatanList = new ArrayList<>(fetchedList);

            List<String> jenisPendapatanNames = new ArrayList<>();
            for (JenisPendapatan item : jenisPendapatanList) {
                jenisPendapatanNames.add(item.getNama());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(InsertDataPendapatanActivity.this, R.layout.spinner_item, jenisPendapatanNames);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerJenisPendapatan.setAdapter(adapter);

            spinnerJenisPendapatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedJenisPendapatanId = jenisPendapatanList.get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedJenisPendapatanId = null;
                }
            });
        });
    }

    private void loadData(String id) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("data_pendapatan").child(id);
        dataRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                DataPendapatan dataPendapatan = snapshot.getValue(DataPendapatan.class);
                if (dataPendapatan != null) {
                    etId.setText(dataPendapatan.getId());
                    etTanggal.setText(dataPendapatan.getTanggal());
                    etSumberPendapatan.setText(dataPendapatan.getSumberPendapatan());
                    etTotalPendapatan.setText(String.valueOf(dataPendapatan.getTotal()));

                    // Preselect spinner value
                    if (jenisPendapatanList != null && !jenisPendapatanList.isEmpty()) {
                        for (int i = 0; i < jenisPendapatanList.size(); i++) {
                            if (jenisPendapatanList.get(i).getId().equals(dataPendapatan.getJenisPendapatanId())) {
                                spinnerJenisPendapatan.setSelection(i);
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
        String sumberPendapatan = etSumberPendapatan.getText().toString().trim();
        String totalString = etTotalPendapatan.getText().toString().trim();
        String jenisPendapatanNama = spinnerJenisPendapatan.getSelectedItem().toString();

        if (tanggal.isEmpty() || sumberPendapatan.isEmpty() || totalString.isEmpty() || selectedJenisPendapatanId == null) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        double total;
        try {
            total = Double.parseDouble(totalString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Total pendapatan harus berupa angka!", Toast.LENGTH_SHORT).show();
            return;
        }

        DataPendapatan dataPendapatan = new DataPendapatan(
                id == null ? null : id,
                tanggal,
                selectedJenisPendapatanId,
                jenisPendapatanNama,
                sumberPendapatan,
                total
        );

        if (id == null) {
            dataPendapatanRepository.insert(dataPendapatan);
        } else {
            dataPendapatanRepository.update(dataPendapatan);
        }

        Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
        resetInputFields();
        startActivity(new Intent(InsertDataPendapatanActivity.this, DataPendapatanActivity.class));
        finish();
    }


    private void resetInputFields() {
        etTanggal.setText("");
        etSumberPendapatan.setText("");
        etTotalPendapatan.setText("");
       // spinnerJenisPendapatan.setSelection(0);
    }
}
