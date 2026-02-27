package com.example.rantissie_commerce;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.rantissie_commerce.Product;
import com.example.rantissie_commerce.TrashItem;

public class EditProductActivity extends AppCompatActivity {

    private EditText etTitle, etDesc, etPrice;
    private Button btnUpdate, btnDelete;

    private DatabaseReference productsRef, trashRef;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        etTitle = findViewById(R.id.etEditProductTitle);
        etDesc = findViewById(R.id.etEditProductDesc);
        etPrice = findViewById(R.id.etEditProductPrice);
        btnUpdate = findViewById(R.id.btnUpdateProduct);
        btnDelete = findViewById(R.id.btnDeleteProduct);

        productsRef = FirebaseDatabase.getInstance().getReference("Products");
        trashRef = FirebaseDatabase.getInstance().getReference("Trash");

        try {
            selectedProduct = (Product) getIntent().getSerializableExtra("selected_product");

            if (selectedProduct == null) {
                throw new NullPointerException("Product data is missing");
            }

            etTitle.setText(selectedProduct.getTitle());
            etDesc.setText(selectedProduct.getDescription());
            etPrice.setText(String.valueOf(selectedProduct.getPrice()));

        } catch (Exception e) {
            Toast.makeText(this, "Error loading product data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnUpdate.setOnClickListener(v -> updateProduct());
        btnDelete.setOnClickListener(v -> softDeleteProduct());
    }

    private void updateProduct() {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            selectedProduct.setTitle(title);
            selectedProduct.setDescription(desc);
            selectedProduct.setPrice(price);

            productsRef.child(selectedProduct.getId()).setValue(selectedProduct)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Product Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
        }
    }

    private void softDeleteProduct() {
        String trashId = trashRef.push().getKey();

        if (trashId != null) {
            TrashItem trashItem = new TrashItem(trashId, "Products", selectedProduct, System.currentTimeMillis());

            trashRef.child(trashId).setValue(trashItem)
                    .addOnSuccessListener(aVoid -> {
                        productsRef.child(selectedProduct.getId()).removeValue()
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(this, "Product moved to Recycle Bin", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Delete Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}