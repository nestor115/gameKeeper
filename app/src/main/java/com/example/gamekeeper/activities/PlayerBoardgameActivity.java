package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
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
        adapter = new PlayerAdapter(playerNames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        FloatingActionButton fabConfirmSelection = findViewById(R.id.fab_confirm_selection);
        fabConfirmSelection.setOnClickListener(v -> processSelectedPlayers());

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
        boolean success = true;

        android.util.Log.d("processSelectedPlayers", "Inicio del proceso de guardado");

        for (int i = 0; i < adapter.getItemCount(); i++) {
            ListElement boardgame = fullList.get(i);
            android.util.Log.d("processSelectedPlayers", "Juego: " + boardgame.getName() + " (ID: " + boardgame.getId() + ")");

            for (int j = 0; j < playerNames.size(); j++) {
                boolean isChecked = adapter.getCheckBoxState(i, j);  // Obtener el estado del CheckBox

                android.util.Log.d("processSelectedPlayers", "Checkbox para jugador " + playerNames.get(j) + " está " + (isChecked ? "marcado" : "desmarcado"));

                if (isChecked) {
                    int playerId = dB.getPlayerIdByName(playerNames.get(j), currentUserId);
                    android.util.Log.d("processSelectedPlayers", "Player ID obtenido: " + playerId);

                    boolean result = dB.addOrIncrementPlayerBoardgame(playerId, boardgame.getId());
                    android.util.Log.d("processSelectedPlayers", "Resultado de addOrIncrementPlayerBoardgame: " + result);

                    success &= result;
                }
            }
        }

        String message = success ? "Datos guardados correctamente" : "Ocurrió un error al guardar los datos";
        android.util.Log.d("processSelectedPlayers", "Resultado final: " + message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}
