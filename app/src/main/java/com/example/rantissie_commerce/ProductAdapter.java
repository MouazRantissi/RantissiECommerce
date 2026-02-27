package com.example.rantissie_commerce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rantissie_commerce.EditProductActivity;
import com.example.rantissie_commerce.ManageProductsActivity;
import com.example.rantissie_commerce.ProductDetailActivity;
import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.Product;


import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductTitle.setText(product.getTitle());
        holder.tvProductPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));

         Glide.with(context).load(product.getImageUrl()).into(holder.ivProductImage);

        holder.itemView.setOnClickListener(v -> {
            // Context-based Routing Logic
            if (context instanceof ManageProductsActivity) {
                // Admin routing
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("selected_product", product);
                context.startActivity(intent);
            } else {
                // Customer routing (CategoryProductsActivity, MainActivity, etc.)
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("selected_product", product);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductTitle, tvProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductTitle = itemView.findViewById(R.id.tvProductTitle);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}