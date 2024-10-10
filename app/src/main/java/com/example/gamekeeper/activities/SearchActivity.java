package com.example.gamekeeper.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private DatabaseHelper dB;
    private EditText editTextSearch;
    private RecyclerView recyclerViewSearchResults;
    private ListAdapter listAdapter;
    private List<ListElement> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dB = new DatabaseHelper(this);
        editTextSearch = findViewById(R.id.editTextBoardGames);
        recyclerViewSearchResults = findViewById(R.id.listRecyclerView);

        searchResults = new ArrayList<>();
        listAdapter = new ListAdapter(searchResults, this);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setAdapter(listAdapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No es necesario implementar
            }
        });
    }

    private void performSearch(String query) {
        Cursor cursor = dB.searchBoardgamesByName(query);
        searchResults.clear();

        if (cursor != null) {
            // Imprime los nombres de las columnas
            String[] columnNames = cursor.getColumnNames();
            for (String name : columnNames) {
                Log.d("COLUMN_NAME", name); // Imprimir los nombres de las columnas
            }

            if (cursor.moveToFirst()) {
                do {
                    int columnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NAME);
                    if (columnIndex != -1) { // Solo proceder si el índice es válido
                        String name = cursor.getString(columnIndex);
                        ListElement element = new ListElement(name);
                        searchResults.add(element);
                    } else {
                        Log.e("SEARCH_ACTIVITY", "Columna no encontrada: " + DatabaseHelper.COLUMN_BOARDGAME_NAME);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        listAdapter.notifyDataSetChanged();
    }
}