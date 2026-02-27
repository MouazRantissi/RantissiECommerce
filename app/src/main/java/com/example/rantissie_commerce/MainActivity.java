package com.example.rantissie_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
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
import com.example.rantissie_commerce.CategoryAdapter;
import com.example.rantissie_commerce.ProductAdapter;
import com.example.rantissie_commerce.Category;
import com.example.rantissie_commerce.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCategories, rvProducts;
    private FloatingActionButton fabCart;
    private ImageView btnProfile;

    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private List<Category> categoryList;
    private List<Product> productList;

    private DatabaseReference categoryRef, productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCategories = findViewById(R.id.rvMainCategories);
        rvProducts = findViewById(R.id.rvMainProducts);
        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        btnProfile = findViewById(R.id.btnProfile);

        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        categoryList = new ArrayList<>();
        productList = new ArrayList<>();

        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        btnProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        loadCategories();
        loadAllProducts();
    }

    private void loadCategories() {
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    if (category != null) {
                        categoryList.add(category);
                    }
                }
                categoryAdapter = new CategoryAdapter(MainActivity.this, categoryList);
                rvCategories.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error loading categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllProducts() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Product product = data.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productAdapter = new ProductAdapter(MainActivity.this, productList);
                rvProducts.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error loading products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}