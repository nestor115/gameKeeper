package com.example.gamekeeper.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
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
    private ArrayList<String> playerNames; // Lista de nombres de jugadores para el autocompletado
    private ArrayList<String> addedPlayers; // Lista de jugadores seleccionados
    private int currentUserId; // Almacenará el ID del usuario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Obtener el currentUserId desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1); // Obtener el ID del usuario actual

        // Inicializar la base de datos y las vistas
        dbHelper = new DatabaseHelper(this);
        playerNameInput = findViewById(R.id.player_name_input);
        playerChipGroup = findViewById(R.id.player_chip_group);
        autoCompleteChipGroup = findViewById(R.id.auto_complete_chip_group);
        addPlayerButton = findViewById(R.id.add_player_button);

        playerNames = new ArrayList<>();
        addedPlayers = new ArrayList<>();

        // Limpiar los chips finales al cargar la actividad
        playerChipGroup.removeAllViews();

        // Cargar los nombres de jugadores desde la base de datos
        loadPlayerNames();

        // Configurar el adaptador para el autocompletado
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, playerNames);
        playerNameInput.setAdapter(adapter);

        // Deshabilitar el desplegable de autocompletado
        playerNameInput.setDropDownHeight(0); // Esto evita que el desplegable se muestre
        playerNameInput.setDropDownWidth(0);  // Esto también asegura que no se muestre

        // Mostrar los chips de autocompletado según lo que se escribe
        playerNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                // Limpiar los chips anteriores
                autoCompleteChipGroup.removeAllViews();

                // Mostrar chips de sugerencias de autocompletado si hay texto
                if (charSequence.length() > 0) {
                    for (String playerName : playerNames) {
                        if (playerName.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                            addAutoCompleteChip(playerName);  // Añadir chips
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Mostrar chips cuando el usuario selecciona una sugerencia
        playerNameInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPlayer = (String) parent.getItemAtPosition(position);
            addPlayerToFinalChips(selectedPlayer);
        });

        // Botón para añadir el jugador a los chips finales
        addPlayerButton.setOnClickListener(v -> {
            String newPlayer = playerNameInput.getText().toString().trim();

            // Formatear la primera letra a mayúscula
            newPlayer = newPlayer.substring(0, 1).toUpperCase() + newPlayer.substring(1).toLowerCase();

            if (!newPlayer.isEmpty()) {
                if (addedPlayers.contains(newPlayer)) {
                    Toast.makeText(PlayerActivity.this, "Este jugador ya está en la lista.", Toast.LENGTH_SHORT).show();
                } else if (addedPlayers.size() >= 4) {
                    Toast.makeText(PlayerActivity.this, "No puedes agregar más de 4 jugadores.", Toast.LENGTH_SHORT).show();
                } else {
                    // Verificar si el jugador ya existe en la base de datos
                    if (!playerNames.contains(newPlayer)) {
                        // El jugador no existe en la base de datos, agregarlo
                        boolean isInserted = dbHelper.addPlayer(currentUserId, newPlayer);
                        if (isInserted) {
                            Toast.makeText(PlayerActivity.this, "Jugador añadido a la base de datos.", Toast.LENGTH_SHORT).show();
                            playerNames.add(newPlayer); // Agregar a la lista de autocompletado también
                        } else {
                            Toast.makeText(PlayerActivity.this, "Error al agregar jugador a la base de datos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    // Añadir el jugador a los chips finales
                    addPlayerToFinalChips(newPlayer);
                    playerNameInput.setText("");
                }
            }
        });
    }

    // Cargar los jugadores de la base de datos
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

    // Añadir jugador a los chips de autocompletado
    private void addAutoCompleteChip(String playerName) {
        Chip chip = new Chip(this);
        chip.setText(playerName);
        chip.setCloseIconVisible(false); // No hay necesidad de eliminar chips de autocompletado
        chip.setOnClickListener(v -> playerNameInput.setText(playerName)); // Autocompletar el texto

        autoCompleteChipGroup.addView(chip);
    }

    // Añadir jugador a los chips finales
    private void addPlayerToFinalChips(String playerName) {
        // Añadir el jugador a la lista de jugadores finales si no está ya añadido y si no se han añadido 4 jugadores
        if (!addedPlayers.contains(playerName) && addedPlayers.size() < 4) {
            addedPlayers.add(playerName);

            // Crear un chip para el jugador
            Chip chip = new Chip(this);
            chip.setText(playerName);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> removePlayerFromFinalChips(playerName));

            // Añadir el chip al grupo de chips finales
            playerChipGroup.addView(chip);
        }
    }

    // Eliminar jugador de los chips finales
    private void removePlayerFromFinalChips(String playerName) {
        addedPlayers.remove(playerName);
        playerChipGroup.removeViewAt(addedPlayers.indexOf(playerName));
    }
}