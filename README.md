# Rantissi E-Commerce App

A native Android E-Commerce application built with Java and Firebase. This app provides a seamless shopping experience for customers and a robust, multi-tiered administrative dashboard for inventory and user management.

Developed by Mouaz Al-Rantissi.

## 🚀 Features

### Customer Experience
* **Secure Authentication:** User registration and login powered by Firebase Authentication.
* **Dynamic Shopping Cart:** A globally accessible cart using the Singleton design pattern, allowing users to add/remove items and calculate totals in real-time.
* **Product Catalog:** Browse products grouped by categories with images loaded dynamically from web URLs via the Glide library.
* **Order History:** Customers can view past checkouts, complete with timestamps, total prices, and delivery statuses.

### Admin & SuperAdmin Dashboard
* **Role-Based Access Control:** Three distinct user tiers (Customer, Admin, SuperAdmin).
* **Inventory Management:** Add, edit, and delete products and categories directly from the app.
* **Recycle Bin (Soft & Hard Delete):** Deleted items are moved to a temporary "Trash" node. Admins can restore items to their original location or permanently delete them.
* **SuperAdmin Protection:** Strict UI and backend intent-fallback security preventing standard Admins from modifying SuperAdmin credentials or accessing user management.

---

## 🏗️ Architecture & Relationships

This project follows a standard Model-View-Controller (MVC) adaptation for Android, heavily integrated with Firebase Realtime Database.



### 1. The Core Models (Data Layer)
These classes represent the exact JSON structure stored in Firebase. They require empty constructors for Firebase serialization.
* **`Product`**: Stores `id`, `title`, `description`, `price`, `imageUrl`, and `category`.
* **`Order`**: Contains `id`, `userId`, `totalAmount`, `status`, `timestamp`, and a `List<Product>` representing the cart at checkout.
* **`User`**: Manages credentials and the `role` tier (Customer, Admin, SuperAdmin).
* **`TrashItem`**: Stores the serialized JSON of deleted objects alongside their `originalNode` so they can be accurately restored.
* **`Category`**: Manages top-level organization nodes (e.g., Menswear, Footwear).

### 2. The Utilities (State Management)
* **`CartManager` (Singleton):** The beating heart of the shopping experience. It holds a single instance of `List<Product>` in the phone's local memory. `ProductDetailActivity` adds to it, `CartActivity` reads/removes from it, and checkout clears it.

### 3. The Adapters (View Binders)
Adapters act as the bridge between the data models and the `RecyclerView` UI components.
* **`ProductAdapter`**: Maps product lists to `item_product.xml`. Implements Glide to fetch image URLs. Handles clicks to open `ProductDetailActivity`.
* **`CartAdapter`**: Maps the Singleton list to `item_cart.xml`. Includes an interface listener (`OnCartUpdatedListener`) to trigger live UI price updates when an item is removed.
* **`TrashAdapter`**: Binds deleted data to `item_trash.xml`. Contains the logic for the "Restore" and "Permanent Delete" database transactions.
* **`OrderAdapter`**: Formats Unix timestamps into readable dates and displays checkout history.

### 4. Key Activities (Controllers)
* **`MainActivity` / `Dashboard`**: The storefront feeding data from Firebase into the Adapters.
* **`ProductDetailActivity`**: Receives serialized `Product` objects via Intents, displays full data, and interfaces with the `CartManager`.
* **`CartActivity`**: Validates the user session, calculates totals, and pushes the final `Order` object to the Firebase `Orders` node.
* **`AdminDashboardActivity`**: The central hub for management. Implements a real-time database check to verify the current user's role before granting access to sensitive screens like `ManageUsersActivity`.

---

## 🛠️ Tech Stack & Libraries
* **Language:** Java
* **IDE:** Android Studio
* **Database:** Firebase Realtime Database
* **Authentication:** Firebase Auth
* **Image Loading:** [Glide](https://github.com/bumptech/glide) (v4.16.0)

## ⚙️ Setup Instructions
1. Clone this repository.
2. Open the project in Android Studio.
3. Connect the project to your own Firebase project via **Tools > Firebase**.
4. Enable **Authentication** (Email/Password) and **Realtime Database** in your Firebase Console.
5. Set Realtime Database rules to allow read/writes for testing.
6. Build and Run the project on an emulator or physical device.
