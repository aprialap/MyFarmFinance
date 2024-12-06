package com.example.myfarmfinance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LaporanPengeluaranActivity extends AppCompatActivity {

    private Button btnCetak;
    private EditText etStartDate, etEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_laporan_pengeluaran);

        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        setupDatePicker();

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaporanPengeluaranActivity.this, LaporanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Listener untuk Start Date
        DatePickerDialog.OnDateSetListener startDateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etStartDate.setText(format.format(calendar.getTime()));
        };

        // Listener untuk End Date
        DatePickerDialog.OnDateSetListener endDateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etEndDate.setText(format.format(calendar.getTime()));
        };

        // Set OnClickListener untuk Start Date
        etStartDate.setOnClickListener(v -> new DatePickerDialog(
                LaporanPengeluaranActivity.this,
                startDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        // Set OnClickListener untuk End Date
        etEndDate.setOnClickListener(v -> new DatePickerDialog(
                LaporanPengeluaranActivity.this,
                endDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show());
    }
}