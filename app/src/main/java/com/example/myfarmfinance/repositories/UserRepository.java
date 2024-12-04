package com.example.myfarmfinance.repositories;

import android.content.Context;
import android.widget.Toast;

import com.example.myfarmfinance.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final DatabaseReference databaseReference;
    private final Context context;

    public UserRepository(Context context) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("users");
        this.context = context;
    }

    // Method for adding a new user to Firebase
    public void insert(User user) {
        String userId = databaseReference.push().getKey();
        if (userId != null) {
            user.setId(userId);
            databaseReference.child(userId).setValue(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Data user berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal menyimpan data user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Gagal menghasilkan ID user", Toast.LENGTH_SHORT).show();
        }
    }

    // Method for fetching user list from Firebase, ordered by a field
    public void getList(String orderByField, OnUserListFetchedListener listener) {
        databaseReference.orderByChild(orderByField).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                listener.onFetched(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Gagal mengambil daftar user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menghapus pengguna berdasarkan ID
    public void delete(String userId, final OnUserDeletedListener listener) {
        databaseReference.child(userId).removeValue()
                .addOnSuccessListener(aVoid -> listener.onDeleted())  // Menghapus berhasil
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));  // Jika gagal
    }

    public void update(User user) {
        if (user.getId() != null) {
            databaseReference.child(user.getId()).setValue(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Data user berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal memperbarui data user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "ID user tidak ditemukan, tidak dapat memperbarui data", Toast.LENGTH_SHORT).show();
        }
    }

    // Interface for fetching user list callback
    public interface OnUserListFetchedListener {
        void onFetched(List<User> fetchedUsers);
    }

    public interface OnUserDeletedListener {
        void onDeleted();  // Callback saat penghapusan berhasil
        void onFailure(String errorMessage);  // Callback saat penghapusan gagal
    }


}
