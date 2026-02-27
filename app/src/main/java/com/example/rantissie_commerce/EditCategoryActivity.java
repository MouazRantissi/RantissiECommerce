package com.example.rantissie_commerce;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.rantissie_commerce.Category;
import com.example.rantissie_commerce.TrashItem;

public class EditCategoryActivity extends AppCompatActivity {

    private EditText etName, etImageUrl;
    private Button btnUpdate, btnDelete;

    private DatabaseReference categoryRef, trashRef;
    private Category selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        etName = findViewById(R.id.etEditCategoryName);
        etImageUrl = findViewById(R.id.etEditCategoryImageUrl);
        btnUpdate = findViewById(R.id.btnUpdateCategory);
        btnDelete = findViewById(R.id.btnDeleteCategory);

        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");
        trashRef = FirebaseDatabase.getInstance().getReference("Trash");

        try {
            selectedCategory = (Category) getIntent().getSerializableExtra("selected_category");

            if (selectedCategory == null) {
                throw new NullPointerException("Category data is missing");
            }

            // Populate existing data
            etName.setText(selectedCategory.getName());
            etImageUrl.setText(selectedCategory.getImageUrl());

        } catch (Exception e) {
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnUpdate.setOnClickListener(v -> updateCategory());
        btnDelete.setOnClickListener(v -> softDeleteCategory());
    }

    private void updateCategory() {
        String name = etName.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedCategory.setName(name);
        selectedCategory.setImageUrl(imageUrl);

        categoryRef.child(selectedCategory.getId()).setValue(selectedCategory)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Category Updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show());
    }

    private void softDeleteCategory() {
        String trashId = trashRef.push().getKey();

        if (trashId != null) {
            TrashItem trashItem = new TrashItem(trashId, "Categories", selectedCategory, System.currentTimeMillis());

            trashRef.child(trashId).setValue(trashItem)
                    .addOnSuccessListener(aVoid -> {
                        categoryRef.child(selectedCategory.getId()).removeValue()
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(this, "Category moved to Recycle Bin", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Delete Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}