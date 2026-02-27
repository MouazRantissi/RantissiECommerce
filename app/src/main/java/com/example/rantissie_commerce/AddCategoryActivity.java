package com.example.rantissie_commerce;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.Category;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText etName, etImageUrl;
    private DatabaseReference categoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        etName = findViewById(R.id.etCategoryName);
        etImageUrl = findViewById(R.id.etCategoryImageUrl);
        Button btnSave = findViewById(R.id.btnSaveCategory);

        categoryRef = FirebaseDatabase.getInstance().getReference("Categories");

        btnSave.setOnClickListener(v -> saveCategory());
    }

    private void saveCategory() {
        String name = etName.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Category Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = categoryRef.push().getKey();
        if (id != null) {
            Category category = new Category(id, name, imageUrl);
            categoryRef.child(id).setValue(category)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Category Added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}