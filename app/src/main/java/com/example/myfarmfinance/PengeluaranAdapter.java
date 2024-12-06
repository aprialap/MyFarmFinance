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
import com.example.myfarmfinance.models.DataPengeluaran;
import com.example.myfarmfinance.repositories.DataPendapatanRepository;
import com.example.myfarmfinance.repositories.DataPengeluaranRepository;

import java.util.List;

public class PengeluaranAdapter extends RecyclerView.Adapter<PengeluaranAdapter.ViewHolder> {

    private List<DataPengeluaran> pengeluaranList;
    private Context context;
    private DataPengeluaranRepository dataPengeluaranRepository;
    public PengeluaranAdapter(Context context, List<DataPengeluaran> pengeluaranList, DataPengeluaranRepository dataPengeluaranRepository) {
        this.context = context;
        this.pengeluaranList = pengeluaranList;
        this.dataPengeluaranRepository = dataPengeluaranRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.item_data_pengeluaran, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataPengeluaran pengeluaran = pengeluaranList.get(position);
        holder.tvId.setText(String.valueOf(pengeluaran.getId()));
        holder.tvTanggal.setText(pengeluaran.getTanggal());
        holder.tvJenisPengeluaran.setText(pengeluaran.getJenisPengeluaranNama());
        holder.tvKeterangan.setText(pengeluaran.getKeterangan());
        holder.tvJumlahPengeluaran.setText(String.format("Rp %.2f", pengeluaran.getJumlahPengeluaran())); // Format amount as currency

        // Edit button listener
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, InsertDataPengeluaranActivity.class);
            intent.putExtra("id", pengeluaran.getId()); // Kirim ID
            context.startActivity(intent);
        });

        // Delete button listener
        holder.btnDelete.setOnClickListener(v -> {
            // Menghapus data dari Firebase
            dataPengeluaranRepository.delete(pengeluaran.getId(), new DataPengeluaranRepository.OnDeletedListener() {
                @Override
                public void onDeleted() {
                    // Setelah data berhasil dihapus dari Firebase, hapus dari userList
                    pengeluaranList.remove(position);
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
        return pengeluaranList.size();
    }

    // ViewHolder to hold references to the views in each item layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvTanggal, tvJenisPengeluaran, tvKeterangan, tvJumlahPengeluaran;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            tvId = itemView.findViewById(R.id.tvId);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJenisPengeluaran = itemView.findViewById(R.id.tvJenisPengeluaran);
            tvKeterangan = itemView.findViewById(R.id.tvKeterangan);
            tvJumlahPengeluaran = itemView.findViewById(R.id.tvJumlahPengeluaran);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
