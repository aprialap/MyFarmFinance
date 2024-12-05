package com.example.myfarmfinance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.DataPendapatan;
import com.example.myfarmfinance.repositories.DataPendapatanRepository;
import com.example.myfarmfinance.repositories.JenisPendapatanRepository;
import com.example.myfarmfinance.repositories.UserRepository;

import java.util.List;

public class PendapatanAdapter extends RecyclerView.Adapter<PendapatanAdapter.ViewHolder> {

    private List<DataPendapatan> pendapatanList;
    private Context context;
    private DataPendapatanRepository dataPendapatanRepository;
    public PendapatanAdapter(Context context, List<DataPendapatan> pendapatanList, DataPendapatanRepository dataPendapatanRepository) {
        this.context = context;
        this.pendapatanList = pendapatanList;
        this.dataPendapatanRepository = dataPendapatanRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.item_data_pendapatan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataPendapatan pendapatan = pendapatanList.get(position);
        holder.tvId.setText(String.valueOf(pendapatan.getId()));
        holder.tvTanggal.setText(pendapatan.getTanggal());
        holder.tvJenisPendapatan.setText(pendapatan.getJenisPendapatanNama());
        holder.tvSumberPendapatan.setText(pendapatan.getSumberPendapatan());
        holder.tvTotal.setText(String.format("Rp %.2f", pendapatan.getTotal())); // Format amount as currency

        // Edit button listener
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, InsertDataPendapatanActivity.class);
            intent.putExtra("id", pendapatan.getId()); // Kirim ID
            context.startActivity(intent);
        });

        // Delete button listener
        holder.btnDelete.setOnClickListener(v -> {
            // Menghapus data dari Firebase
            dataPendapatanRepository.delete(pendapatan.getId(), new DataPendapatanRepository.OnDeletedListener() {
                @Override
                public void onDeleted() {
                    // Setelah data berhasil dihapus dari Firebase, hapus dari userList
                    pendapatanList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Data berhasil dihapus.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Menangani kesalahan saat penghapusan gagal
                    Toast.makeText(context, "Gagal menghapus data: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return pendapatanList.size();
    }

    // ViewHolder to hold references to the views in each item layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvTanggal, tvJenisPendapatan, tvSumberPendapatan, tvTotal;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            tvId = itemView.findViewById(R.id.tvId);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJenisPendapatan = itemView.findViewById(R.id.tvJenisPendapatan);
            tvSumberPendapatan = itemView.findViewById(R.id.tvSumberPendapatan);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
