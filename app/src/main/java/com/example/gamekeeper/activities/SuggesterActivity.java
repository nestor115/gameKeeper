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
import com.example.gamekeeper.utils.IntentExtras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SuggesterActivity extends BaseActivity {

    private DatabaseHelper dbHelper;
    private TextView gameNameTextView;
    private TextView gameOrderTextView;
    private ImageView gameImageView;
    private int currentUserId;
    private ArrayList<String> playerNames;
    private List<ListElement> shownNewGames = new ArrayList<>();
    private List<ListElement> shownPlayedGames = new ArrayList<>();
    private int newGameCounter = 0;
    private int playedGameCounter = 0;
    private int randomGameCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggester);

        dbHelper = new DatabaseHelper(this);
        gameNameTextView = findViewById(R.id.gameNameTextView);
        gameImageView = findViewById(R.id.gameImageView);
        gameOrderTextView = findViewById(R.id.gameOrderTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        Button newGameButton = findViewById(R.id.newGameButton);
        Button playedGameButton = findViewById(R.id.playedGameButton);
        Button randomButton = findViewById(R.id.randomButton);
        Intent intent = getIntent();
        playerNames = intent.getStringArrayListExtra(IntentExtras.SELECTED_PLAYERS);
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

    //Devuelve un juego al azar
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

                // Contar cuántos jugadores han jugado este juego
                int playedCount = 0;

                // Suponiendo que 'playerNames' contiene los nombres de los jugadores
                for (String playerName : playerNames) {
                    int playerId = dbHelper.getPlayerIdByName(playerName, currentUserId);
                    if (playerId != -1) {
                        boolean hasPlayed = dbHelper.hasPlayerPlayedGame(playerId, randomGame.getId());
                        if (hasPlayed) {
                            playedCount++;
                        }
                    }
                }

                randomGameCounter++;
                gameNameTextView.setText(randomGame.getName());
                Glide.with(this).load(randomGame.getImage()).into(gameImageView);
                gameOrderTextView.setText(randomGameCounter + " - " + "Juego aleatorio (" + playedCount + ") jugadores");

                if (shownNewGames.size() + shownPlayedGames.size() >= userGames.size()) {
                    shownNewGames.clear();
                    shownPlayedGames.clear();
                    randomGameCounter = 0;  // Reinicia el contador de juegos aleatorios
                    Log.d("RandomButton", "Todos los juegos aleatorios han sido mostrados. Reiniciando contador.");
                }
            }
        } else if (userGames.size() == 1) {
            ListElement game = userGames.get(0);

            // Contar cuántos jugadores han jugado este juego
            int playedCount = 0;

            // Suponiendo que 'playerNames' contiene los nombres de los jugadores
            for (String playerName : playerNames) {
                int playerId = dbHelper.getPlayerIdByName(playerName, currentUserId);
                if (playerId != -1) {
                    boolean hasPlayed = dbHelper.hasPlayerPlayedGame(playerId, game.getId());
                    if (hasPlayed) {
                        playedCount++;
                    }
                }
            }

            randomGameCounter++;
            gameNameTextView.setText(game.getName());
            Glide.with(this).load(game.getImage()).into(gameImageView);
            gameOrderTextView.setText(randomGameCounter + " - " + "Juego aleatorio (" + playedCount + ")");
        }
    }



    //Devuelve un juego el cual no ha sido jugado por nadie o por la mayor parte de jugadores
    private void showNewGame() {
        List<ListElement> userGames = dbHelper.getUserBoardgames(currentUserId);
        List<Integer> playerIds = new ArrayList<>();

        for (String playerName : playerNames) {
            int playerId = dbHelper.getPlayerIdByName(playerName, currentUserId);
            if (playerId != -1) {
                playerIds.add(playerId);
            }
        }


        List<ListElement> unplayedByAll = new ArrayList<>();
        List<ListElement> unplayedByThree = new ArrayList<>();
        List<ListElement> unplayedByTwo = new ArrayList<>();
        List<ListElement> unplayedByOne = new ArrayList<>();
        List<ListElement> remainingGames = new ArrayList<>();

        for (ListElement game : userGames) {
            if (shownNewGames.contains(game)) {
                continue;
            }

            int gameId = game.getId();
            int unplayedCount = 0;

            for (int playerId : playerIds) {
                boolean hasPlayed = dbHelper.hasPlayerPlayedGame(playerId, gameId);
                if (!hasPlayed) {
                    unplayedCount++;
                }
            }

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
                default:
                    remainingGames.add(game);
                    break;
            }
        }

        Collections.shuffle(unplayedByAll);
        Collections.shuffle(unplayedByThree);
        Collections.shuffle(unplayedByTwo);
        Collections.shuffle(unplayedByOne);
        Collections.shuffle(remainingGames);


        ListElement selectedGame = null;

        if (!unplayedByAll.isEmpty()) {
            selectedGame = unplayedByAll.remove(0);
            Log.d("NewGameButton", "Seleccionado de 'No jugado por nadie'");
        } else if (!unplayedByThree.isEmpty()) {
            selectedGame = unplayedByThree.remove(0);
            Log.d("NewGameButton", "Seleccionado de 'No jugado por tres'");
        } else if (!unplayedByTwo.isEmpty()) {
            selectedGame = unplayedByTwo.remove(0);
            Log.d("NewGameButton", "Seleccionado de 'No jugado por dos'");
        } else if (!unplayedByOne.isEmpty()) {
            selectedGame = unplayedByOne.remove(0);
            Log.d("NewGameButton", "Seleccionado de 'No jugado por uno'");
        } else if (!remainingGames.isEmpty()) {
            selectedGame = remainingGames.remove(0);
            Log.d("NewGameButton", "Seleccionado de 'Cualquier juego restante'");
        } else {
            // Reiniciar la lista si todos los juegos han sido mostrados
            Log.d("NewGameButton", "Todos los juegos han sido mostrados. Reiniciando lista.");
            shownNewGames.clear();
            newGameCounter=0;
            showNewGame();
            return;
        }

        // Mostrar el juego seleccionado
        if (selectedGame != null) {
            int playedCount = 0;
            for (int playerId : playerIds) {
                boolean hasPlayed = dbHelper.hasPlayerPlayedGame(playerId, selectedGame.getId());
                if (hasPlayed) {
                    playedCount++;
                }
            }
            gameNameTextView.setText(selectedGame.getName());
            Glide.with(this).load(selectedGame.getImage()).into(gameImageView);

            shownNewGames.add(selectedGame);
            newGameCounter++;  // Incrementa el contador de juegos nuevos
            gameOrderTextView.setText(newGameCounter + " - " + "Juego menos jugado (" + playedCount + ") jugadores");
            Log.d("NewGameButton", "Juego mostrado: " + selectedGame.getName());
        } else {
            Log.d("NewGameButton", "No hay juegos disponibles para mostrar.");
        }
    }

    //Devuelve un juego el cual ha sido jugado por todos los jugadores o por la mayor parte de jugadores
    private void showPlayedGame() {
        List<ListElement> userGames = dbHelper.getUserBoardgames(currentUserId);
        List<Integer> playerIds = new ArrayList<>();

        for (String playerName : playerNames) {
            int playerId = dbHelper.getPlayerIdByName(playerName, currentUserId);
            if (playerId != -1) {
                playerIds.add(playerId);
            }
        }

        ListElement currentGame = getCurrentGame();

        List<ListElement> playedByAll = new ArrayList<>();
        List<ListElement> playedByThree = new ArrayList<>();
        List<ListElement> playedByTwo = new ArrayList<>();
        List<ListElement> playedByOne = new ArrayList<>();
        List<ListElement> unplayedByAll = new ArrayList<>();

        for (ListElement game : userGames) {
            if (shownPlayedGames.contains(game)) {
                continue; // Excluye los juegos ya mostrados
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
                default:
                    unplayedByAll.add(game);
                    break;
            }
        }

        Collections.shuffle(playedByAll);
        Collections.shuffle(playedByThree);
        Collections.shuffle(playedByTwo);
        Collections.shuffle(playedByOne);
        Collections.shuffle(unplayedByAll);

        // Más jugadores que lo han jugado primero
        ListElement selectedGame = null;
        if (!playedByAll.isEmpty()) {
            selectedGame = playedByAll.remove(0);
        } else if (!playedByThree.isEmpty()) {
            selectedGame = playedByThree.remove(0);
        } else if (!playedByTwo.isEmpty()) {
            selectedGame = playedByTwo.remove(0);
        } else if (!playedByOne.isEmpty()) {
            selectedGame = playedByOne.remove(0);
        } else if (!unplayedByAll.isEmpty()) {
            selectedGame = unplayedByAll.remove(0);
        }

        // Si se han mostrado todos los juegos, reiniciar la lista
        if (selectedGame == null && !userGames.isEmpty()) {
            Log.d("PlayedGameButton", "Reiniciando lista de juegos mostrados.");
            shownPlayedGames.clear();
            playedGameCounter=0;
            showPlayedGame();
            return;
        }

        if (selectedGame != null) {
            int playedCount = 0;
            for (int playerId : playerIds) {
                boolean hasPlayed = dbHelper.hasPlayerPlayedGame(playerId, selectedGame.getId());
                if (hasPlayed) {
                    playedCount++;
                }
            }
            shownPlayedGames.add(selectedGame);
            gameNameTextView.setText(selectedGame.getName());
            playedGameCounter++;
            gameOrderTextView.setText(playedGameCounter + " - " + "Juego mas jugado " + "("+  playedCount   +") jugadores");
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
// kittens 4 unstable unicorns 3 bang 2 pelusas 1 picnic 1 rebel princes 0 samoa 0
//samoa 0 rebel princess 1 picnic 1