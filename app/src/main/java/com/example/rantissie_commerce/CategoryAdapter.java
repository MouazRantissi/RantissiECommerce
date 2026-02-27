package com.example.rantissie_commerce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rantissie_commerce.CategoryProductsActivity;
import com.example.rantissie_commerce.EditCategoryActivity;
import com.example.rantissie_commerce.MainActivity;
import com.example.rantissie_commerce.ManageCategoriesActivity;
import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.Category;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof ManageCategoriesActivity) {
                Intent intent = new Intent(context, EditCategoryActivity.class);
                intent.putExtra("selected_category", category); // Automatically uses Serializable
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, CategoryProductsActivity.class);
                intent.putExtra("category_name", category.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}