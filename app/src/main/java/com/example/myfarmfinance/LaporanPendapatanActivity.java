package com.example.myfarmfinance;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfarmfinance.models.DataPendapatan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LaporanPendapatanActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 100;
    private Button btnCetak;
    private EditText etStartDate, etEndDate;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_laporan_pendapatan);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnCetak = findViewById(R.id.btnCetak);

        setupDatePicker();

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(LaporanPendapatanActivity.this, LaporanActivity.class);
            startActivity(intent);
        });

        btnCetak.setOnClickListener(v -> {
            // Cek izin penyimpanan sebelum melanjutkan
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                cetakPdf();
            }
        });
    }

    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();

        etStartDate.setOnClickListener(v -> showDatePicker(calendar, date -> etStartDate.setText(date)));
        etEndDate.setOnClickListener(v -> showDatePicker(calendar, date -> etEndDate.setText(date)));
    }

    private void showDatePicker(Calendar calendar, DatePickerCallback callback) {
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    callback.onDateSelected(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void cetakPdf() {
        String startDateString = etStartDate.getText().toString();
        String endDateString = etEndDate.getText().toString();

        if (startDateString.isEmpty() || endDateString.isEmpty()) {
            Toast.makeText(this, "Tolong pilih tanggal mulai dan selesai", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            if (startDate == null || endDate == null) {
                Toast.makeText(this, "Format tanggal tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            fetchAndGeneratePdf(startDate, endDate);
        } catch (ParseException e) {
            Toast.makeText(this, "Error parsing date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAndGeneratePdf(Date startDate, Date endDate) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data_pendapatan");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DataPendapatan> dataPendapatanList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataPendapatan data = snapshot.getValue(DataPendapatan.class);
                    if (data != null) {
                        try {
                            Date dataDate = dateFormat.parse(data.getTanggal());
                            if (dataDate != null && dataDate.compareTo(startDate) >= 0 && dataDate.compareTo(endDate) <= 0) {
                                dataPendapatanList.add(data);
                            }
                        } catch (ParseException e) {
                            Log.e("Date Parse", "Error parsing date from Firebase", e);
                        }
                    }
                }
                generatePdf(dataPendapatanList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generatePdf(List<DataPendapatan> dataPendapatanList) {
        // Tentukan nama file PDF
        String fileName = "data_pendapatan_filtered.pdf";

        OutputStream outputStream = null;
        Uri pdfUri = null;

        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // Android 10+ menggunakan MediaStore
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
//                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
//                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
//
//                pdfUri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);
//                if (pdfUri != null) {
//                    outputStream = getContentResolver().openOutputStream(pdfUri);
//                }
//            } else {
                // Android 9 dan sebelumnya menggunakan direktori file eksternal
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File pdfFile = new File(downloadsDir, fileName);
                outputStream = new FileOutputStream(pdfFile);
                pdfUri = Uri.fromFile(pdfFile);
//            }

            if (outputStream != null) {
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                // Tambahkan judul
                document.add(new Paragraph("Laporan Pendapatan")
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(20)
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

                document.add(new Paragraph("Tanggal Cetak: " + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date())));

                // Buat tabel dengan 4 kolom
                float[] columnWidths = {2, 2, 3, 2};
                Table table = new Table(columnWidths);
                table.setWidth(520);

                // Header tabel
                table.addCell("Tanggal");
                table.addCell("Jenis");
                table.addCell("Sumber");
                table.addCell("Total");

                double subtotal = 0;

                // Tambahkan data ke tabel
                for (DataPendapatan data : dataPendapatanList) {
                    table.addCell(data.getTanggal());
                    table.addCell(data.getJenisPendapatanNama());
                    table.addCell(data.getSumberPendapatan());
                    double total = data.getTotal();
                    table.addCell(String.valueOf(total));
                    subtotal += total;
                }

                // Tambahkan subtotal
                table.addCell(new Cell(1, 3).add(new Paragraph("Subtotal"))
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", subtotal)))
                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

                document.add(table);
                document.close();

                Toast.makeText(this, "PDF berhasil disimpan di: " + pdfUri.getPath(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Log.e("PDF Error", "Error saving PDF", e);
            Toast.makeText(this, "Gagal menyimpan PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private interface DatePickerCallback {
        void onDateSelected(String date);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cetakPdf();
        if (requestCode == STORAGE_PERMISSION_CODE) {
            // Memeriksa apakah izin yang diminta diberikan
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, panggil fungsi cetakPdf()
             //   cetakPdf();
            } else {
                // Izin ditolak, beri tahu pengguna
              //  Toast.makeText(this, "Izin penyimpanan ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
