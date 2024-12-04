package com.example.myfarmfinance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.User;
import com.example.myfarmfinance.repositories.UserRepository;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;
    private UserRepository userRepository;

    public UserAdapter(Context context, List<User> userList, UserRepository userRepository) {
        this.context = context;
        this.userList = userList;
        this.userRepository = userRepository;  // Inisialisasi UserRepository
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvNama.setText(user.getNama());
        holder.tvId.setText(user.getId());

        // Edit button listener
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, InsertUserActivity.class);
            intent.putExtra("user_id", user.getId()); // Kirim ID user
            context.startActivity(intent);
        });

        // Delete button listener
        holder.btnDelete.setOnClickListener(v -> {
            // Menghapus data dari Firebase
            userRepository.delete(user.getId(), new UserRepository.OnUserDeletedListener() {
                @Override
                public void onDeleted() {
                    // Setelah data berhasil dihapus dari Firebase, hapus dari userList
                    userList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Data pengguna berhasil dihapus.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Menangani kesalahan saat penghapusan gagal
                    Toast.makeText(context, "Gagal menghapus data pengguna: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNama;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNama = itemView.findViewById(R.id.tvNama);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
