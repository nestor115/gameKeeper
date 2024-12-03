package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.activities.SuggesterActivity;
import com.example.gamekeeper.adapters.PlayerAdapter;
import com.example.gamekeeper.fragments.PlayerFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;
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
        playerNames = intent.getStringArrayListExtra("player_names");
        adapter = new PlayerAdapter(playerNames, dB, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        FloatingActionButton fabConfirmSelection = findViewById(R.id.fab_confirm_selection);
        fabConfirmSelection.setOnClickListener(v -> {
            processSelectedPlayers();
            Intent intentSuggester = new Intent(PlayerBoardgameActivity.this, SuggesterActivity.class);
            startActivity(intentSuggester);

        });

        loadData();
        loadSearchFragment();
    }
    private void loadData() {
        List<ListElement> elements = dB.getUserBoardgames(currentUserId);
        if (elements != null && !elements.isEmpty()) {
            fullList = elements;
            adapter.submitList(fullList);
            recyclerView.setAdapter(adapter);
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
        adapter.submitList(filteredList);
    }
    private void loadSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PlayerFragment());
        transaction.commit();
    }
    private void processSelectedPlayers() {
        Log.d("DEBUGPlayers", "processSelectedPlayers: Iniciado");

        for (int i = 0; i < adapter.getItemCount(); i++) {
            ListElement boardgame = fullList.get(i);
            Log.d("DEBUGPlayers", "Juego: " + boardgame.getName() + " (ID: " + boardgame.getId() + ")");

            for (int j = 0; j < playerNames.size(); j++) {
                boolean isChecked = adapter.getCheckBoxState(i, j);

                Log.d("DEBUGPlayers", "Jugador: " + playerNames.get(j) + " | CheckBox seleccionado: " + isChecked);

                if (isChecked) {
                    int playerId = dB.getPlayerIdByName(playerNames.get(j), currentUserId);
                    Log.d("DEBUGPlayers", "playerId obtenido para " + playerNames.get(j) + ": " + playerId);

                    boolean canAdd = dB.addPlayerBoardgameOnce(playerId, boardgame.getId());
                    if (canAdd) {
                        Log.d("DEBUGPlayers", "Relación añadida: Jugador " + playerId + " - Juego " + boardgame.getId());
                    } else {
                        Log.d("DEBUGPlayers", "Relación ya existente para Jugador " + playerId + " y Juego " + boardgame.getId());
                        Toast.makeText(this, "El jugador ya jugó este juego", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        Log.d("DEBUGPlayers", "processSelectedPlayers: Finalizado");
    }




}
