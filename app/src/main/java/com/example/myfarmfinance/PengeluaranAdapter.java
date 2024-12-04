package com.example.myfarmfinance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfarmfinance.models.DataPengeluaran;

import java.util.List;

public class PengeluaranAdapter extends RecyclerView.Adapter<PengeluaranAdapter.ViewHolder> {

    private List<DataPengeluaran> pengeluaranList;
    private Context context;

    public PengeluaranAdapter(Context context, List<DataPengeluaran> pengeluaranList) {
        this.context = context;
        this.pengeluaranList = pengeluaranList;
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
        holder.tvJenisPengeluaran.setText(pengeluaran.getJenisPengeluaran());
        holder.tvSumberPengeluaran.setText(pengeluaran.getSumberPengeluaran());
        holder.tvTotal.setText(String.format("Rp %.2f", pengeluaran.getTotal())); // Format amount as currency

        // Edit button listener
        holder.btnEdit.setOnClickListener(v -> {
            // Handle Edit logic here
            Toast.makeText(context, "Edit clicked for " + pengeluaran.getJenisPengeluaran(), Toast.LENGTH_SHORT).show();
        });

        // Delete button listener
        holder.btnDelete.setOnClickListener(v -> {
            // Handle Delete logic
            Toast.makeText(context, "Delete clicked for " + pengeluaran.getJenisPengeluaran(), Toast.LENGTH_SHORT).show();
            pengeluaranList.remove(position); // Remove the item from the list
            notifyItemRemoved(position); // Notify adapter about the item removal
        });
    }

    @Override
    public int getItemCount() {
        return pengeluaranList.size();
    }

    // ViewHolder to hold references to the views in each item layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvTanggal, tvJenisPengeluaran, tvSumberPengeluaran, tvTotal;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            tvId = itemView.findViewById(R.id.tvId);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJenisPengeluaran = itemView.findViewById(R.id.tvJenisPengeluaran);
            tvSumberPengeluaran = itemView.findViewById(R.id.tvSumberPengeluaran);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
