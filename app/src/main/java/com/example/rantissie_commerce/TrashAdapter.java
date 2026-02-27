package com.example.rantissie_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.TrashItem;

import java.util.List;
import java.util.Map;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.TrashViewHolder> {

    private Context context;
    private List<TrashItem> trashList;
    private DatabaseReference rootRef;

    public TrashAdapter(Context context, List<TrashItem> trashList) {
        this.context = context;
        this.trashList = trashList;
        this.rootRef = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public TrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trash, parent, false);
        return new TrashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrashViewHolder holder, int position) {
        TrashItem trashItem = trashList.get(position);

        holder.tvTrashNode.setText("Original Location: " + trashItem.getOriginalNode());

        try {
            Map<String, Object> itemDataMap = (Map<String, Object>) trashItem.getItemData();
            String displayName = "Unknown Item";

            if (itemDataMap.containsKey("title")) {
                displayName = (String) itemDataMap.get("title");
            } else if (itemDataMap.containsKey("name")) {
                displayName = (String) itemDataMap.get("name");
            }

            holder.tvTrashItemName.setText(displayName);

        } catch (Exception e) {
            holder.tvTrashItemName.setText("Data parsing error");
        }

        holder.btnRestore.setOnClickListener(v -> restoreItem(trashItem));

        holder.btnPermanentDelete.setOnClickListener(v -> permanentDelete(trashItem));
    }

    private void restoreItem(TrashItem trashItem) {
        try {
            Map<String, Object> itemDataMap = (Map<String, Object>) trashItem.getItemData();
            String originalId = (String) itemDataMap.get("id");

            if (originalId == null) {
                Toast.makeText(context, "Cannot restore: Missing original ID", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference originalRef = rootRef.child(trashItem.getOriginalNode()).child(originalId);
            DatabaseReference trashRef = rootRef.child("Trash").child(trashItem.getTrashId());

            originalRef.setValue(itemDataMap)
                    .addOnSuccessListener(aVoid -> {
                        trashRef.removeValue()
                                .addOnSuccessListener(aVoid1 -> Toast.makeText(context, "Item Restored Successfully", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Restoration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        } catch (ClassCastException e) {
            Toast.makeText(context, "Error reading trash data format", Toast.LENGTH_SHORT).show();
        }
    }

    private void permanentDelete(TrashItem trashItem) {
        DatabaseReference trashRef = rootRef.child("Trash").child(trashItem.getTrashId());

        trashRef.removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Permanently Deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Delete Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return trashList.size();
    }

    public static class TrashViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrashNode, tvTrashItemName;
        Button btnRestore, btnPermanentDelete;

        public TrashViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrashNode = itemView.findViewById(R.id.tvTrashNode);
            tvTrashItemName = itemView.findViewById(R.id.tvTrashItemName);
            btnRestore = itemView.findViewById(R.id.btnRestore);
            btnPermanentDelete = itemView.findViewById(R.id.btnPermanentDelete);
        }
    }
}