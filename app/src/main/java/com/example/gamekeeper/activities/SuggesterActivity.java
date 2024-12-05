package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuggesterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView gameNameTextView;
    private ImageView gameImageView;
    private int currentUserId;
    private ArrayList<String> playerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggester);

        dbHelper = new DatabaseHelper(this);
        gameNameTextView = findViewById(R.id.gameNameTextView);
        gameImageView = findViewById(R.id.gameImageView);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        Button newGameButton = findViewById(R.id.newGameButton);
        Button playedGameButton = findViewById(R.id.playedGameButton);
        Button randomButton = findViewById(R.id.randomButton);
        Intent intent = getIntent();
        playerNames = intent.getStringArrayListExtra("selected_players");
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomGame();
            }
        });
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewGame();
            }
        });
        playedGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayedGame();
            }
        });


    }

    private void showRandomGame() {
        List<ListElement> userGames = dbHelper.getUserBoardgames(currentUserId);

        if (userGames.size() > 1) {
            Random random = new Random();
            ListElement currentGame = getCurrentGame();
            List<ListElement> filteredGames = new ArrayList<>(userGames);

            if (currentGame != null) {

                filteredGames.remove(currentGame);
            }

            if (!filteredGames.isEmpty()) {
                int randomIndex = random.nextInt(filteredGames.size());
                ListElement randomGame = filteredGames.get(randomIndex);
                gameNameTextView.setText(randomGame.getName());
                Glide.with(this).load(randomGame.getImage()).into(gameImageView);
            }
        } else if (userGames.size() == 1) {
            ListElement game = userGames.get(0);

            gameNameTextView.setText(game.getName());
            Glide.with(this).load(game.getImage()).into(gameImageView);
        }
    }
    private void showNewGame() {
        List<ListElement> userGames = dbHelper.getUserBoardgames(currentUserId);
        List<Integer> playerIds = new ArrayList<>();

        // Obtener los IDs de los jugadores seleccionados
        for (String playerName : playerNames) {
            int playerId = dbHelper.getPlayerIdByName(playerName, currentUserId);
            if (playerId != -1) {
                playerIds.add(playerId);
            }
        }

        // Juego actualmente mostrado
        ListElement currentGame = getCurrentGame();

        // Listas para juegos filtrados por número de jugadores que no lo han jugado
        List<ListElement> unplayedByAll = new ArrayList<>();
        List<ListElement> unplayedByThree = new ArrayList<>();
        List<ListElement> unplayedByTwo = new ArrayList<>();
        List<ListElement> unplayedByOne = new ArrayList<>();

        for (ListElement game : userGames) {
            if (currentGame != null && game.getId() == currentGame.getId()) {
                continue; // Excluir el juego actualmente mostrado
            }

            int gameId = game.getId();
            int unplayedCount = 0;

            for (int playerId : playerIds) {
                boolean hasPlayed = dbHelper.hasPlayerPlayedGame(playerId, gameId);
                if (!hasPlayed) {
                    unplayedCount++;
                }
            }

            // Clasificación según jugadores que no lo han jugado
            switch (unplayedCount) {
                case 4:
                    unplayedByAll.add(game);
                    break;
                case 3:
                    unplayedByThree.add(game);
                    break;
                case 2:
                    unplayedByTwo.add(game);
                    break;
                case 1:
                    unplayedByOne.add(game);
                    break;
            }
        }

        Random random = new Random();
        ListElement selectedGame = null;

        // Prioridad de selección: más jugadores que no lo han jugado primero
        if (!unplayedByAll.isEmpty()) {
            selectedGame = unplayedByAll.get(random.nextInt(unplayedByAll.size()));
            Log.d("NewGameButton", "Seleccionado de 'No jugado por nadie'");
        } else if (!unplayedByThree.isEmpty()) {
            selectedGame = unplayedByThree.get(random.nextInt(unplayedByThree.size()));
            Log.d("NewGameButton", "Seleccionado de 'No jugado por tres'");
        } else if (!unplayedByTwo.isEmpty()) {
            selectedGame = unplayedByTwo.get(random.nextInt(unplayedByTwo.size()));
            Log.d("NewGameButton", "Seleccionado de 'No jugado por dos'");
        } else if (!unplayedByOne.isEmpty()) {
            selectedGame = unplayedByOne.get(random.nextInt(unplayedByOne.size()));
            Log.d("NewGameButton", "Seleccionado de 'No jugado por uno'");
        } else if (!userGames.isEmpty()) {
            // Excluir nuevamente el juego actual en la selección general
            List<ListElement> filteredGames = new ArrayList<>(userGames);
            if (currentGame != null) {
                filteredGames.remove(currentGame);
            }
            if (!filteredGames.isEmpty()) {
                selectedGame = filteredGames.get(random.nextInt(filteredGames.size()));
            }
            Log.d("NewGameButton", "Seleccionado de 'Cualquier juego aleatorio'");
        }

        // Mostrar el juego seleccionado
        if (selectedGame != null) {
            gameNameTextView.setText(selectedGame.getName());
            Glide.with(this).load(selectedGame.getImage()).into(gameImageView);

            Log.d("NewGameButton", "Juego mostrado: " + selectedGame.getName());
        } else {
            Log.d("NewGameButton", "No hay juegos disponibles para mostrar.");
        }
    }

    private void showPlayedGame() {
        List<ListElement> userGames = dbHelper.getUserBoardgames(currentUserId);
        List<Integer> playerIds = new ArrayList<>();

        // Obtener los IDs de los jugadores seleccionados
        for (String playerName : playerNames) {
            int playerId = dbHelper.getPlayerIdByName(playerName, currentUserId);
            if (playerId != -1) {
                playerIds.add(playerId);
            }
        }

        // Juego actualmente mostrado
        ListElement currentGame = getCurrentGame();

        // Listas para juegos filtrados por número de jugadores que lo han jugado
        List<ListElement> playedByAll = new ArrayList<>();
        List<ListElement> playedByThree = new ArrayList<>();
        List<ListElement> playedByTwo = new ArrayList<>();
        List<ListElement> playedByOne = new ArrayList<>();

        for (ListElement game : userGames) {
            if (currentGame != null && game.getId() == currentGame.getId()) {
                continue; // Excluir el juego actualmente mostrado
            }

            int gameId = game.getId();
            int playedCount = 0;

            for (int playerId : playerIds) {
                boolean hasPlayed = dbHelper.hasPlayerPlayedGame(playerId, gameId);
                if (hasPlayed) {
                    playedCount++;
                }
            }

            // Clasificación según jugadores que lo han jugado
            switch (playedCount) {
                case 4:
                    playedByAll.add(game);
                    break;
                case 3:
                    playedByThree.add(game);
                    break;
                case 2:
                    playedByTwo.add(game);
                    break;
                case 1:
                    playedByOne.add(game);
                    break;
            }
        }

        Random random = new Random();
        ListElement selectedGame = null;

        // Prioridad de selección: más jugadores que lo han jugado primero
        if (!playedByAll.isEmpty()) {
            selectedGame = playedByAll.get(random.nextInt(playedByAll.size()));
            Log.d("PlayedGameButton", "Seleccionado de 'Jugado por todos'");
        } else if (!playedByThree.isEmpty()) {
            selectedGame = playedByThree.get(random.nextInt(playedByThree.size()));
            Log.d("PlayedGameButton", "Seleccionado de 'Jugado por tres'");
        } else if (!playedByTwo.isEmpty()) {
            selectedGame = playedByTwo.get(random.nextInt(playedByTwo.size()));
            Log.d("PlayedGameButton", "Seleccionado de 'Jugado por dos'");
        } else if (!playedByOne.isEmpty()) {
            selectedGame = playedByOne.get(random.nextInt(playedByOne.size()));
            Log.d("PlayedGameButton", "Seleccionado de 'Jugado por uno'");
        } else if (!userGames.isEmpty()) {
            // Excluir nuevamente el juego actual en la selección general
            List<ListElement> filteredGames = new ArrayList<>(userGames);
            if (currentGame != null) {
                filteredGames.remove(currentGame);
            }
            if (!filteredGames.isEmpty()) {
                selectedGame = filteredGames.get(random.nextInt(filteredGames.size()));
            }
            Log.d("PlayedGameButton", "Seleccionado de 'Cualquier juego aleatorio'");
        }

        // Mostrar el juego seleccionado
        if (selectedGame != null) {
            gameNameTextView.setText(selectedGame.getName());
            Glide.with(this).load(selectedGame.getImage()).into(gameImageView);

            Log.d("PlayedGameButton", "Juego mostrado: " + selectedGame.getName());
        } else {
            Log.d("PlayedGameButton", "No hay juegos disponibles para mostrar.");
        }
    }



    private ListElement getCurrentGame() {
        String currentGameName = gameNameTextView.getText().toString();
        if (!currentGameName.isEmpty()) {
            for (ListElement game : dbHelper.getUserBoardgames(currentUserId)) {
                if (game.getName().equals(currentGameName)) {
                    return game;
                }
            }
        }
        return null;
    }
}
