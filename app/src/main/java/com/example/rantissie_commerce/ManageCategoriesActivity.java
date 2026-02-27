package com.example.rantissie_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.Category;
import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private List<Category> categoryList;
    private DatabaseReference categoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        categoryList = new ArrayList<>();
        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");

        FloatingActionButton fab = findViewById(R.id.fabAddCategory);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddCategoryActivity.class)));

        loadCategories();
    }

    private void loadCategories() {
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    if (category != null) {
                        categoryList.add(category);
                    }
                }

                CategoryAdapter categoryAdapter = new CategoryAdapter(ManageCategoriesActivity.this, categoryList);
                rvCategories.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageCategoriesActivity.this, "Failed to load: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}