package com.example.rantissie_commerce;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.rantissie_commerce.TrashItem;
import com.example.rantissie_commerce.User;

public class EditUserActivity extends AppCompatActivity {

    private EditText etName;
    private TextView tvEmail;
    private Spinner spinnerRole;
    private Button btnUpdate, btnDelete;

    private DatabaseReference usersRef, trashRef;
    private User selectedUser;
    private String[] roles = {"Customer", "Admin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etName = findViewById(R.id.etEditUserName);
        tvEmail = findViewById(R.id.tvEditUserEmail);
        spinnerRole = findViewById(R.id.spinnerUserRole);
        btnUpdate = findViewById(R.id.btnUpdateUser);
        btnDelete = findViewById(R.id.btnDeleteUser);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        trashRef = FirebaseDatabase.getInstance().getReference("Trash");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        try {
            selectedUser = (User) getIntent().getSerializableExtra("selected_user");

            if (selectedUser == null) {
                throw new NullPointerException("User data is missing");
            }

            if ("admin@clothingstore.com".equalsIgnoreCase(selectedUser.getEmail())) {
                Toast.makeText(this, "Protected account cannot be edited.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            etName.setText(selectedUser.getName());
            tvEmail.setText(selectedUser.getEmail());

            if ("Admin".equals(selectedUser.getRole())) {
                spinnerRole.setSelection(1); // 1 is Admin
            } else {
                spinnerRole.setSelection(0); // 0 is Customer
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error loading user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnUpdate.setOnClickListener(v -> updateUser());
        btnDelete.setOnClickListener(v -> softDeleteUser());
    }

    private void updateUser() {
        String name = etName.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedUser.setName(name);
        selectedUser.setRole(role);

        usersRef.child(selectedUser.getId()).setValue(selectedUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "User Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void softDeleteUser() {
        String trashId = trashRef.push().getKey();

        if (trashId != null) {
            TrashItem trashItem = new TrashItem(trashId, "Users", selectedUser, System.currentTimeMillis());

            trashRef.child(trashId).setValue(trashItem)
                    .addOnSuccessListener(aVoid -> {
                        usersRef.child(selectedUser.getId()).removeValue()
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(this, "User moved to Recycle Bin", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Delete Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}