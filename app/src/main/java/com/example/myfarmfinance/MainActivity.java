package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Menghubungkan View dengan ID
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Menambahkan event click pada tombol login
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi input username dan password
            if (TextUtils.isEmpty(username)) {
                etUsername.setError("Username tidak boleh kosong!");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password tidak boleh kosong!");
                return;
            }

            // Cek kredensial login ke Firebase
            isValidLogin(username, password, new LoginCallback() {
                @Override
                public void onLoginSuccess() {
                    // Arahkan ke Dashboard jika login berhasil
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish(); // Menutup activity login
                }

                @Override
                public void onLoginFailed(String errorMessage) {
                    // Tampilkan pesan kesalahan jika login gagal
                    // Misalnya, menggunakan dialog atau UI lain untuk menampilkan errorMessage
                    etPassword.setError(errorMessage); // Set error ke password
                }
            });
        });
    }

    private void isValidLogin(final String username, final String password, final LoginCallback callback) {
        // Mendapatkan referensi ke Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Mengecek apakah username dan password valid dengan Firebase
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Ambil data password dari Firebase
                        String storedPassword = snapshot.child("password").getValue(String.class);

                        // Cek password
                        if (password.equals(storedPassword)) {
                            // Login berhasil, panggil callback onLoginSuccess
                            callback.onLoginSuccess();
                            return; // Keluar setelah login berhasil
                        } else {
                            // Password salah, panggil callback onLoginFailed
                            callback.onLoginFailed("Password salah");
                            return; // Keluar setelah password salah
                        }
                    }
                } else {
                    // Username tidak ditemukan, panggil callback onLoginFailed
                    callback.onLoginFailed("Username tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tangani error jika terjadi
                callback.onLoginFailed("Terjadi kesalahan, coba lagi");
            }
        });
    }

    // Interface callback untuk hasil login
    public interface LoginCallback {
        void onLoginSuccess();
        void onLoginFailed(String errorMessage);
    }
}
