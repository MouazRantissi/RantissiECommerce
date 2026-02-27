package com.example.rantissie_commerce;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.rantissie_commerce.Category;
import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.Product;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private EditText etTitle, etDesc, etPrice, etImageUrl;
    private Spinner spinnerCategory;
    private DatabaseReference productsRef, categoriesRef;
    private List<String> categoryNames;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etTitle = findViewById(R.id.etProductTitle);
        etDesc = findViewById(R.id.etProductDescription);
        etPrice = findViewById(R.id.etProductPrice);
        etImageUrl = findViewById(R.id.etProductImageUrl);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnSave = findViewById(R.id.btnSaveProduct);

        productsRef = FirebaseDatabase.getInstance().getReference("Products");
        categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");

        categoryNames = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        loadCategoriesIntoSpinner();

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void loadCategoriesIntoSpinner() {
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryNames.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    if (category != null) {
                        categoryNames.add(category.getName());
                    }
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddProductActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProduct() {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        if (spinnerCategory.getSelectedItem() == null) {
            Toast.makeText(this, "Please create a category first", Toast.LENGTH_SHORT).show();
            return;
        }
        String selectedCategory = spinnerCategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = productsRef.push().getKey();
        if (id != null) {
            Product product = new Product(id, title, desc, price, imageUrl, selectedCategory);
            productsRef.child(id).setValue(product)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}