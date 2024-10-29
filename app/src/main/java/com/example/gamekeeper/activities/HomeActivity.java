package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.BoardGameAdapter;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.BoardGame;

import java.util.List;

public class HomeActivity extends BaseActivity {

    private DatabaseHelper dB;
    private Button buttonGoSearch;
    private RecyclerView recyclerView;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dB = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("user_id", -1);


        if (currentUserId != -1) {

            List<BoardGame> userBoardgames = dB.getUserBoardgames(currentUserId);


            displayBoardgames(userBoardgames);
        } else {
            Toast.makeText(this, "No se encontró el ID del usuario.", Toast.LENGTH_SHORT).show();
        }

        insertExampleBoardgames();

        buttonGoSearch = findViewById(R.id.buttonGoSearch);
        dB = new DatabaseHelper(this);

        buttonGoSearch.setOnClickListener(v -> {


            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();

        });
    }

    private void insertExampleBoardgames() {
        dB.addBoardgame("Exploding Kittens", null, "Un juego de cartas de estrategia y humor en el que los jugadores deben evitar explotar.", 2015, "2-5", "15 min");
        dB.addBoardgame("Sushi Go!", null, "Un juego de cartas de draft rápido en el que los jugadores intentan crear la mejor combinación de sushi.", 2013, "2-5", "15 min");
        dB.addBoardgame("The Mind", null, "Un juego cooperativo donde los jugadores intentan sincronizar sus cartas en orden ascendente sin comunicación verbal.", 2018, "2-4", "20 min");
        dB.addBoardgame("Catan", null, "Un juego de estrategia donde los jugadores intentan construir y gestionar colonias en la isla de Catan.", 1995, "3-4", "60-120 min");
        dB.addBoardgame("Cubirds", null, "Un juego de cartas en el que los jugadores intentan recolectar bandadas de aves colocando cartas estratégicamente.", 2018, "2-5", "20-30 min");
        dB.addBoardgame("Unstable Unicorns", null, "Un juego de cartas en el que los jugadores construyen un ejército de unicornios y usan su magia para sabotear a los demás.", 2017, "2-8", "30-45 min");
        dB.addBoardgame("Virus", null, "Un juego de cartas en el que los jugadores intentan infectar a otros mientras protegen sus órganos sanos.", 2015, "2-6", "20 min");
        dB.addBoardgame("Here to Slay", null, "Un juego de cartas estratégico y competitivo en el que los jugadores forman un equipo de héroes y luchan contra monstruos.", 2020, "2-6", "30-60 min");

    }

    private void displayBoardgames(List<BoardGame> boardgames) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("user_id", -1);

        recyclerView = findViewById(R.id.recyclerViewBoardgames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BoardGameAdapter adapter = new BoardGameAdapter(boardgames, dB, currentUserId);
        recyclerView.setAdapter(adapter);
    }
}



