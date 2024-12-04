package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfarmfinance.models.User;
import com.example.myfarmfinance.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InsertUserActivity extends AppCompatActivity {
    private EditText etId, etNama, etUsername, etPassword, etFoto;
    private Spinner spinnerRole;
    private Button btnSave;
    private UserAdapter adapter;
    private List<User> userList;
    private UserRepository userRepository;
    int totalUser = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsertUserActivity.this, MasterUserActivity.class);
                startActivity(intent);
            }
        });

        // Inisialisasi View
        etId = findViewById(R.id.etId);
        etNama = findViewById(R.id.etNama);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFoto = findViewById(R.id.etFoto);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnSave = findViewById(R.id.btnSave);

        String userId = getIntent().getStringExtra("user_id");

        // Jika userId tidak null, berarti ini mode edit
        if (userId != null) {
            loadUserData(userId, etNama, etUsername, etPassword, etFoto, spinnerRole);
        }

        // Inisialisasi
        userList = new ArrayList<>();
        userRepository = new UserRepository(this);

        // Menyusun adapter untuk spinner role
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // Listener tombol simpan
        btnSave.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String foto = etFoto.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();
            String Id = etId.getText().toString().trim();

            if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || foto.isEmpty() || role.isEmpty()) {
                Toast.makeText(InsertUserActivity.this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            } else {
                if (Id == null || Id.isEmpty()) {
                    User newUser = new User(null, nama, username, password, foto, role);
                    userRepository.insert(newUser); // Insert new user
                }else{
                    User updateUser = new User(Id, nama, username, password, foto, role);
                    userRepository.update(updateUser); // update user
                }

                resetInputFields();
                Intent intent = new Intent(InsertUserActivity.this, MasterUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void resetInputFields() {
        etNama.setText("");
        etUsername.setText("");
        etPassword.setText("");
        etFoto.setText("");
        spinnerRole.setSelection(0);
    }

    private void loadUserData(String userId, EditText etNama, EditText etUsername, EditText etPassword, EditText etFoto, Spinner spinnerRole) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    etId.setText(user.getId());
                    etNama.setText(user.getNama());
                    etUsername.setText(user.getUsername());
                    etPassword.setText(user.getPassword());
                    etFoto.setText(user.getFoto());
                    if ("User".equals(user.getRole())) {
                        spinnerRole.setSelection(1);
                    } else {
                        spinnerRole.setSelection(0);
                    }
                }
            }
        });
    }

}
