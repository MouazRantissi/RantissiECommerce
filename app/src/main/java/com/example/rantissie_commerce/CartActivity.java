package com.example.rantissie_commerce;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.rantissie_commerce.ProductAdapter;
import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.Order;
import com.example.rantissie_commerce.Product;
import com.example.rantissie_commerce.CartManager;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCartItems;
    private TextView tvCartTotal;
    private Button btnCheckout;
    private CartAdapter adapter;
    private DatabaseReference ordersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCartItems = findViewById(R.id.rvCartItems);
        tvCartTotal = findViewById(R.id.tvCartTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        mAuth = FirebaseAuth.getInstance();
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        if (btnCheckout != null) {
            btnCheckout.setOnClickListener(v -> processCheckout());
        }

        setupCart();
    }

    private void setupCart() {
        List<Product> cartItems = CartManager.getInstance().getCartItems();

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CartAdapter(this, cartItems, new CartAdapter.OnCartUpdatedListener() {
            @Override
            public void onCartUpdated() {
                updateTotalPriceUI(cartItems);
            }
        });
        rvCartItems.setAdapter(adapter);

        updateTotalPriceUI(cartItems);
    }

    private void updateTotalPriceUI(List<Product> cartItems) {
        double total = CartManager.getInstance().getTotalPrice();
        tvCartTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));

        // Bulletproof null-check before touching the UI
        if (btnCheckout != null && cartItems != null) {
            btnCheckout.setEnabled(!cartItems.isEmpty());
        }
    }

    private void processCheckout() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to checkout", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Product> cartItems = CartManager.getInstance().getCartItems();
        double totalAmount = CartManager.getInstance().getTotalPrice();
        String userId = currentUser.getUid();

        String orderId = ordersRef.push().getKey();
        if (orderId != null) {

            Order newOrder = new Order(orderId, userId, cartItems, totalAmount, "Delivered", System.currentTimeMillis());

            ordersRef.child(orderId).setValue(newOrder)
                    .addOnSuccessListener(aVoid -> {
                        CartManager.getInstance().clearCart();

                        Toast.makeText(this, "Order Delivered Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Checkout Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}