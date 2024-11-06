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

        // Verificar si es el primer inicio
        if (isFirstLaunch()) {
            insertData(); // Solo se ejecuta si es el primer inicio
            setFirstLaunchFlag(); // Marca que la app ya se ha lanzado
        }

        buttonGoSearch = findViewById(R.id.buttonGoSearch);
        dB = new DatabaseHelper(this);

        buttonGoSearch.setOnClickListener(v -> {


            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();

        });
    }
    // Método para verificar si es la primera vez que se lanza la app
    private boolean isFirstLaunch() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getBoolean("first_launch", true);
    }

    // Método para establecer que la app ya fue lanzada
    private void setFirstLaunchFlag() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_launch", false); // Marca que no es el primer inicio
        editor.apply();
    }

    private void insertData() {
        dB.addGenre("Estrategia");
        dB.addGenre("Rol");
        dB.addGenre("Cooperativo");
        dB.addGenre("Cartas");
        dB.addGenre("Construcción de mazos");
        dB.addGenre("Construcción");
        dB.addGenre("Party");
        dB.addGenre("Draft");
        dB.addGenre("Familiar");
        dB.addGenre("Deducción");
        dB.addGenre("Palabras");
        dB.addGenre("Trivia");
        dB.addGenre("Colocación de trabajadores");
        dB.addGenre("Wargames");
        dB.addGenre("Miniaturas");
        dB.addGenre("Tablero abstractos");
        dB.addGenre("Economía");
        dB.addGenre("Aventura");
        dB.addGenre("Gestión de recursos");
        dB.addGenre("Set collection");
        dB.addGenre("Negociación");
        dB.addGenre("Memoria");
        dB.addGenre("Habilidad/destreza");
        dB.addGenre("Partida Rápida");
        dB.addGenre("Role-playing (RPG)");
        dB.addGenre("Tiradas de Dados");
        dB.addGenre("Colocación de Losetas");
        dB.addGenre("Temáticos");

        long gameId;

        gameId = dB.addBoardgame("Exploding Kittens", null, "Un juego de cartas de estrategia y humor en el que los jugadores deben evitar explotar.", 2015, "2-5", "15 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("Sushi Go!", null, "Un juego de cartas de draft rápido en el que los jugadores intentan crear la mejor combinación de sushi.", 2013, "2-5", "15 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Draft"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("The Mind", null, "Un juego cooperativo donde los jugadores intentan sincronizar sus cartas en orden ascendente sin comunicación verbal.", 2018, "2-4", "20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cooperativo"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("Catan", null, "Un juego de estrategia donde los jugadores intentan construir y gestionar colonias en la isla de Catan.", 1995, "3-4", "60-120 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Construcción"));
        gameId = dB.addBoardgame("Cubirds", null, "Un juego de cartas en el que los jugadores intentan recolectar bandadas de aves colocando cartas estratégicamente.", 2018, "2-5", "20-30 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        gameId = dB.addBoardgame("Unstable Unicorns", null, "Un juego de cartas en el que los jugadores construyen un ejército de unicornios y usan su magia para sabotear a los demás.", 2017, "2-8", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        gameId = dB.addBoardgame("Virus", null, "Un juego de cartas en el que los jugadores intentan infectar a otros mientras protegen sus órganos sanos.", 2015, "2-6", "20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        gameId = dB.addBoardgame("Here to Slay", null, "Un juego de cartas estratégico y competitivo en el que los jugadores forman un equipo de héroes y luchan contra monstruos.", 2020, "2-6", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
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



