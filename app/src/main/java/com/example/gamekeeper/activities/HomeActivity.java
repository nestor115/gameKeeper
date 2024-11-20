package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.ListAdapterHome;
import com.example.gamekeeper.fragments.PlayerFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ListAdapterHome adapter;
    private DatabaseHelper dB;
    private int currentUserId; // Almacenar el ID del usuario actual
    private List<ListElement> fullList = new ArrayList<>(); // Lista completa de elementos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_boardgame);

        // Inicializar DatabaseHelper
        dB = new DatabaseHelper(this);

        // Obtener el ID del usuario desde SharedPreferences
        currentUserId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);

        // Verificar si es el primer inicio
        if (isFirstLaunch()) {
            insertData(); // Solo se ejecuta si es el primer inicio
            setFirstLaunchFlag(); // Marca que la app ya se ha lanzado
        }

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ListAdapterHome();
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Para lista vertical
        recyclerView.setAdapter(adapter);

        // Cargar los datos
        loadData();

        // Cargar el fragmento con el SearchView y Spinner
        loadSearchFragment();
    }

    private void loadSearchFragment() {
        // Cargar el fragmento de búsqueda
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PlayerFragment());
        transaction.commit();
    }

    private void loadData() {
        // Llamamos al método de la base de datos para obtener los juegos del usuario
        List<ListElement> elements = dB.getUserBoardgames(currentUserId);

        if (elements != null && !elements.isEmpty()) {
            fullList = elements; // Guardamos la lista completa
            adapter.submitList(fullList);
        } else {
            Toast.makeText(this, "No se encontraron juegos.", Toast.LENGTH_SHORT).show();
        }
    }

    public List<String> getGenres() {
        // Obtener los géneros desde la base de datos
        return dB.getGenres();
    }

    // Método para filtrar la lista
    public void filterList(String query, String selectedGenre) {
        List<ListElement> filteredList = new ArrayList<>();
        for (ListElement element : fullList) {
            boolean matchesQuery = element.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesGenre = selectedGenre.equals("Todos") || dB.isBoardgameInGenre(element.getId(), selectedGenre);

            // Añadir a la lista filtrada si cumple con ambos criterios
            if (matchesQuery && matchesGenre) {
                filteredList.add(element);
            }
        }
        // Actualizar el RecyclerView con los elementos filtrados
        adapter.submitList(filteredList);
    }

    // Métodos para verificar el primer inicio y configurarlo
    private boolean isFirstLaunch() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getBoolean("first_launch", true);
    }

    private void setFirstLaunchFlag() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_launch", false); // Marca que no es el primer inicio
        editor.apply();
    }




    private void insertData() {
        dB.addGenre("Estrategia");
        dB.addGenre("Rol");
        dB.addGenre("Cooperativo");
        dB.addGenre("Cartas");
        dB.addGenre("Construcción de mazos");
        dB.addGenre("Construcción");
        dB.addGenre("Party");
        dB.addGenre("Draft");
        dB.addGenre("Familiar");
        dB.addGenre("Deducción");
        dB.addGenre("Palabras");
        dB.addGenre("Trivia");
        dB.addGenre("Colocación de trabajadores");
        dB.addGenre("Wargames");
        dB.addGenre("Miniaturas");
        dB.addGenre("Tablero abstractos");
        dB.addGenre("Economía");
        dB.addGenre("Aventura");
        dB.addGenre("Gestión de recursos");
        dB.addGenre("Set collection");
        dB.addGenre("Negociación");
        dB.addGenre("Memoria");
        dB.addGenre("Habilidad/destreza");
        dB.addGenre("Partida Rápida");
        dB.addGenre("Role-playing (RPG)");
        dB.addGenre("Tiradas de Dados");
        dB.addGenre("Colocación de Losetas");
        dB.addGenre("Temáticos");

        long gameId;
        byte[] explodingKittensPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("ExplodingKittens.webp"));
        byte[] sushiGoPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("SushiGo.webp"));
        byte[] theMindPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("TheMind.webp"));
        byte[] catanPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Catan.webp"));
        byte[] cubirdsPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Cubirds.webp"));
        byte[] unstableUnicornsPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("UnstableUnicorns.webp"));
        byte[] virusPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Virus.webp"));
        byte[] hereToSlayPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("HereToSlay.webp"));
        byte[] paradicePhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Paradice.webp"));
        byte[] diosesPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Dioses.webp"));
        byte[] azulPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Azul.webp"));

        gameId = dB.addBoardgame("Exploding Kittens", explodingKittensPhoto, "Un juego de cartas de estrategia y humor en el que los jugadores deben evitar explotar.", 2015, "2-5", "15 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("Sushi Go!", sushiGoPhoto, "Un juego de cartas de draft rápido en el que los jugadores intentan crear la mejor combinación de sushi.", 2013, "2-5", "15 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Draft"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("The Mind", theMindPhoto, "Un juego cooperativo donde los jugadores intentan sincronizar sus cartas en orden ascendente sin comunicación verbal.", 2018, "2-4", "20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cooperativo"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("Catan", catanPhoto, "Un juego de estrategia donde los jugadores intentan construir y gestionar colonias en la isla de Catan.", 1995, "3-4", "60-120 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Construcción"));
        gameId = dB.addBoardgame("Cubirds", cubirdsPhoto, "Un juego de cartas en el que los jugadores intentan recolectar bandadas de aves colocando cartas estratégicamente.", 2018, "2-5", "20-30 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        gameId = dB.addBoardgame("Unstable Unicorns", unstableUnicornsPhoto, "Un juego de cartas en el que los jugadores construyen un ejército de unicornios y usan su magia para sabotear a los demás.", 2017, "2-8", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        gameId = dB.addBoardgame("Virus", virusPhoto, "Un juego de cartas en el que los jugadores intentan infectar a otros mientras protegen sus órganos sanos.", 2015, "2-6", "20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        gameId = dB.addBoardgame("Here to Slay", hereToSlayPhoto, "Un juego de cartas estratégico y competitivo en el que los jugadores forman un equipo de héroes y luchan contra monstruos.", 2020, "2-6", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("Paradice", paradicePhoto, "Un juego de cartas de estrategia y azar en el que los jugadores deben crear su propio paraíso mientras compiten por la supremacía.", 2020, "2-6", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("Dioses!", diosesPhoto, "Un juego de estrategia y cartas donde los jugadores asumen el papel de dioses en un mundo mitológico, buscando dominar a los demás a través de poderes divinos.", 2021, "2-4", "45-75 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        gameId = dB.addBoardgame("Azul", azulPhoto, "Un juego de estrategia abstracto en el que los jugadores deben completar patrones con losetas de colores.", 2017, "2-4", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Colocación de Losetas"));
    }

}