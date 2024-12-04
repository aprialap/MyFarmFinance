package com.example.myfarmfinance;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.JenisPengeluaran;
import com.example.myfarmfinance.repositories.JenisPengeluaranRepository;

import java.util.List;

public class JenisPengeluaranAdapter extends RecyclerView.Adapter<JenisPengeluaranAdapter.ViewHolder> {

    private List<JenisPengeluaran> jenisPengeluaranList;
    private Context context;
    private JenisPengeluaranRepository jenisPengeluaranRepository;

    public JenisPengeluaranAdapter(Context context, List<JenisPengeluaran> jenisPengeluaranList, JenisPengeluaranRepository jenisPengeluaranRepository) {
        this.context = context;
        this.jenisPengeluaranList = jenisPengeluaranList;
        this.jenisPengeluaranRepository = jenisPengeluaranRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_jenis_pengeluaran, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JenisPengeluaran jenisPengeluaran = jenisPengeluaranList.get(position);
        holder.tvId.setText(String.valueOf(jenisPengeluaran.getId()));
        holder.tvNama.setText(jenisPengeluaran.getNama());
        // Edit button listener
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, InsertJenisPengeluaranActivity.class);
            intent.putExtra("id", jenisPengeluaran.getId()); // Kirim ID
            context.startActivity(intent);
        });

        // Delete button listener
        holder.btnDelete.setOnClickListener(v -> {
            jenisPengeluaranRepository.delete(jenisPengeluaran.getId(), new JenisPengeluaranRepository.OnDeletedListener() {
                @Override
                public void onDeleted() {
                    // Setelah data berhasil dihapus dari Firebase, hapus dari userList
                    jenisPengeluaranList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Data berhasil dihapus.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Menangani kesalahan saat penghapusan gagal
                    Toast.makeText(context, "Gagal menghapus data : " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return jenisPengeluaranList.size();
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
