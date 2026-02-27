package com.example.rantissie_commerce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rantissie_commerce.EditUserActivity;
import com.example.rantissie_commerce.R;
import com.example.rantissie_commerce.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvUserName.setText(user.getName());
        holder.tvUserEmail.setText(user.getEmail());

        if ("admin@clothingstore.com".equalsIgnoreCase(user.getEmail())) {

            holder.tvUserRole.setText("SuperAdmin (Protected)");
            holder.tvUserRole.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));

            holder.itemView.setAlpha(0.6f);

            holder.itemView.setOnClickListener(null);

        } else {
            holder.tvUserRole.setText(user.getRole());
            holder.tvUserRole.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));

            holder.itemView.setAlpha(1.0f);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditUserActivity.class);
                intent.putExtra("selected_user", user);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail, tvUserRole;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
        }
    }
}