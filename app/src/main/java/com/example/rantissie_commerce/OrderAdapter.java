package com.example.rantissie_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        String shortId = order.getId() != null && order.getId().length() > 8
                ? order.getId().substring(order.getId().length() - 8)
                : order.getId();
        holder.tvOrderId.setText("Order #" + shortId.toUpperCase());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
        String dateString = sdf.format(new Date(order.getTimestamp()));
        holder.tvOrderDate.setText(dateString);

        int itemCount = order.getProducts() != null ? order.getProducts().size() : 0;
        holder.tvOrderItemsCount.setText(itemCount + (itemCount == 1 ? " Item" : " Items"));
        holder.tvOrderTotal.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));

        holder.tvOrderStatus.setText(order.getStatus());

        if ("Delivered".equalsIgnoreCase(order.getStatus())) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else if ("Pending".equalsIgnoreCase(order.getStatus())) {
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderItemsCount, tvOrderStatus, tvOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderItemsCount = itemView.findViewById(R.id.tvOrderItemsCount);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
        }
    }
}