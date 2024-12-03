package com.example.gamekeeper.activities;

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

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomGame();
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
