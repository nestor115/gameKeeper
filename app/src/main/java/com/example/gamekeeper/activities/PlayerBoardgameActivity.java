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
    private int currentUserId;  // Almacenar el ID del usuario actual
    private List<ListElement> fullList = new ArrayList<>(); // Lista completa de elementos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_boardgame);

        // Inicializar DatabaseHelper
        dB = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        // Obtener el RecyclerView del layout
        recyclerView = findViewById(R.id.recyclerView);

        // Crear el adaptador
        adapter = new ListAdapterPlayers();

        // Configurar el RecyclerView con el adaptador
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Para que se muestre como lista vertical
        recyclerView.setAdapter(adapter);

        // Llamar al método para cargar los datos del usuario
        loadData();

        // Cargar el PlayerFragment (que contiene el SearchView)
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
            fullList = elements;  // Guardamos la lista completa
            adapter.submitList(fullList);
        } else {
            Toast.makeText(this, "No se encontraron juegos.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para filtrar la lista de elementos
    public void filterList(String query) {
        List<ListElement> filteredList = new ArrayList<>();
        for (ListElement element : fullList) {
            if (element.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(element);
            }
        }
        adapter.submitList(filteredList); // Actualizar el RecyclerView con los elementos filtrados
    }
}