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
import androidx.fragment.app.FragmentTransaction;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.ListAdapter;
import com.example.gamekeeper.fragments.SearchBarFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SearchBarFragment.OnSearchListener {
    private RecyclerView recyclerViewSearchResults;
    private ListAdapter listAdapter;
    private List<ListElement> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchResults = new ArrayList<>();
        listAdapter = new ListAdapter(searchResults, this);

        recyclerViewSearchResults = findViewById(R.id.listRecyclerView);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.BOARDGAME_ID, searchResults.get(position).getId());
                intent.putExtra(DetailActivity.NAME_VIEW, "SEARCH");
                startActivity(intent);
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, SearchBarFragment.newInstance());
        transaction.commit();
    }


    @Override
    public void onSearchResults(List<ListElement> results) {
        searchResults.clear();
        searchResults.addAll(results);
        listAdapter.notifyDataSetChanged();
    }
}