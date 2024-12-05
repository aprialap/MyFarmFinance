package com.example.myfarmfinance.repositories;

import android.content.Context;
import android.widget.Toast;

import com.example.myfarmfinance.models.DataPendapatan;
import com.example.myfarmfinance.models.JenisPendapatan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataPendapatanRepository {
    private final DatabaseReference databaseReference;
    private final Context context;

    public DataPendapatanRepository(Context context) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("data_pendapatan");
        this.context = context;
    }

    // Method for adding a new data to Firebase
    public void insert(DataPendapatan dataPendapatan) {
        String userId = databaseReference.push().getKey();
        if (userId != null) {
            dataPendapatan.setId(userId);
            databaseReference.child(userId).setValue(dataPendapatan)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Data pendapatan berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal menyimpan data pendapatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Gagal menghasilkan ID data pendapatan", Toast.LENGTH_SHORT).show();
        }
    }

    // Method for fetching data list from Firebase, ordered by a field
    public void getList(String orderByField, DataPendapatanRepository.OnListFetchedListener listener) {
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
                Collections.reverse(dataPendapatanList);
                listener.onFetched(dataPendapatanList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Gagal mengambil daftar data pendapatan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menghapus pengguna berdasarkan ID
    public void delete(String Id, final DataPendapatanRepository.OnDeletedListener listener) {
        databaseReference.child(Id).removeValue()
                .addOnSuccessListener(aVoid -> listener.onDeleted())  // Menghapus berhasil
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));  // Jika gagal
    }

    public void update(DataPendapatan dataPendapatan) {
        if (dataPendapatan.getId() != null) {
            databaseReference.child(dataPendapatan.getId()).setValue(dataPendapatan)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Data pendapatan berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal memperbarui data pendapatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "ID pendapatan tidak ditemukan, tidak dapat memperbarui data", Toast.LENGTH_SHORT).show();
        }
    }

    // Interface for fetching user list callback
    public interface OnListFetchedListener {
        void onFetched(List<DataPendapatan> fetched);
    }

    public interface OnDeletedListener {
        void onDeleted();  // Callback saat penghapusan berhasil
        void onFailure(String errorMessage);  // Callback saat penghapusan gagal
    }
}
