package com.example.gamekeeper.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


        Button btnBack = findViewById(R.id.btn_Back);

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

                intent.putExtra("BOARDGAME_ID", searchResults.get(position).getId());
                startActivity(intent);
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        performSearch("");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void performSearch(String query) {
        Cursor cursor;


        if (query.isEmpty()) {
            cursor = dB.getAllBoardgames();
        } else {
            cursor = dB.searchBoardgamesByName(query);
        }

        searchResults.clear();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int columnIndexName = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NAME);
                    int columnIndexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_ID);

                    if (columnIndexName != -1 && columnIndexId != -1) {
                        String name = cursor.getString(columnIndexName);
                        int id = cursor.getInt(columnIndexId);
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
