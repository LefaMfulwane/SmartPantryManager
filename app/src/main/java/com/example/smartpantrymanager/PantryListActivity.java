package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PantryListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private PantryAdapter pantryAdapter;
    private List<PantryItem> pantryItemList;
    private FloatingActionButton fabAdd;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_pantry_list);

            databaseHelper = new DatabaseHelper(this);
            recyclerView = findViewById(R.id.recyclerViewPantry);
            fabAdd = findViewById(R.id.fabAddPantry);
            btnBackHome = findViewById(R.id.btnBackHomePantry);

            if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            pantryItemList = new ArrayList<>();

            loadPantryData();

            if (fabAdd != null) {
                fabAdd.setOnClickListener(v -> showAddPantryDialog(null));
            }

            if (btnBackHome != null) {
                btnBackHome.setOnClickListener(v -> finish());
            }

            if (getIntent().getBooleanExtra("open_add_dialog", false)) {
                if (recyclerView != null) {
                    recyclerView.post(() -> showAddPantryDialog(null));
                }
            }
        } catch (Exception e) {
            Log.e("PantryListActivity", "Error in onCreate", e);
            Toast.makeText(this, "Startup Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadPantryData() {
        pantryItemList.clear();
        try {
            Cursor cursor = databaseHelper.getAllPantryItems();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PANTRY_ID);
                    int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PANTRY_NAME);
                    int qtyIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PANTRY_QTY);
                    int unitIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PANTRY_UNIT);
                    int expiryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PANTRY_EXPIRY);

                    if (idIndex != -1 && nameIndex != -1 && qtyIndex != -1) {
                        int id = cursor.getInt(idIndex);
                        String name = cursor.getString(nameIndex);
                        double qty = cursor.getDouble(qtyIndex);
                        String unit = unitIndex != -1 ? cursor.getString(unitIndex) : "";
                        String expiry = expiryIndex != -1 ? cursor.getString(expiryIndex) : "";

                        pantryItemList.add(new PantryItem(id, name, qty, unit, expiry));
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("PantryListActivity", "Error loading pantry data", e);
        }

        pantryAdapter = new PantryAdapter(pantryItemList,
                id -> {
                    try {
                        boolean success = databaseHelper.deletePantryItem(id);
                        if (success) {
                            Toast.makeText(PantryListActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                            loadPantryData();
                        } else {
                            Toast.makeText(PantryListActivity.this, "Error deleting item", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                item -> showAddPantryDialog(item)
        );
        if (recyclerView != null) {
            recyclerView.setAdapter(pantryAdapter);
        }
    }

    private void showAddPantryDialog(final PantryItem itemToEdit) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_pantry, null);

            final EditText inputName = dialogView.findViewById(R.id.edit_text_name);
            final EditText inputQty = dialogView.findViewById(R.id.edit_text_quantity);
            final EditText inputUnit = dialogView.findViewById(R.id.edit_text_unit);
            final EditText inputExpiry = dialogView.findViewById(R.id.edit_text_expiry);

            if (itemToEdit != null) {
                builder.setTitle("Edit Pantry Item");
                if (inputName != null) inputName.setText(itemToEdit.getName());
                if (inputQty != null) inputQty.setText(String.valueOf(itemToEdit.getQuantity()));
                if (inputUnit != null) inputUnit.setText(itemToEdit.getUnit());
                if (inputExpiry != null) inputExpiry.setText(itemToEdit.getExpiryDate());
            } else {
                builder.setTitle("Add Pantry Item");
            }

            builder.setView(dialogView);
            builder.setPositiveButton(itemToEdit == null ? "Add" : "Update", (dialog, which) -> {
                String name = inputName != null ? inputName.getText().toString().trim() : "";
                String qtyStr = inputQty != null ? inputQty.getText().toString().trim() : "";
                String unit = inputUnit != null ? inputUnit.getText().toString().trim() : "";
                String expiry = inputExpiry != null ? inputExpiry.getText().toString().trim() : "";

                if (!name.isEmpty() && !qtyStr.isEmpty()) {
                    try {
                        double qty = Double.parseDouble(qtyStr);
                        boolean success = (itemToEdit == null) ?
                                databaseHelper.addPantryItem(name, qty, unit, expiry) :
                                databaseHelper.updatePantryItem(itemToEdit.getId(), name, qty, unit, expiry);

                        if (success) {
                            Toast.makeText(PantryListActivity.this, itemToEdit == null ? "Added!" : "Updated!", Toast.LENGTH_SHORT).show();
                            loadPantryData();
                        } else {
                            Toast.makeText(PantryListActivity.this, "Operation failed.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(PantryListActivity.this, "Invalid quantity number.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PantryListActivity.this, "Name and quantity required.", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        } catch (Exception e) {
            Log.e("PantryListActivity", "Error opening dialog", e);
            Toast.makeText(this, "Dialog Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}