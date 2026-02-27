package com.example.rantissie_commerce;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.rantissie_commerce.TrashAdapter;
import com.example.rantissie_commerce.TrashItem;

import java.util.ArrayList;
import java.util.List;

public class RecycleBinActivity extends AppCompatActivity {

    private RecyclerView rvTrashItems;
    private TrashAdapter adapter;
    private List<TrashItem> trashList;
    private DatabaseReference trashRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);

        rvTrashItems = findViewById(R.id.rvTrashItems);
        rvTrashItems.setLayoutManager(new LinearLayoutManager(this));

        trashList = new ArrayList<>();
        trashRef = FirebaseDatabase.getInstance().getReference("Trash");

        loadTrashItems();
    }

    private void loadTrashItems() {
        trashRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trashList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TrashItem trashItem = data.getValue(TrashItem.class);
                    if (trashItem != null) {
                        trashList.add(trashItem);
                    }
                }

                if (trashList.isEmpty()) {
                    Toast.makeText(RecycleBinActivity.this, "Recycle Bin is empty", Toast.LENGTH_SHORT).show();
                }

                adapter = new TrashAdapter(RecycleBinActivity.this, trashList);
                rvTrashItems.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecycleBinActivity.this, "Failed to load Recycle Bin: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}