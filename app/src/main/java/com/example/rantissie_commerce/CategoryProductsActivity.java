package com.example.rantissie_commerce;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.rantissie_commerce.ProductAdapter;
import com.example.rantissie_commerce.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryProductsActivity extends AppCompatActivity {

    private TextView tvCategoryName;
    private RecyclerView rvFilteredProducts;

    private ProductAdapter productAdapter;
    private List<Product> filteredList;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        tvCategoryName = findViewById(R.id.tvFilteredCategoryName);
        rvFilteredProducts = findViewById(R.id.rvFilteredProducts);
        rvFilteredProducts.setLayoutManager(new LinearLayoutManager(this));

        filteredList = new ArrayList<>();
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        String categoryName = getIntent().getStringExtra("category_name");

        if (categoryName != null && !categoryName.isEmpty()) {
            tvCategoryName.setText(categoryName);
            loadFilteredProducts(categoryName);
        } else {
            Toast.makeText(this, "Category not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadFilteredProducts(String categoryName) {
        Query filterQuery = productsRef.orderByChild("category").equalTo(categoryName);

        filterQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filteredList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Product product = data.getValue(Product.class);
                    if (product != null) {
                        filteredList.add(product);
                    }
                }

                if (filteredList.isEmpty()) {
                    Toast.makeText(CategoryProductsActivity.this, "No products in this category yet.", Toast.LENGTH_SHORT).show();
                }

                productAdapter = new ProductAdapter(CategoryProductsActivity.this, filteredList);
                rvFilteredProducts.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryProductsActivity.this, "Filter failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}