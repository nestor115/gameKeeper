package com.example.gamekeeper.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AddBoardgameActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextYear, editTextPlayers, editTextTime;
    private Spinner spinnerGenre1, spinnerGenre2;
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
        spinnerGenre1 = findViewById(R.id.spinner_genre1);
        spinnerGenre2 = findViewById(R.id.spinner_genre2);
        buttonAddGame = findViewById(R.id.btn_add_game);

        setupGenreSpinners();
        buttonAddGame.setOnClickListener(v -> addBoardGame());
    }

    private void setupGenreSpinners(){
        Cursor cursor = db.getAllGenres();
        List<String> genreList = new ArrayList<>();
        genreList.add("Selecciona un género");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int columnIndexName = cursor.getColumnIndex(DatabaseHelper.COLUMN_GENRE_NAME);
                String genreName = cursor.getString(columnIndexName);
                if (genreName != null) {
                    genreList.add(genreName);
                } else {
                    Log.e("SETUP_GENRE_SPINNERS", "Género nulo encontrado");
                }
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Log.e("SETUP_GENRE_SPINNERS", "El cursor es nulo o no contiene datos");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre1.setAdapter(adapter);
        spinnerGenre2.setAdapter(adapter);
    }

    private void addBoardGame() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String yearString = editTextYear.getText().toString().trim();
        String numberOfPlayers = editTextPlayers.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();

        String genre1 = spinnerGenre1.getSelectedItem().toString();
        String genre2 = spinnerGenre2.getSelectedItem().toString();


        if (name.isEmpty() || description.isEmpty() || yearString.isEmpty() || numberOfPlayers.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        int yearPublished;
        try {
            yearPublished = Integer.parseInt(yearString);
            if (yearPublished <= 0) {
                throw new NumberFormatException("Año inválido");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El año debe ser un número positivo válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (genre1.equals("Selecciona un género") && genre2.equals("Selecciona un género")) {
            Toast.makeText(this, "Debes seleccionar al menos un género", Toast.LENGTH_SHORT).show();
            return;
        }


        long boardgameId = db.addBoardgame(name, null, description, yearPublished, numberOfPlayers, time);
        if (boardgameId != -1) {
            Toast.makeText(this, "Juego añadido con éxito", Toast.LENGTH_SHORT).show();


            if (!genre1.equals("Selecciona un género")) {
                int genreId1 = db.getGenreId(genre1);
                if (genreId1 != -1) {
                    db.addBoardgameGenre(boardgameId, genreId1);
                } else {
                    Log.e("ADD_BOARD_GAME", "ID de género no encontrado para: " + genre1);
                }
            }
            if (!genre2.equals("Selecciona un género") && !genre2.equals(genre1)) {
                int genreId2 = db.getGenreId(genre2);
                if (genreId2 != -1) {
                    db.addBoardgameGenre(boardgameId, genreId2);
                } else {
                    Log.e("ADD_BOARD_GAME", "ID de género no encontrado para: " + genre2);
                }
            }

            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

            finish();
        } else {
            Toast.makeText(this, "Error al añadir el juego", Toast.LENGTH_SHORT).show();
        }
    }
}