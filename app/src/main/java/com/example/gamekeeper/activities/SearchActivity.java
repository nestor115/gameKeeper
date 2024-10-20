package com.example.gamekeeper.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.ListAdapter;
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
        editTextSearch = findViewById(R.id.et_BoardgamesSearch);
        recyclerViewSearchResults = findViewById(R.id.listRecyclerView);

        searchResults = new ArrayList<>();
        listAdapter = new ListAdapter(searchResults, this);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                // Pasa el ID del juego seleccionado
                intent.putExtra("BOARDGAME_ID", searchResults.get(position).getId());
                startActivity(intent);
            }
        });

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
                    // Obtener los índices de las columnas necesarias
                    int columnIndexName = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NAME);
                    int columnIndexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_ID); // Asume que este es el nombre de la columna del ID

                    if (columnIndexName != -1 && columnIndexId != -1) {
                        // Obtener el nombre y el ID del juego
                        String name = cursor.getString(columnIndexName);
                        int id = cursor.getInt(columnIndexId);

                        // Crear un nuevo ListElement con el nombre y el ID
                        ListElement element = new ListElement(name, id);
                        searchResults.add(element);
                    } else {
                        Log.e("SEARCH_ACTIVITY", "Columnas no encontradas");
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        listAdapter.notifyDataSetChanged();
    }
}
