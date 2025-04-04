package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.utils.IntentExtras;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class DetailActivity extends BaseActivity {
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
        //Cojo los datos del intent
        int boardGameId = getIntent().getIntExtra(IntentExtras.BOARDGAME_ID, 1);
        String nameView = getIntent().getStringExtra(IntentExtras.NAME_VIEW);
        //Si el intent viene de la pagina de buscar, se muestra el boton de añadir, sino se oculta
        if ("SEARCH".equals(nameView)) {
            btnAddBoardgame.setVisibility(View.VISIBLE);
            btnAddBoardgame.setEnabled(true);
        } else if ("HOME".equals(nameView)) {
            btnAddBoardgame.setVisibility(View.INVISIBLE);
            btnAddBoardgame.setEnabled(false);
        }
        loadBoardGameDetails(boardGameId);
        FloatingActionButton fabBack = findViewById(R.id.fab_back);
        //Boton para volver a la pagina anterior
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if ("SEARCH".equals(nameView)) {
                    intent = new Intent(DetailActivity.this, SearchActivity.class);
                } else if ("HOME".equals(nameView)) {
                    intent = new Intent(DetailActivity.this, HomeActivity.class);
                } else {
                    intent = new Intent(DetailActivity.this, HomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });
        //Boton para añadir el juego a la colección
        btnAddBoardgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                int userId = sharedPreferences.getInt("user_id", -1);
                if (userId != -1) {
                    boolean added = dB.addUserBoardgame(userId, boardGameId);
                    if (added) {
                        Toast.makeText(DetailActivity.this, "Juego añadido a tu colección", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailActivity.this, SearchActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(DetailActivity.this, "Ese juego ya existe en tu colección", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Usuario no identificado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Carga los datos del juego desde la base de datos
    private void loadBoardGameDetails(int id) {
        Cursor cursor = dB.getGameData(id);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames) {
                    Log.d("DetailActivity", "Column: " + columnName);
                }

                int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NAME);
                int photoIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_PHOTO);
                int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_DESCRIPTION);
                int yearIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_YEAR_PUBLISHED);
                int playersIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NUMBER_OF_PLAYERS);
                int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_TIME);
                //Compruebo que los datos existan en la base de datos
                if (titleIndex == -1 || photoIndex == -1 || descriptionIndex == -1 || yearIndex == -1 || playersIndex == -1 || timeIndex == -1) {
                    Toast.makeText(this, "Datos no encontrados", Toast.LENGTH_LONG).show();
                } else {
                    String title = cursor.getString(titleIndex);
                    String photo = cursor.getString(photoIndex);
                    String description = cursor.getString(descriptionIndex);
                    int year = cursor.getInt(yearIndex);
                    String players = cursor.getString(playersIndex);
                    String time = cursor.getString(timeIndex);

                    tvTitle.setText(title);

                    if (photo != null && !photo.isEmpty()) {
                        Glide.with(this)
                                .load(photo)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(ivGame);
                    } else {
                        ivGame.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                    String yearStr = "" + year;

                    tvDescription.setText(description);
                    tvYear.setText(yearStr);
                    tvPlayers.setText(players);
                    tvTime.setText(time);

                    List<String> genres = dB.getBoardGameGenres(id);

                    if (genres.size() == 2) {
                        tvGenre.setText( genres.get(0) + "/" + genres.get(1));
                    } else if (genres.size() == 1) {
                        tvGenre.setText(genres.get(0));
                    }
                }
            }
            cursor.close();
        }
    }

}