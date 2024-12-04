package com.example.myfarmfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.User;
import com.example.myfarmfinance.repositories.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MasterUserActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private List<User> userList;
    private UserRepository userRepository;

    private FloatingActionButton fabAdd; // FloatingActionButton declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_user);

        // Initialize RecyclerView
        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        // Initialize FloatingActionButton
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddUserActivity when the FAB is clicked
                Intent intent = new Intent(MasterUserActivity.this, InsertUserActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterUserActivity.this, MasterDasboardActivity.class);
                startActivity(intent);
            }
        });

        // Inisialisasi
        userList = new ArrayList<>();
        userRepository = new UserRepository(this);

        adapter = new UserAdapter(this, userList, userRepository);
        rvUsers.setAdapter(adapter);

        // Fetch data from Firebase
       fetchUsersFromFirebase();
    }

    private void fetchUsersFromFirebase() {
        UserRepository userRepository = new UserRepository(this);
        userRepository.getList("Nama",new UserRepository.OnUserListFetchedListener() {
            @Override
            public void onFetched(List<User> fetchedUsers) {
                // Clear the existing list and add the fetched users
                userList.clear();
                userList.addAll(fetchedUsers);

                // Notify adapter about data changes
                adapter.notifyDataSetChanged();
            }
        });
    }


}
