package com.example.gamekeeper.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.sampledata.CloudinaryConfig;
import com.example.gamekeeper.utils.IntentExtras;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddBoardgameActivity extends BaseActivity {

    private EditText editTextName, editTextDescription, editTextYear, editTextPlayersMax, editTextPlayersMin, editTextTimeMax, editTextTimeMin;
    private Spinner spinnerGenre1, spinnerGenre2;
    private Button buttonAddGame;
    private ImageView imageViewBoardgame;
    private DatabaseHelper dB;
    private Uri imageUri;
    private List<String> genreList = new ArrayList<>();
    private ToggleButton toggleTime ;
    private ToggleButton togglePlayer;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boardgame);

        dB = new DatabaseHelper(this);

        editTextName = findViewById(R.id.et_name);
        editTextDescription = findViewById(R.id.et_description);
        editTextYear = findViewById(R.id.et_year);
        editTextPlayersMax = findViewById(R.id.et_players_max);
        editTextPlayersMin = findViewById(R.id.et_players_min);
        editTextTimeMax = findViewById(R.id.et_time_max);
        editTextTimeMin = findViewById(R.id.et_time_min);
        spinnerGenre1 = findViewById(R.id.spinner_genre1);
        spinnerGenre2 = findViewById(R.id.spinner_genre2);
        buttonAddGame = findViewById(R.id.btn_add_game);
        imageViewBoardgame = findViewById(R.id.iv_selected_image);
        FloatingActionButton fabBack = findViewById(R.id.fab_back);
        toggleTime = findViewById(R.id.toggleTime);
        togglePlayer = findViewById(R.id.togglePlayer);
        setupGenreSpinners();
        Intent intent = getIntent();
         action = intent.getStringExtra(IntentExtras.ACTION_BUTTON);
        if (action != null && action.equals("EDIT")) {
            int gameId = intent.getIntExtra(IntentExtras.GAME_ID, -1);
            if (gameId != -1) {
                loadGameData(gameId);
            }
        }
        buttonAddGame.setText("EDIT".equals(action) ? "Actualizar Juego" : "Añadir Juego");
        String defaultImageUrl = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1734118500/default_pp4xqf.jpg";
        Glide.with(this)
                .load(defaultImageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageViewBoardgame);
        togglePlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Modo único
                    editTextPlayersMax.setVisibility(View.GONE);
                    editTextPlayersMax.setText(""); // Limpiar el campo max
                } else {
                    // Modo rango
                    editTextPlayersMax.setVisibility(View.VISIBLE);
                }
            }
        });

        toggleTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Modo único
                    editTextTimeMax.setVisibility(View.GONE);
                    editTextTimeMax.setText(""); // Limpiar el campo max
                } else {
                    // Modo rango
                    editTextTimeMax.setVisibility(View.VISIBLE);
                }
            }
        });




        buttonAddGame.setOnClickListener(v -> onSaveButtonClick());

        fabBack.setOnClickListener(v -> {
            Intent intentBack = new Intent(AddBoardgameActivity.this, SearchActivity.class);
            startActivity(intentBack);
            finish();
        });



    }
    public void onSaveButtonClick() {
        if ("EDIT".equals(action)) {
            int gameId = getIntent().getIntExtra(IntentExtras.GAME_ID, -1);
            if (gameId != -1) {
                editBoardGame(gameId);
            }
        } else if ("ADD".equals(action)) {
            addBoardGame();
        }
    }
    private boolean validateFields() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String yearString = editTextYear.getText().toString().trim();
        String playersMax = editTextPlayersMax.getText().toString().trim();
        String playersMin = editTextPlayersMin.getText().toString().trim();
        String timeMax = editTextTimeMax.getText().toString().trim();
        String timeMin = editTextTimeMin.getText().toString().trim();

        String genre1 = spinnerGenre1.getSelectedItem().toString();
        String genre2 = spinnerGenre2.getSelectedItem().toString();

        if (name.isEmpty() || description.isEmpty() || yearString.isEmpty() ||
                (!togglePlayer.isChecked() && playersMax.isEmpty()) || playersMin.isEmpty() ||
                (!toggleTime.isChecked() && timeMax.isEmpty()) || timeMin.isEmpty()) {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int yearPublished = Integer.parseInt(yearString);
            if (yearPublished <= 0 || yearPublished >= 2024) {
                throw new NumberFormatException("Año inválido");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El año debe ser un número válido y menor de 2024", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int minPlayers = Integer.parseInt(playersMin);
            int maxPlayers = togglePlayer.isChecked() ? minPlayers : Integer.parseInt(playersMax);
            if (minPlayers < 1 || maxPlayers < 1 || minPlayers > maxPlayers || maxPlayers > 100) {
                throw new NumberFormatException("Número de jugadores inválido");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Los jugadores deben ser números positivos válidos y el máximo no puede superar 100", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int minTime = Integer.parseInt(timeMin);
            int maxTime = toggleTime.isChecked() ? minTime : Integer.parseInt(timeMax);
            if (minTime < 1 || maxTime < 1 || minTime > maxTime || maxTime > 360) {
                throw new NumberFormatException("Tiempo de juego inválido");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El tiempo de juego debe ser un número positivo válido y no puede superar los 360 minutos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (genre1.equals("Selecciona un género") && genre2.equals("Selecciona un género")) {
            Toast.makeText(this, "Debes seleccionar al menos un género", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void loadGameData(int gameId) {
        Cursor cursor = null;
        SQLiteDatabase db = this.dB.getReadableDatabase();
        try {
            // Usar el método de DatabaseHelper
            cursor = this.dB.getBoardGameById(gameId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NAME);
                int photoIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_PHOTO);
                int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_DESCRIPTION);
                int yearIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_YEAR_PUBLISHED);
                int playersIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NUMBER_OF_PLAYERS);
                int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_TIME);

                if (titleIndex == -1 || photoIndex == -1 || descriptionIndex == -1 || yearIndex == -1 || playersIndex == -1 || timeIndex == -1) {
                    Toast.makeText(this, "Error: One or more columns not found in the database", Toast.LENGTH_LONG).show();
                } else {
                    String title = cursor.getString(titleIndex);
                    String photo = cursor.getString(photoIndex);
                    String description = cursor.getString(descriptionIndex);
                    int year = cursor.getInt(yearIndex);
                    String players = cursor.getString(playersIndex);
                    String time = cursor.getString(timeIndex);


                    editTextName.setText(title);
                    editTextDescription.setText(description);
                    editTextYear.setText(String.valueOf(year));


                    if (players != null && !players.isEmpty()) {
                        if (players.contains("-")) {
                            String[] playersRange = players.split("-");
                            if (playersRange.length > 1) {
                                editTextPlayersMin.setText(playersRange[0]);
                                editTextPlayersMax.setText(playersRange[1]);
                            }
                        } else {
                            editTextPlayersMin.setText(players);
                            editTextPlayersMax.setText("");
                            togglePlayer.setChecked(true);
                        }
                    }


                    if (time != null && !time.isEmpty()) {
                        time = time.replace("min", "").trim();
                        if (time.contains("-")) {
                            // Separar en función del guion si hay un rango de tiempo
                            String[] timeMultiple = time.split("-");
                            if (timeMultiple.length > 1) {
                                editTextTimeMin.setText(timeMultiple[0].trim());
                                editTextTimeMax.setText(timeMultiple[1].trim());
                            }

                        } else {
                            editTextTimeMin.setText(time.trim());
                            editTextTimeMax.setText("");

                            toggleTime.setChecked(true);
                        }
                    }

                    if (photo != null && !photo.isEmpty()) {
                        Glide.with(this)
                                .load(photo)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(imageViewBoardgame);
                    }

                    // Cargar los géneros del juego
                    List<String> genres = dB.getBoardGameGenres(gameId);
                    if (!genres.isEmpty()) {
                        String genre1 = genres.get(0);
                        spinnerGenre1.setSelection(getGenreIndex(genre1));


                        if (genres.size() > 1) {
                            String genre2 = genres.get(1);
                            spinnerGenre2.setSelection(getGenreIndex(genre2));
                        }
                    }
                    spinnerGenre1.setEnabled(false);
                    spinnerGenre2.setEnabled(false);

                }
            }
        }} catch (Exception e) {
            // Manejo de errores si es necesario
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
    private void editBoardGame(int gameId){
        Log.e("DetailActivity", "Entro en editBoardGame");
        if (!validateFields()) {
            return;
        }

        // Obtener los datos de los campos
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String yearString = editTextYear.getText().toString().trim();
        String playersMax = editTextPlayersMax.getText().toString().trim();
        String playersMin = editTextPlayersMin.getText().toString().trim();
        String timeMax = editTextTimeMax.getText().toString().trim();
        String timeMin = editTextTimeMin.getText().toString().trim();


        String players = togglePlayer.isChecked() ? playersMin : playersMin + "-" + playersMax;


        String time = toggleTime.isChecked() ? timeMin : timeMin + "-" + timeMax;


        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BOARDGAME_NAME, name);
        values.put(DatabaseHelper.COLUMN_BOARDGAME_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_BOARDGAME_YEAR_PUBLISHED, Integer.parseInt(yearString));
        values.put(DatabaseHelper.COLUMN_BOARDGAME_NUMBER_OF_PLAYERS, players);
        values.put(DatabaseHelper.COLUMN_BOARDGAME_TIME, time);

        SQLiteDatabase db = this.dB.getWritableDatabase();
        int rowsUpdated = db.update(DatabaseHelper.TABLE_BOARDGAME, values,
                DatabaseHelper.COLUMN_BOARDGAME_ID + " = ?", new String[]{String.valueOf(gameId)});

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Juego actualizado correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar el juego", Toast.LENGTH_SHORT).show();
        }
    }

    private void addBoardGame() {
        if (!validateFields()) {
            return;
        }
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int yearPublished = Integer.parseInt(editTextYear.getText().toString().trim());

        String playersMin = editTextPlayersMin.getText().toString().trim();
        String playersMax = togglePlayer.isChecked() ? playersMin : editTextPlayersMax.getText().toString().trim();
        String timeMin = editTextTimeMin.getText().toString().trim();
        String timeMax = toggleTime.isChecked() ? timeMin : editTextTimeMax.getText().toString().trim();

        String genre1 = spinnerGenre1.getSelectedItem().toString();
        String genre2 = spinnerGenre2.getSelectedItem().toString();

        String players = playersMin + (togglePlayer.isChecked() ? "" : "-" + playersMax);
        String playTime = timeMin + (toggleTime.isChecked() ? "" : "-" + timeMax) + " min";
        String defaultImageUrl = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1734118500/default_pp4xqf.jpg";
        String imageUrl = (imageUri != null) ? imageUri.toString() : defaultImageUrl;

        long boardgameId = dB.addBoardgame(name, imageUrl, description, yearPublished, players, playTime);
        if (boardgameId != -1) {
            if (!genre1.equals("Selecciona un género")) {
                dB.addBoardgameGenre(boardgameId, dB.getGenreId(genre1));
            }
            if (!genre2.equals("Selecciona un género")) {
                dB.addBoardgameGenre(boardgameId, dB.getGenreId(genre2));
            }
                Toast.makeText(this, "Juego añadido con éxito", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
        }
    }


    private int getGenreIndex(String genreName) {
        for (int i = 0; i < genreList.size(); i++) {
            if (genreList.get(i).equals(genreName)) {
                return i;
            }
        }
        return 0;
    }

    private void setupGenreSpinners() {
        Cursor cursor = dB.getAllGenres();
        genreList.clear();
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

}
