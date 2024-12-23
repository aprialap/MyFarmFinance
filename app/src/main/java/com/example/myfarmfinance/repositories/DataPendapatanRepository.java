package com.example.myfarmfinance.repositories;

import android.content.Context;
import android.widget.Toast;

import com.example.myfarmfinance.models.DataPendapatan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataPendapatanRepository {
    private final DatabaseReference databaseReference;
    private final Context context;

    public DataPendapatanRepository(Context context) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("data_pendapatan");
        this.context = context;
    }

    // Menyisipkan data baru ke Firebase
    public void insert(DataPendapatan dataPendapatan) {
        String generatedId = databaseReference.push().getKey();
        if (generatedId != null) {
            dataPendapatan.setId(generatedId);
            databaseReference.child(generatedId).setValue(dataPendapatan)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(context, "Data pendapatan berhasil disimpan!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(context, "Gagal membuat ID data pendapatan", Toast.LENGTH_SHORT).show();
        }
    }

    // Mengambil daftar data pendapatan, diurutkan berdasarkan tanggal (descending)
    public void getList(String orderByField, OnListFetchedListener listener) {
        databaseReference.orderByChild(orderByField).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DataPendapatan> dataPendapatanList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataPendapatan dataPendapatan = snapshot.getValue(DataPendapatan.class);
                    if (dataPendapatan != null) {
                        dataPendapatanList.add(dataPendapatan);
                    }
                }

                // Mengurutkan berdasarkan tanggal dalam format descending
                Collections.sort(dataPendapatanList, (o1, o2) -> {
                    try {
                        Date date1 = parseDate(o1.getTanggal());
                        Date date2 = parseDate(o2.getTanggal());
                        return date2.compareTo(date1); // Urutkan menurun
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });

                listener.onFetched(dataPendapatanList);
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
    public void update(DataPendapatan dataPendapatan) {
        if (dataPendapatan.getId() != null) {
            databaseReference.child(dataPendapatan.getId()).setValue(dataPendapatan)
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
        void onFetched(List<DataPendapatan> fetched);
    }

    // Interface untuk callback penghapusan data
    public interface OnDeletedListener {
        void onDeleted();

        void onFailure(String errorMessage);
    }
}
