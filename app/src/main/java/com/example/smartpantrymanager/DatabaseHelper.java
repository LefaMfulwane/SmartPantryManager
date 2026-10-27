package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 4; // Bumped version to trigger fresh onCreate seeding

    public static final String TABLE_PANTRY = "pantry";
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QTY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "recipe_name";
    public static final String COLUMN_RECIPE_INGREDIENTS = "ingredients";
    public static final String COLUMN_RECIPE_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PANTRY_NAME + " TEXT, " +
                COLUMN_PANTRY_QTY + " REAL, " +
                COLUMN_PANTRY_UNIT + " TEXT, " +
                COLUMN_PANTRY_EXPIRY + " TEXT)";
        db.execSQL(createPantryTable);

        String createRecipeTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT, " +
                COLUMN_RECIPE_INGREDIENTS + " TEXT, " +
                COLUMN_RECIPE_STEPS + " TEXT)";
        db.execSQL(createRecipeTable);

        // Seed the 5 recipes using the insertRecipe helper method
        insertRecipe(db, "Scrambled Eggs", "eggs:2,butter:1", "1. Whisk eggs in a bowl.\n2. Melt butter in a pan over medium heat.\n3. Pour in eggs and stir continuously until cooked.");
        insertRecipe(db, "French Toast", "bread:2,milk:50,eggs:1,butter:10", "1. Whisk egg and milk together.\n2. Dip bread slices into the mixture.\n3. Fry in butter until golden brown on both sides.");
        insertRecipe(db, "Peanut Butter Banana Toast", "bread:1,banana:1,peanuts:20", "1. Toast the slice of bread.\n2. Spread peanut butter or crushed peanuts evenly.\n3. Slice banana and place on top.");
        insertRecipe(db, "Apple Banana Smoothie", "apple:1,banana:1,milk:150", "1. Chop the apple and banana.\n2. Place fruits and milk into a blender.\n3. Blend until smooth and creamy.");
        insertRecipe(db, "Croissant Breakfast Sandwich", "croissants:1,eggs:1,butter:5", "1. Slice the croissant in half.\n2. Scramble the egg in a pan with butter.\n3. Place the cooked egg inside the croissant.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    private void insertRecipe(SQLiteDatabase db, String name, String ingredients, String steps) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_INGREDIENTS, ingredients);
        values.put(COLUMN_RECIPE_STEPS, steps);
        db.insert(TABLE_RECIPES, null, values);
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY, null);
    }

    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);
    }

    public boolean addPantryItem(String name, double quantity, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, name);
        values.put(COLUMN_PANTRY_QTY, quantity);
        values.put(COLUMN_PANTRY_UNIT, unit);
        values.put(COLUMN_PANTRY_EXPIRY, expiry);
        long result = db.insert(TABLE_PANTRY, null, values);
        return result != -1;
    }

    public boolean updatePantryItem(int id, String name, double quantity, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, name);
        values.put(COLUMN_PANTRY_QTY, quantity);
        values.put(COLUMN_PANTRY_UNIT, unit);
        values.put(COLUMN_PANTRY_EXPIRY, expiry);
        long result = db.update(TABLE_PANTRY, values, COLUMN_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_PANTRY, COLUMN_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}