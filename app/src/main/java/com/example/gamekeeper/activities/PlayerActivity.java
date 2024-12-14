package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.utils.IntentExtras;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class PlayerActivity extends BaseActivity {

    private DatabaseHelper dB;
    private AutoCompleteTextView playerNameInput;
    private ChipGroup playerChipGroup;
    private ChipGroup autoCompleteChipGroup;
    private Button addPlayerButton;
    private Button playButton;
    private ArrayList<String> playerNames;
    private ArrayList<String> addedPlayers;
    private int currentUserId;
    private static final int MAX_AUTOCOMPLETE_CHIPS = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playButton = findViewById(R.id.play_button);
        playButton.setEnabled(false);
        playButton.setAlpha(0.5f);
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
        //Boton para ir a la vista de juegos jugados
        playButton.setOnClickListener(v -> {
            //Si no existen juegos en la colección, no se puede ir a la siguiente view
            if (dB.hasGamesForUser(currentUserId)) {
                Intent intent = new Intent(PlayerActivity.this, PlayerBoardgameActivity.class);
                intent.putStringArrayListExtra(IntentExtras.PLAYER_NAMES, addedPlayers);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(PlayerActivity.this, "No hay juegos en la colección", Toast.LENGTH_SHORT).show();
            }
        });

        dB = new DatabaseHelper(this);
        playerNameInput = findViewById(R.id.player_name_input);
        playerChipGroup = findViewById(R.id.player_chip_group);
        autoCompleteChipGroup = findViewById(R.id.auto_complete_chip_group);
        addPlayerButton = findViewById(R.id.add_player_button);

        playerNames = new ArrayList<>();
        addedPlayers = new ArrayList<>();

        playerChipGroup.removeAllViews();
        loadPlayerNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, playerNames);
        playerNameInput.setAdapter(adapter);

        playerNameInput.setDropDownHeight(0);
        playerNameInput.setDropDownWidth(0);
        //Los nombres de los jugadores solo pueden tener 20 caracteres
        playerNameInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        // Añade un TextWatcher para detectar cambios en el campo de texto playerNameInput
        playerNameInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            //Se llama cuando el texto cambia
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                //Limpia los chips de autocompletado
                autoCompleteChipGroup.removeAllViews();
                //Si se ha escrito al menos un caracter en el campo de texto, se muestran los chips de autocompletado
                if (charSequence.length() > 0) {
                    int chipsAdded = 0;
                    for (String playerName : playerNames) {
                        if (playerName.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                            if (chipsAdded >= MAX_AUTOCOMPLETE_CHIPS) break;
                            addAutoCompleteChip(playerName);
                            chipsAdded++;
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //Añade el jugador del chip de autocompletado a la lista de jugadores
        playerNameInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPlayer = (String) parent.getItemAtPosition(position);
            addPlayerToFinalChips(selectedPlayer);
        });
        //Añade el jugador del input a la lista de jugadores
        addPlayerButton.setOnClickListener(v -> {
            String newPlayer = playerNameInput.getText().toString().trim();

            validateAndAddPlayer(newPlayer);
        });
    }

    //Carga los nombres de los jugadores de la base de datos
    private void loadPlayerNames() {
        playerNames.clear();
        Cursor cursor = dB.getPlayersByUserId(currentUserId);

        while (cursor.moveToNext()) {
            int indexPlayerName = cursor.getColumnIndex("player_name");
            String playerName = cursor.getString(indexPlayerName);
            playerNames.add(playerName);
        }
        cursor.close();
    }

    //Añade un chip de autocompletado a la vista
    private void addAutoCompleteChip(String playerName) {
        Chip chip = new Chip(this);
        chip.setText(playerName);
        chip.setCloseIconVisible(false);
        chip.setOnClickListener(v -> {
            validateAndAddPlayer(playerName);
        });

        autoCompleteChipGroup.addView(chip);
    }

    //Añade un chip de jugador a la vista, si no se han llegado a 4 jugadores
    private void addPlayerToFinalChips(String playerName) {
        if (!addedPlayers.contains(playerName) && addedPlayers.size() < 4) {
            addedPlayers.add(playerName);

            Chip chip = new Chip(this);
            chip.setText(playerName);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {

                addedPlayers.remove(playerName);
                playerChipGroup.removeView(chip);

                updatePlayButtonState();
            });

            playerChipGroup.addView(chip);
        }

        updatePlayButtonState();
    }

    //Actualiza el estado del botón jugar, si no hay jugadores se desactiva, si hay se activa
    private void updatePlayButtonState() {
        if (!addedPlayers.isEmpty()) {
            playButton.setEnabled(true);
            playButton.setAlpha(1.0f);
        } else {
            playButton.setEnabled(false);
            playButton.setAlpha(0.5f);
        }
    }

    //Valida si el nombre del jugador es válido y lo añade a la lista de jugadores
    private void validateAndAddPlayer(String playerName) {
        if (playerName.isEmpty()) {
            return;
        }

        playerName = playerName.substring(0, 1).toUpperCase() + playerName.substring(1).toLowerCase();
        //Comprueba si el jugador ya está en la lista
        if (addedPlayers.contains(playerName)) {
            Toast.makeText(PlayerActivity.this, "Este jugador ya está en la lista.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Comprueba si se ha llegado a 4 jugadores
        if (addedPlayers.size() >= 4) {
            Toast.makeText(PlayerActivity.this, "No puedes agregar más de 4 jugadores.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Añade el jugador a la base de datos
        if (!playerNames.contains(playerName)) {
            boolean isInserted = dB.addPlayer(currentUserId, playerName);
            if (isInserted) {
                Toast.makeText(PlayerActivity.this, "Jugador añadido a la base de datos.", Toast.LENGTH_SHORT).show();
                playerNames.add(playerName);
            } else {
                Toast.makeText(PlayerActivity.this, "Error al agregar jugador a la base de datos.", Toast.LENGTH_SHORT).show();
            }
        }

        addPlayerToFinalChips(playerName);

        playerNameInput.setText("");
    }

}