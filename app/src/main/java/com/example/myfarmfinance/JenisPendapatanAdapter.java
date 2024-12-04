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

import com.example.myfarmfinance.models.JenisPendapatan;
import com.example.myfarmfinance.repositories.JenisPendapatanRepository;

import java.util.List;

public class JenisPendapatanAdapter extends RecyclerView.Adapter<JenisPendapatanAdapter.ViewHolder> {

    private List<JenisPendapatan> jenisPendapatanList;
    private Context context;
    private JenisPendapatanRepository jenisPendapatanRepository;

    public JenisPendapatanAdapter(Context context, List<JenisPendapatan> jenisPendapatanList, JenisPendapatanRepository jenisPendapatanRepository) {
        this.context = context;
        this.jenisPendapatanList = jenisPendapatanList;
        this.jenisPendapatanRepository = jenisPendapatanRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_jenis_pendapatan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JenisPendapatan jenisPendapatan = jenisPendapatanList.get(position);
        holder.tvId.setText(String.valueOf(jenisPendapatan.getId()));
        holder.tvNama.setText(jenisPendapatan.getNama());
        // Edit button listener
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, InsertJenisPendapatanActivity.class);
            intent.putExtra("id", jenisPendapatan.getId()); // Kirim ID
            context.startActivity(intent);
        });

        // Delete button listener
        holder.btnDelete.setOnClickListener(v -> {

            jenisPendapatanRepository.delete(jenisPendapatan.getId(), new JenisPendapatanRepository.OnDeletedListener() {
                @Override
                public void onDeleted() {
                    // Setelah data berhasil dihapus dari Firebase, hapus dari userList
                    jenisPendapatanList.remove(position);
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
        return jenisPendapatanList.size();
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
