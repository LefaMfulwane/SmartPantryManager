package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listViewRecipes;
    private TextView tvEmptyState;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        databaseHelper = new DatabaseHelper(this);
        listViewRecipes = findViewById(R.id.listViewRecipes);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnBackHome = findViewById(R.id.btnBackHomeRecipes);

        if (btnBackHome != null) {
            btnBackHome.setOnClickListener(v -> finish());
        }

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        Map<String, Double> pantryMap = new HashMap<>();
        try {
            Cursor pantryCursor = databaseHelper.getAllPantryItems();
            if (pantryCursor != null && pantryCursor.moveToFirst()) {
                do {
                    int nameIdx = pantryCursor.getColumnIndex(DatabaseHelper.COLUMN_PANTRY_NAME);
                    int qtyIdx = pantryCursor.getColumnIndex(DatabaseHelper.COLUMN_PANTRY_QTY);
                    if (nameIdx != -1 && qtyIdx != -1) {
                        String name = pantryCursor.getString(nameIdx).toLowerCase().trim();
                        double qty = pantryCursor.getDouble(qtyIdx);
                        pantryMap.put(name, qty);
                    }
                } while (pantryCursor.moveToNext());
                pantryCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> matchedRecipeDisplayList = new ArrayList<>();
        try {
            Cursor recipeCursor = databaseHelper.getAllRecipes();
            if (recipeCursor != null && recipeCursor.moveToFirst()) {
                do {
                    int nameIdx = recipeCursor.getColumnIndex(DatabaseHelper.COLUMN_RECIPE_NAME);
                    int ingIdx = recipeCursor.getColumnIndex(DatabaseHelper.COLUMN_RECIPE_INGREDIENTS);
                    int stepIdx = recipeCursor.getColumnIndex(DatabaseHelper.COLUMN_RECIPE_STEPS);

                    if (nameIdx != -1 && ingIdx != -1 && stepIdx != -1) {
                        String recipeName = recipeCursor.getString(nameIdx);
                        String ingredientsReq = recipeCursor.getString(ingIdx);
                        String steps = recipeCursor.getString(stepIdx);

                        boolean canMake = true;
                        if (ingredientsReq != null && !ingredientsReq.isEmpty()) {
                            String[] requirements = ingredientsReq.split(",");
                            for (String req : requirements) {
                                String[] parts = req.split(":");
                                if (parts.length == 2) {
                                    String reqItem = parts[0].toLowerCase().trim();
                                    double reqQty = 0;
                                    try {
                                        reqQty = Double.parseDouble(parts[1].trim());
                                    } catch (NumberFormatException ignored) {}

                                    if (!pantryMap.containsKey(reqItem) || pantryMap.get(reqItem) < reqQty) {
                                        canMake = false;
                                        break;
                                    }
                                }
                            }
                        }

                        if (canMake) {
                            matchedRecipeDisplayList.add(recipeName + "\n\nSteps:\n" + steps);
                        }
                    }
                } while (recipeCursor.moveToNext());
                recipeCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (matchedRecipeDisplayList.isEmpty()) {
            if (tvEmptyState != null) tvEmptyState.setVisibility(View.VISIBLE);
            if (listViewRecipes != null) listViewRecipes.setVisibility(View.GONE);
        } else {
            if (tvEmptyState != null) tvEmptyState.setVisibility(View.GONE);
            if (listViewRecipes != null) {
                listViewRecipes.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, matchedRecipeDisplayList);
                listViewRecipes.setAdapter(adapter);
            }
        }
    }
}