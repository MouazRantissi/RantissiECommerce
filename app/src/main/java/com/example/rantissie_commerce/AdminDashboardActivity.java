package com.example.rantissie_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button btnLogout = findViewById(R.id.btnLogout);
        CardView cardCategories = findViewById(R.id.cardManageCategories);
        CardView cardProducts = findViewById(R.id.cardManageProducts);
        CardView cardUsers = findViewById(R.id.cardManageUsers);
        CardView cardRecycleBin = findViewById(R.id.cardRecycleBin);

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        cardCategories.setOnClickListener(v -> startActivity(new Intent(this, ManageCategoriesActivity.class)));
        cardProducts.setOnClickListener(v -> startActivity(new Intent(this, ManageProductsActivity.class)));

         cardUsers.setOnClickListener(v -> startActivity(new Intent(this, ManageUsersActivity.class)));
         cardRecycleBin.setOnClickListener(v -> startActivity(new Intent(this, RecycleBinActivity.class)));
    }
}