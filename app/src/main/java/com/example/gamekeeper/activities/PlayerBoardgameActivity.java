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

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardgameActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private DatabaseHelper dB;
    private int currentUserId;
    private List<ListElement> fullList = new ArrayList<>();
    private ArrayList<String> playerNames; // Para almacenar los nombres de los jugadores

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

}
