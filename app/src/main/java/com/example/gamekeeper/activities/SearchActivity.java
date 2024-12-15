package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.SearchAdapter;
import com.example.gamekeeper.fragments.SearchGenreFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;
import com.example.gamekeeper.utils.IntentExtras;

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

        Button btnAddBoardgame = findViewById(R.id.btnAddBoardgame);
        //Boton para añadir un juego de mesa
        btnAddBoardgame.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, AddBoardgameActivity.class);
            intent.putExtra(IntentExtras.ACTION_BUTTON, "ADD");
            startActivity(intent);
        });
        //Boton para ver los detalles de un juego de mesa
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement listElement) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra(IntentExtras.BOARDGAME_ID, listElement.getId());
                intent.putExtra(IntentExtras.NAME_VIEW, "SEARCH");
                startActivity(intent);
            }
        });
        //Boton para editar un juego de mesa
        searchAdapter.setOnEditClickListener(listElement -> {
            Intent intent = new Intent(this, AddBoardgameActivity.class);
            intent.putExtra(IntentExtras.ACTION_BUTTON, "EDIT"); // Indica que es una edición
            intent.putExtra(IntentExtras.GAME_ID, listElement.getId()); // Pasa el ID del juego
            startActivity(intent);
        });
        loadData();
        loadSearchFragment();
    }

    //Metodo para cargar el fragmento de busqueda y del filtro de generos
    private void loadSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SearchGenreFragment());
        transaction.commit();
    }

    //Metodo para cargar los juegos de la base de datos
    private void loadData() {
        List<ListElement> elements = dB.getAllBoardgames();

        if (elements != null && !elements.isEmpty()) {
            fullList = new ArrayList<>(elements);
            searchAdapter.submitList(new ArrayList<>(fullList));
        } else {
            Toast.makeText(this, "No se encontraron juegos.", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para obtener los generos de la base de datos
    public List<String> getGenres() {
        return dB.getGenres();
    }

    //Metodo filtrar la lista de juegos segun la busqueda y el genero seleccionado
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