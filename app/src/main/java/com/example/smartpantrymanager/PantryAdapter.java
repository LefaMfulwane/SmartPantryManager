package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItemList;
    private OnItemDeleteListener deleteListener;
    private OnItemEditListener editListener;

    public interface OnItemDeleteListener {
        void onDelete(int id);
    }

    public interface OnItemEditListener {
        void onEdit(PantryItem item);
    }

    public PantryAdapter(List<PantryItem> pantryItemList, OnItemDeleteListener deleteListener, OnItemEditListener editListener) {
        this.pantryItemList = pantryItemList;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItemList.get(position);
        holder.textName.setText(item.getName());

        String expiryText = (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) ? "Not provided" : item.getExpiryDate();
        holder.textDetails.setText("Quantity: " + item.getQuantity() + " " + item.getUnit() + " | Expiry: " + expiryText);

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(item.getId());
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEdit(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItemList.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDetails;
        Button btnDelete, btnEdit;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textPantryName);
            textDetails = itemView.findViewById(R.id.textPantryDetails);
            btnDelete = itemView.findViewById(R.id.btnDeletePantry);
            btnEdit = itemView.findViewById(R.id.button_edit);
        }
    }
}