package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.PlayerAdapter;
import com.example.gamekeeper.fragments.SearchGenreFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;
import com.example.gamekeeper.utils.IntentExtras;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardgameActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private DatabaseHelper dB;
    private int currentUserId;
    private List<ListElement> fullList = new ArrayList<>();
    private ArrayList<String> playerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_boardgame);

        dB = new DatabaseHelper(this);
        currentUserId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);

        recyclerView = findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        //Obtiene los nombres de los jugadores seleccionados de la actividad anterior
        playerNames = intent.getStringArrayListExtra(IntentExtras.PLAYER_NAMES);
        adapter = new PlayerAdapter(playerNames, dB, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        FloatingActionButton fabConfirmSelection = findViewById(R.id.fab_confirm_selection);
        //Boton de confirmacion de que los jugadores han jugado a esos juegos
        fabConfirmSelection.setOnClickListener(v -> {
            associatePlayersWithGames();
            Intent intentSuggester = new Intent(PlayerBoardgameActivity.this, SuggesterActivity.class);
            intentSuggester.putStringArrayListExtra(IntentExtras.SELECTED_PLAYERS, playerNames);
            startActivity(intentSuggester);
            finish();

        });

        loadData();
        loadSearchFragment();
    }
    //Carga los juegos del usuario y los envia al adapter
    private void loadData() {
        List<ListElement> elements = dB.getUserBoardgames(currentUserId);
        if (elements != null && !elements.isEmpty()) {
            fullList = new ArrayList<>(elements);
            adapter.submitList(new ArrayList<>(fullList));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No se encontraron juegos.", Toast.LENGTH_SHORT).show();
        }
    }
    //Obtiene los generos de la base de datos
    public List<String> getGenres() {
        return dB.getGenres();
    }

    //Filtra la lista de juegos segun la busqueda y el genero seleccionado
    public void filterList(String query, String selectedGenre) {
        List<ListElement> filteredList = new ArrayList<>();
        for (ListElement element : fullList) {
            boolean matchesQuery = element.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesGenre = selectedGenre.equals("Todos") || dB.isBoardgameInGenre(element.getId(), selectedGenre);

            if (matchesQuery && matchesGenre) {
                filteredList.add(element);
            }
        }
        adapter.submitList(filteredList);
    }
    //Carga el fragmento de busqueda
    private void loadSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SearchGenreFragment());
        transaction.commit();
    }
    //Asocia jugadores seleccionados con juegos en la base de datos evitando duplicados.
    private void associatePlayersWithGames() {

        for (int i = 0; i < adapter.getItemCount(); i++) {
            ListElement boardgame = fullList.get(i);

            for (int j = 0; j < playerNames.size(); j++) {
                boolean isChecked = adapter.getCheckBoxState(i, j);
                if (isChecked) {
                    int playerId = dB.getPlayerIdByName(playerNames.get(j), currentUserId);
                    if (dB.addPlayerBoardgameOnce(playerId, boardgame.getId())) {
                        Log.d("DEBUGPlayers", "Relación añadida: Jugador " + playerId + " - Juego " + boardgame.getId());
                    }
                }
            }
        }

        Log.d("DEBUGPlayers", "processSelectedPlayers: Finalizado");
    }


}
