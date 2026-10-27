# Smart Pantry Manager 🥑🍞

Smart Pantry Manager is a native Android application designed to help users efficiently track pantry inventory, minimize food waste, and automatically discover recipes based on available ingredients. Built using Java and Android SDK, it features local persistent storage via SQLite and an dynamic recipe-matching algorithm.

---

## 🚀 Key Features

* **Pantry Inventory Management (CRUD):** 
  * **Create:** Easily add pantry items with quantities, measurement units, and expiration dates.
  * **Read:** View active inventory items dynamically listed in a high-performance `RecyclerView`.
  * **Update:** Edit quantities and details of existing pantry stock.
  * **Delete:** Remove consumed or expired ingredients from the pantry database.
* **Smart Recipe Matching Engine:** 
  * Automatically cross-references currently stocked pantry items against pre-loaded recipes.
  * Displays matching recipes that can be prepared with available inventory.
* **Pre-seeded Recipe Database:** 
  * Comes out-of-the-box with 5 built-in recipes pre-loaded into the local database upon initial launch.

---

## 🛠️ Tech Stack & Architecture

* **Language:** Java
* **UI/Layouts:** Android XML Layouts, `RecyclerView`, `CardView`
* **Local Database:** SQLite (`SQLiteOpenHelper`)
* **Database Name:** `smart_pantry.db` (Schema Version 4)
* **Architecture Pattern:** Model-View-Adapter (MVA) pattern using custom adapters (`PantryAdapter`) and data access layers (`DatabaseHelper`).

---

## 🗄️ Database Schema & Pre-seeded Data

### Tables Structure

1. **`pantry` Table:**
   * `id` (INTEGER, Primary Key, Autoincrement)
   * `name` (TEXT)
   * `quantity` (REAL)
   * `unit` (TEXT)
   * `expiry_date` (TEXT)

2. **`recipes` Table:**
   * `id` (INTEGER, Primary Key, Autoincrement)
   * `recipe_name` (TEXT)
   * `ingredients` (TEXT - formatted key-value pairs e.g., `eggs:2,butter:1`)
   * `steps` (TEXT)

### Default Seeded Recipes

The application automatically seeds the following 5 recipes on initial database creation:
1. **Scrambled Eggs** (`eggs:2, butter:1`)
2. **French Toast** (`bread:2, milk:50, eggs:1, butter:10`)
3. **Peanut Butter Banana Toast** (`bread:1, banana:1, peanuts:20`)
4. **Apple Banana Smoothie** (`apple:1, banana:1, milk:150`)
5. **Croissant Breakfast Sandwich** (`croissants:1, eggs:1, butter:5`)

---

## 📲 How to Run the Project


1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/LefaMfulwane/SmartPantryManager.git](https://github.com/LefaMfulwane/SmartPantryManager.git)

   
## 📷 Application Screenshots

| Pantry Stock List | Add Item | Suggested Recipes |
| :---: | :---: | :---: |
| ![Pantry List](<Screenshot 2026-10-26 163334.png>) | ![Add Item](<Screenshot 2026-10-26 163429.png>) | ![Suggested Recipes](<Screenshot 2026-10-26 163546.png>) |
