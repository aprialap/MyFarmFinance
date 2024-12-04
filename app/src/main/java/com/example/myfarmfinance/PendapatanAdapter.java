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

import com.example.myfarmfinance.models.DataPendapatan;

import java.util.List;

public class PendapatanAdapter extends RecyclerView.Adapter<PendapatanAdapter.ViewHolder> {

    private List<DataPendapatan> pendapatanList;
    private Context context;

    public PendapatanAdapter(Context context, List<DataPendapatan> pendapatanList) {
        this.context = context;
        this.pendapatanList = pendapatanList;
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
        holder.tvJenisPendapatan.setText(pendapatan.getJenisPendapatan());
        holder.tvKeterangan.setText(pendapatan.getKeterangan());
        holder.tvTotal.setText(String.format("Rp %.2f", pendapatan.getTotal())); // Format amount as currency

        // Edit button listener
        holder.btnEdit.setOnClickListener(v -> {
            // Handle Edit logic here
            Toast.makeText(context, "Edit clicked for " + pendapatan.getJenisPendapatan(), Toast.LENGTH_SHORT).show();
        });

        // Delete button listener
        holder.btnDelete.setOnClickListener(v -> {
            // Handle Delete logic
            Toast.makeText(context, "Delete clicked for " + pendapatan.getJenisPendapatan(), Toast.LENGTH_SHORT).show();
            pendapatanList.remove(position); // Remove the item from the list
            notifyItemRemoved(position); // Notify adapter about the item removal
        });
    }

    @Override
    public int getItemCount() {
        return pendapatanList.size();
    }

    // ViewHolder to hold references to the views in each item layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvTanggal, tvJenisPendapatan, tvKeterangan, tvTotal;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            tvId = itemView.findViewById(R.id.tvId);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJenisPendapatan = itemView.findViewById(R.id.tvJenisPendapatan);
            tvKeterangan = itemView.findViewById(R.id.tvKeterangan);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
