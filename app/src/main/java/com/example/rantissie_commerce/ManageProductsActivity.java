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
import com.example.rantissie_commerce.Product;
import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private List<Product> productList;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        rvProducts = findViewById(R.id.rvCategories); // Reusing the same ID for simplicity
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        FloatingActionButton fab = findViewById(R.id.fabAddCategory);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));

        loadProducts();
    }

    private void loadProducts() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }

                ProductAdapter productAdapter = new ProductAdapter(ManageProductsActivity.this, productList);
                rvProducts.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageProductsActivity.this, "Failed to load: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}