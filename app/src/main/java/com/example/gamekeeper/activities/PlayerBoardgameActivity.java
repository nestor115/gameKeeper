package com.example.gamekeeper.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.ListAdapterPlayers;
import com.example.gamekeeper.fragments.PlayerFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardgameActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ListAdapterPlayers adapter;
    private DatabaseHelper dB;
    private int currentUserId; // Almacenar el ID del usuario actual
    private List<ListElement> fullList = new ArrayList<>(); // Lista completa de elementos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_boardgame);

        // Inicializar DatabaseHelper
        dB = new DatabaseHelper(this);

        // Obtener el ID del usuario desde SharedPreferences
        currentUserId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ListAdapterPlayers();
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Para lista vertical
        recyclerView.setAdapter(adapter);

        // Cargar los datos
        loadData();

        // Cargar el fragmento con el SearchView y Spinner
        loadSearchFragment();
    }

    private void loadSearchFragment() {
        // Cargar el fragmento de búsqueda
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PlayerFragment());
        transaction.commit();
    }

    private void loadData() {
        // Llamamos al método de la base de datos para obtener los juegos del usuario
        List<ListElement> elements = dB.getUserBoardgames(currentUserId);

        if (elements != null && !elements.isEmpty()) {
            fullList = elements; // Guardamos la lista completa
            adapter.submitList(fullList);
        } else {
            Toast.makeText(this, "No se encontraron juegos.", Toast.LENGTH_SHORT).show();
        }
    }

    public List<String> getGenres() {
        // Obtener los géneros desde la base de datos
        return dB.getGenres();
    }

    // Método para filtrar la lista
    public void filterList(String query, String selectedGenre) {
        List<ListElement> filteredList = new ArrayList<>();
        for (ListElement element : fullList) {
            boolean matchesQuery = element.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesGenre = selectedGenre.equals("Todos") || dB.isBoardgameInGenre(element.getId(), selectedGenre);

            // Añadir a la lista filtrada si cumple con ambos criterios
            if (matchesQuery && matchesGenre) {
                filteredList.add(element);
            }
        }
        // Actualizar el RecyclerView con los elementos filtrados
        adapter.submitList(filteredList);
    }
}