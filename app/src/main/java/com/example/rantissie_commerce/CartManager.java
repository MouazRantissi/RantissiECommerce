package com.example.rantissie_commerce;

import com.example.rantissie_commerce.Product;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;

    private List<Product> cartItems = new ArrayList<>();

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProduct(Product product) {
        cartItems.add(product);
    }

    public List<Product> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void removeProduct(Product product) {
        cartItems.remove(product);
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product item : cartItems) {
            total += item.getPrice();
        }
        return total;
    }
}