package com.example.myfarmfinance.repositories;

import android.content.Context;
import android.widget.Toast;

import com.example.myfarmfinance.models.JenisPengeluaran;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JenisPengeluaranRepository {
    private final DatabaseReference databaseReference;
    private final Context context;

    public JenisPengeluaranRepository(Context context) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("jenis_pengeluaran");
        this.context = context;
    }


    // Method for adding a new data to Firebase
    public void insert(JenisPengeluaran jenisPengeluaran) {
        String userId = databaseReference.push().getKey();
        if (userId != null) {
            jenisPengeluaran.setId(userId);
            databaseReference.child(userId).setValue(jenisPengeluaran)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Data jenis pengeluaran berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal menyimpan data jenis pengeluaran: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Gagal menghasilkan ID jenis pengeluaran", Toast.LENGTH_SHORT).show();
        }
    }

    // Method for fetching  data list from Firebase, ordered by a field
    public void getList(String orderByField, JenisPengeluaranRepository.OnListFetchedListener listener) {
        databaseReference.orderByChild(orderByField).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<JenisPengeluaran> jenisPengeluaranList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    JenisPengeluaran jenisPengeluaran = snapshot.getValue(JenisPengeluaran.class);
                    if (jenisPengeluaran != null) {
                        jenisPengeluaranList.add(jenisPengeluaran);
                    }
                }
                listener.onFetched(jenisPengeluaranList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Gagal mengambil daftar jenis pengeluaran: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menghapus pengguna berdasarkan ID
    public void delete(String Id, final JenisPengeluaranRepository.OnDeletedListener listener) {
        databaseReference.child(Id).removeValue()
                .addOnSuccessListener(aVoid -> listener.onDeleted())  // Menghapus berhasil
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));  // Jika gagal
    }

    public void update(JenisPengeluaran jenisPengeluaran) {
        if (jenisPengeluaran.getId() != null) {
            databaseReference.child(jenisPengeluaran.getId()).setValue(jenisPengeluaran)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Data jenis pengeluaran berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal memperbarui data jenis pengeluaran: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "ID jenis pengeluaran tidak ditemukan, tidak dapat memperbarui data", Toast.LENGTH_SHORT).show();
        }
    }

    // Interface for fetching user list callback
    public interface OnListFetchedListener {
        void onFetched(List<JenisPengeluaran> fetched);
    }

    public interface OnDeletedListener {
        void onDeleted();  // Callback saat penghapusan berhasil
        void onFailure(String errorMessage);  // Callback saat penghapusan gagal
    }

}
