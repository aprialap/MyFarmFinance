package com.example.myfarmfinance.repositories;

import android.content.Context;
import android.widget.Toast;

import com.example.myfarmfinance.models.DataPengeluaran;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataPengeluaranRepository {
    private final DatabaseReference databaseReference;
    private final Context context;

    public DataPengeluaranRepository(Context context) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("data_pengeluaran");
        this.context = context;
    }

    // Menyisipkan data baru ke Firebase
    public void insert(DataPengeluaran dataPengeluaran) {
        String generatedId = databaseReference.push().getKey();
        if (generatedId != null) {
            dataPengeluaran.setId(generatedId);
            databaseReference.child(generatedId).setValue(dataPengeluaran)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(context, "Data pengeluaran berhasil disimpan!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(context, "Gagal membuat ID data pengeluaran", Toast.LENGTH_SHORT).show();
        }
    }

    // Mengambil daftar data pengeluaran, diurutkan berdasarkan tanggal (descending)
    public void getList(String orderByField, OnListFetchedListener listener) {
        databaseReference.orderByChild(orderByField).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DataPengeluaran> dataPengeluaranList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataPengeluaran dataPengeluaran = snapshot.getValue(DataPengeluaran.class);
                    if (dataPengeluaran != null) {
                        dataPengeluaranList.add(dataPengeluaran);
                    }
                }

                // Mengurutkan berdasarkan tanggal dalam format descending
                Collections.sort(dataPengeluaranList, (o1, o2) -> {
                    try {
                        Date date1 = parseDate(o1.getTanggal());
                        Date date2 = parseDate(o2.getTanggal());
                        return date2.compareTo(date1); // Urutkan menurun
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });

                listener.onFetched(dataPengeluaranList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Gagal mengambil data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menghapus data berdasarkan ID
    public void delete(String id, OnDeletedListener listener) {
        databaseReference.child(id).removeValue()
                .addOnSuccessListener(aVoid -> listener.onDeleted())
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    // Memperbarui data berdasarkan ID
    public void update(DataPengeluaran dataPengeluaran) {
        if (dataPengeluaran.getId() != null) {
            databaseReference.child(dataPengeluaran.getId()).setValue(dataPengeluaran)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(context, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Gagal memperbarui data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(context, "ID tidak ditemukan, tidak dapat memperbarui data", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi utilitas untuk mem-parse tanggal
    private Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.parse(dateString);
    }

    // Interface untuk callback pengambilan data
    public interface OnListFetchedListener {
        void onFetched(List<DataPengeluaran> fetched);
    }

    // Interface untuk callback penghapusan data
    public interface OnDeletedListener {
        void onDeleted();

        void onFailure(String errorMessage);
    }
}
