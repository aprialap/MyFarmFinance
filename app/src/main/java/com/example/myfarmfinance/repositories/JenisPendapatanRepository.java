package com.example.myfarmfinance.repositories;

import android.content.Context;
import android.widget.Toast;

import com.example.myfarmfinance.models.JenisPendapatan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JenisPendapatanRepository {
    private final DatabaseReference databaseReference;
    private final Context context;

    public JenisPendapatanRepository(Context context) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("jenis_pendapatan");
        this.context = context;
    }


    // Method for adding a new data to Firebase
    public void insert(JenisPendapatan jenisPendapatan) {
        String userId = databaseReference.push().getKey();
        if (userId != null) {
            jenisPendapatan.setId(userId);
            databaseReference.child(userId).setValue(jenisPendapatan)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Data jenis pendapatan berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal menyimpan data jenis pendapatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Gagal menghasilkan ID jenis pendapatan", Toast.LENGTH_SHORT).show();
        }
    }

    // Method for fetching data list from Firebase, ordered by a field
    public void getList(String orderByField, JenisPendapatanRepository.OnListFetchedListener listener) {
        databaseReference.orderByChild(orderByField).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<JenisPendapatan> jenisPendapatanList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    JenisPendapatan jenisPendapatan = snapshot.getValue(JenisPendapatan.class);
                    if (jenisPendapatan != null) {
                        jenisPendapatanList.add(jenisPendapatan);
                    }
                }
                listener.onFetched(jenisPendapatanList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Gagal mengambil daftar jenis pendapatan: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menghapus pengguna berdasarkan ID
    public void delete(String Id, final JenisPendapatanRepository.OnDeletedListener listener) {
        databaseReference.child(Id).removeValue()
                .addOnSuccessListener(aVoid -> listener.onDeleted())  // Menghapus berhasil
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));  // Jika gagal
    }

    public void update(JenisPendapatan jenisPendapatan) {
        if (jenisPendapatan.getId() != null) {
            databaseReference.child(jenisPendapatan.getId()).setValue(jenisPendapatan)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Data jenis pendapatan berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal memperbarui data jenis pendapatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "ID jenis pendapatan tidak ditemukan, tidak dapat memperbarui data", Toast.LENGTH_SHORT).show();
        }
    }

    // Interface for fetching user list callback
    public interface OnListFetchedListener {
        void onFetched(List<JenisPendapatan> fetched);
    }

    public interface OnDeletedListener {
        void onDeleted();  // Callback saat penghapusan berhasil
        void onFailure(String errorMessage);  // Callback saat penghapusan gagal
    }

}
