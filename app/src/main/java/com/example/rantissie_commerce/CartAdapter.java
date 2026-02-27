package com.example.rantissie_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Product> cartList;
    private OnCartUpdatedListener listener;

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }

    public CartAdapter(Context context, List<Product> cartList, OnCartUpdatedListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartList.get(position);

        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));

        holder.btnRemove.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Product currentProduct = cartList.get(currentPosition);
                CartManager.getInstance().removeProduct(currentProduct);
                notifyItemRemoved(currentPosition);
                if (listener != null) {
                    listener.onCartUpdated();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice;
        Button btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCartItemTitle);
            tvPrice = itemView.findViewById(R.id.tvCartItemPrice);
            btnRemove = itemView.findViewById(R.id.btnRemoveFromCart);
        }
    }
}