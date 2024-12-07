package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.SearchAdapter;
import com.example.gamekeeper.fragments.PlayerFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    private RecyclerView recyclerViewSearch;
    private SearchAdapter searchAdapter;
    private DatabaseHelper dB;
    private List<ListElement> fullList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dB = new DatabaseHelper(this);

        recyclerViewSearch = findViewById(R.id.listRecyclerView);
        searchAdapter = new SearchAdapter();
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setAdapter(searchAdapter);

        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement listElement) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.BOARDGAME_ID, listElement.getId());
                intent.putExtra(DetailActivity.NAME_VIEW, "SEARCH");
                startActivity(intent);
            }
        });
        loadData();
        loadSearchFragment();
    }
    private void loadSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PlayerFragment());
        transaction.commit();
    }

    private void loadData() {
        List<ListElement> elements = dB.getAllBoardgames();

        if (elements != null && !elements.isEmpty()) {
            fullList = new ArrayList<>(elements);
            searchAdapter.submitList(new ArrayList<>(fullList));
        } else {
            Toast.makeText(this, "No se encontraron juegos.", Toast.LENGTH_SHORT).show();
        }
    }
    public List<String> getGenres() {
        return dB.getGenres();
    }
    public void filterList(String query, String selectedGenre) {
        List<ListElement> filteredList = new ArrayList<>();
        for (ListElement element : fullList) {
            boolean matchesQuery = element.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesGenre = selectedGenre.equals("Todos") || dB.isBoardgameInGenre(element.getId(), selectedGenre);

            if (matchesQuery && matchesGenre) {
                filteredList.add(element);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No se encontraron juegos de ese genero.", Toast.LENGTH_SHORT).show();
        }
        searchAdapter.submitList(new ArrayList<>(filteredList));
    }

}