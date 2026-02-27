package com.example.rantissie_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.rantissie_commerce.Product;
import com.example.rantissie_commerce.CartManager;


import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvTitle, tvPrice, tvDescription;
    private Button btnAddToCart;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivImage = findViewById(R.id.ivDetailImage);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        try {
            selectedProduct = (Product) getIntent().getSerializableExtra("selected_product");

            if (selectedProduct == null) {
                throw new NullPointerException("Product data is missing");
            }

            tvTitle.setText(selectedProduct.getTitle());
            tvDescription.setText(selectedProduct.getDescription());
            tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", selectedProduct.getPrice()));

            if (selectedProduct.getImageUrl() != null && !selectedProduct.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(selectedProduct.getImageUrl())
                        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                        .error(android.R.drawable.stat_notify_error)
                        .into(ivImage);
            }


        } catch (Exception e) {
            Toast.makeText(this, "Error loading product: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnAddToCart.setOnClickListener(v -> {

            CartManager.getInstance().addProduct(selectedProduct);
            Toast.makeText(this, "Added to Cart!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);

            finish();
        });
    }
}