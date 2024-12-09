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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gamekeeper.R;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends BaseActivity {

    private DatabaseHelper dbHelper;
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

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerActivity.this, PlayerBoardgameActivity.class);
            intent.putStringArrayListExtra("player_names", addedPlayers);
            startActivity(intent);
        });
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
        dbHelper = new DatabaseHelper(this);
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

        playerNameInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        playerNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {

                autoCompleteChipGroup.removeAllViews();

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

        playerNameInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPlayer = (String) parent.getItemAtPosition(position);
            addPlayerToFinalChips(selectedPlayer);
        });

        addPlayerButton.setOnClickListener(v -> {
            String newPlayer = playerNameInput.getText().toString().trim();

            validateAndAddPlayer(newPlayer);
        });
    }

    private void loadPlayerNames() {
        playerNames.clear();
        Cursor cursor = dbHelper.getPlayersByUserId(currentUserId);

        while (cursor.moveToNext()) {
            int indexPlayerName = cursor.getColumnIndex("player_name");
            String playerName = cursor.getString(indexPlayerName);
            playerNames.add(playerName);
        }
        cursor.close();
    }

    private void addAutoCompleteChip(String playerName) {
        Chip chip = new Chip(this);
        chip.setText(playerName);
        chip.setCloseIconVisible(false);
        chip.setOnClickListener(v -> {
            validateAndAddPlayer(playerName);
        });

        autoCompleteChipGroup.addView(chip);
    }


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

    private void updatePlayButtonState() {
        if (!addedPlayers.isEmpty()) {
            playButton.setEnabled(true);
            playButton.setAlpha(1.0f);
        } else {
            playButton.setEnabled(false);
            playButton.setAlpha(0.5f);
        }
    }

    private void validateAndAddPlayer(String playerName) {
        if (playerName.isEmpty()) {
            return;
        }

        playerName = playerName.substring(0, 1).toUpperCase() + playerName.substring(1).toLowerCase();

        if (addedPlayers.contains(playerName)) {
            Toast.makeText(PlayerActivity.this, "Este jugador ya está en la lista.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (addedPlayers.size() >= 4) {
            Toast.makeText(PlayerActivity.this, "No puedes agregar más de 4 jugadores.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!playerNames.contains(playerName)) {
            boolean isInserted = dbHelper.addPlayer(currentUserId, playerName);
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