package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnViewPantry = findViewById(R.id.btnViewPantry);
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        Button btnFindRecipes = findViewById(R.id.btnFindRecipes);
        Button btnSettings = findViewById(R.id.btnSettings);

        if (btnViewPantry != null) {
            btnViewPantry.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PantryListActivity.class);
                startActivity(intent);
            });
        }

        if (btnAddIngredient != null) {
            btnAddIngredient.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PantryListActivity.class);
                intent.putExtra("open_add_dialog", true);
                startActivity(intent);
            });
        }

        if (btnFindRecipes != null) {
            btnFindRecipes.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
                startActivity(intent);
            });
        }

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }
    }
}