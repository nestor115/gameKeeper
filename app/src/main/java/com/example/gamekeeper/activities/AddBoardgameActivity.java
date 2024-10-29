package com.example.gamekeeper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;

public class AddBoardgameActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextYear, editTextPlayers, editTextTime;
    private Button buttonAddGame;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boardgame);

        db = new DatabaseHelper(this);

        editTextName = findViewById(R.id.et_name);
        editTextDescription = findViewById(R.id.et_description);
        editTextYear = findViewById(R.id.et_year);
        editTextPlayers = findViewById(R.id.et_players);
        editTextTime = findViewById(R.id.et_time);
        buttonAddGame = findViewById(R.id.btn_add_game);

        buttonAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBoardGame();
            }
        });
    }

    private void addBoardGame() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int yearPublished = Integer.parseInt(editTextYear.getText().toString().trim());
        String numberOfPlayers = editTextPlayers.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();

        boolean result = db.addBoardgame(name, null, description, yearPublished, numberOfPlayers, time);
        if (result) {
            Toast.makeText(this, "Juego añadido con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al añadir el juego", Toast.LENGTH_SHORT).show();
        }
    }
}