package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamekeeper.R;
import com.example.gamekeeper.Types.ListType;
import com.example.gamekeeper.adapters.ListAdapterSearch;
import com.example.gamekeeper.fragments.SearchBarFragment;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SearchBarFragment.OnSearchListener {
    private RecyclerView recyclerViewSearchResults;
    private ListAdapterSearch listAdapterSearch;
    private List<ListElement> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchResults = new ArrayList<>();
        listAdapterSearch = new ListAdapterSearch(searchResults, this,ListType.SEARCH);

        recyclerViewSearchResults = findViewById(R.id.listRecyclerView);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setAdapter(listAdapterSearch);

        listAdapterSearch.setOnItemClickListener(new ListAdapterSearch.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.BOARDGAME_ID, searchResults.get(position).getId());
                intent.putExtra(DetailActivity.NAME_VIEW, "SEARCH");
                startActivity(intent);
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, SearchBarFragment.newInstance(ListType.SEARCH));
        transaction.commit();
    }


    @Override
    public void onSearchResults(List<ListElement> results) {
        // Aquí se verifica el tipo de vista, en función de eso, filtramos los resultados.



            searchResults.clear();
            searchResults.addAll(results); // Aquí es donde se hace la búsqueda en la tabla boardgames

        listAdapterSearch.notifyDataSetChanged();
    }
}