package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.Types.ListType;
import com.example.gamekeeper.adapters.ListAdapter;
import com.example.gamekeeper.adapters.ListAdapterHome;
import com.example.gamekeeper.fragments.SearchBarFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity{

    private DatabaseHelper dB;
    private RecyclerView recyclerView;
    private ListAdapterHome listAdapter;
    private List<ListElement> listElements;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dB = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("user_id", -1);
        if (currentUserId != -1) {
            listElements = dB.getUserBoardgames(currentUserId);
            displayBoardgames(listElements);
        } else {
            Toast.makeText(this, "No se encontró el ID del usuario.", Toast.LENGTH_SHORT).show();
        }

        // Verificar si es el primer inicio
        if (isFirstLaunch()) {
            insertData(); // Solo se ejecuta si es el primer inicio
            setFirstLaunchFlag(); // Marca que la app ya se ha lanzado
        }
      /*  // Cargar el fragmento de búsqueda
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, SearchBarFragment.newInstance(ListType.HOME));
        transaction.commit();*/
    }
    private List<ListElement> getUserBoardgames(int userId) {
        List<ListElement> listElements = new ArrayList<>();
        Cursor cursor = dB.getAllBoardgamesForUser(userId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NAME);
                    int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_ID);
                    int photoIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_PHOTO);
                    String name = cursor.getString(nameIndex);
                    int id = cursor.getInt(idIndex);
                    byte[] photo = cursor.getBlob(photoIndex);
                    listElements.add(new ListElement(name, id, photo));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return listElements;
    }
    private void displayBoardgames(List<ListElement> listElements) {
        recyclerView = findViewById(R.id.recyclerViewBoardgames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listAdapter = new ListAdapterHome(listElements, this,currentUserId);
        recyclerView.setAdapter(listAdapter);

        // Configurar un listener para los clics en los ítems del RecyclerView
        listAdapter.setOnItemClickListener(position -> {
            ListElement listElement = listElements.get(position);
            Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.BOARDGAME_ID, listElement.getId());
            intent.putExtra(DetailActivity.NAME_VIEW, "HOME");
            startActivity(intent);
        });
    }

    // Método para verificar si es la primera vez que se lanza la app
    private boolean isFirstLaunch() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getBoolean("first_launch", true);
    }

    // Método para establecer que la app ya fue lanzada
    private void setFirstLaunchFlag() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_launch", false); // Marca que no es el primer inicio
        editor.apply();
    }
   /* @Override
    public void onSearchResults(List<ListElement> searchResults) {
        // Aquí se verifica el tipo de vista, en función de eso, filtramos los resultados.



        listElements.clear();
        listElements.addAll(searchResults); // Aquí es donde se hace la búsqueda en la tabla boardgames

        listAdapter.notifyDataSetChanged();
    }*/




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