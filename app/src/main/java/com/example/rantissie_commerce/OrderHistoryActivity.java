package com.example.rantissie_commerce;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.rantissie_commerce.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private List<Order> orderList;
    private DatabaseReference ordersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        fetchUserOrders();
    }

    private void fetchUserOrders() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String currentUserId = currentUser.getUid();

        Query userOrdersQuery = ordersRef.orderByChild("userId").equalTo(currentUserId);

        userOrdersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }

                OrderAdapter orderAdapter = new OrderAdapter(OrderHistoryActivity.this, orderList);
                rvOrders.setAdapter(orderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this, "Error fetching orders: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}