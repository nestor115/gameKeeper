package com.example.gamekeeper.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;

public class DetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvDescription, tvYear, tvPlayers, tvTime, tvGenre;
    private ImageView ivGame;
    private DatabaseHelper dB;

    private Button btnAddBoardgame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tv_TittleDetail);
        tvDescription = findViewById(R.id.tv_Description);
        tvYear = findViewById(R.id.tv_Year);
        tvPlayers = findViewById(R.id.tv_Players);
        tvTime = findViewById(R.id.tv_time);
        tvGenre = findViewById(R.id.tv_genre);
        ivGame = findViewById(R.id.iv_Game);
        btnAddBoardgame = findViewById(R.id.btn_AddBoardgame);


        dB = new DatabaseHelper(this);

        int boardGameId = getIntent().getIntExtra("BOARDGAME_ID", 1); // Obtén el ID del juego, por defecto 1
        loadBoardGameDetails(boardGameId);

        btnAddBoardgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del usuario actual
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                int userId = sharedPreferences.getInt("user_id", -1); // -1 es el valor por defecto si no se encuentra


                if (userId != -1) {
                    boolean added = dB.addUserBoardgame(userId, boardGameId);
                    if (added) {
                        Toast.makeText(DetailActivity.this, "Juego añadido a tu colección", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Error al añadir el juego a tu colección", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Usuario no identificado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadBoardGameDetails(int id) {
        SQLiteDatabase db = dB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BOARDGAME + " WHERE " + DatabaseHelper.COLUMN_BOARDGAME_ID + " = ?", new String[]{String.valueOf(id)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Imprime los nombres de las columnas
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames) {
                    Log.d("DetailActivity", "Column: " + columnName);
                }

                // Verifica que los nombres de las columnas sean correctos
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
                    byte[] photo = cursor.getBlob(photoIndex);
                    String description = cursor.getString(descriptionIndex);
                    int year = cursor.getInt(yearIndex);
                    String players = cursor.getString(playersIndex);
                    String time = cursor.getString(timeIndex);

                    tvTitle.setText(title);

                    if (photo != null && photo.length > 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                        ivGame.setImageBitmap(bitmap);
                    } else {
                        // Carga la imagen desde los recursos
                        ivGame.setImageResource(R.drawable.ic_launcher_foreground);
                    }

                    tvDescription.setText(description);
                    tvYear.setText(String.valueOf(year));
                    tvPlayers.setText(players);
                    tvTime.setText(time);

                    // Asigna el género manualmente si no existe en la base de datos
                    String genre = getBoardGameGenre(id);
                    if (genre == null || genre.isEmpty()) {
                        tvGenre.setText("Estrategia");
                    } else {
                        tvGenre.setText(genre);
                    }
                }
            }
            cursor.close();
        }
    }

    private String getBoardGameGenre(int boardGameId) {
        SQLiteDatabase db = dB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT g." + DatabaseHelper.COLUMN_GENRE_NAME +
                " FROM " + DatabaseHelper.TABLE_GENRE + " g " +
                " JOIN " + DatabaseHelper.TABLE_BOARDGAME_GENRE + " bg " +
                " ON g." + DatabaseHelper.COLUMN_GENRE_ID + " = bg." + DatabaseHelper.COLUMN_G_ID +
                " WHERE bg." + DatabaseHelper.COLUMN_BG_ID + " = ?", new String[]{String.valueOf(boardGameId)});

        String genre = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int genreIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GENRE_NAME);
                if (genreIndex != -1) {
                    genre = cursor.getString(genreIndex);
                } else {
                    Log.e("DetailActivity", "Column index not found for genre");
                }
            }
            cursor.close();
        }
        return genre;
    }

}