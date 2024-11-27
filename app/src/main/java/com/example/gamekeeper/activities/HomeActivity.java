package com.example.gamekeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.HomeAdapter;
import com.example.gamekeeper.fragments.PlayerFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private DatabaseHelper dB;
    private int currentUserId;
    private List<ListElement> fullList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_boardgame);

        // Inicializar DatabaseHelper
        dB = new DatabaseHelper(this);


        currentUserId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);


        if (isFirstLaunch()) {
            insertData();
            setFirstLaunchFlag();
        }

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new HomeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Para lista vertical
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.BOARDGAME_ID, fullList.get(position.getId()).getId());
            intent.putExtra(DetailActivity.NAME_VIEW, "HOME"); // Nombre de la vista como "HOME"
            startActivity(intent);
        });
        adapter.setOnDeleteClickListener(listElement -> {
            boolean isDeleted = dB.removeUserBoardgame(currentUserId, listElement.getId());
            if (isDeleted) {
                Toast.makeText(this, "Juego borrado exitosamente", Toast.LENGTH_SHORT).show();
                loadData();
            } else {
                Toast.makeText(this, "Error al borrar el juego", Toast.LENGTH_SHORT).show();
            }
        });
        loadData();

        loadSearchFragment();
    }

    private void loadSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PlayerFragment());
        transaction.commit();
    }

    private void loadData() {
        List<ListElement> elements = dB.getUserBoardgames(currentUserId);

        if (elements != null && !elements.isEmpty()) {
            fullList = elements; // Guardamos la lista completa
            adapter.submitList(fullList);
        } else {
            Toast.makeText(this, "No se encontraron juegos.", Toast.LENGTH_SHORT).show();
        }
    }

    public List<String> getGenres() {
        return dB.getGenres();
    }

    public void filterList(String query, String selectedGenre) {
        List<ListElement> filteredList = new ArrayList<>();
        for (ListElement element : fullList) {
            boolean matchesQuery = element.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesGenre = selectedGenre.equals("Todos") || dB.isBoardgameInGenre(element.getId(), selectedGenre);

            if (matchesQuery && matchesGenre) {
                filteredList.add(element);
            }
        }
        adapter.submitList(filteredList);
    }

    private boolean isFirstLaunch() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getBoolean("first_launch", true);
    }

    private void setFirstLaunchFlag() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_launch", false);
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
        byte[] barBestialPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("BarBestial.webp"));
        byte[] desplumadosPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Desplumados.webp"));
        byte[] arreUnicornioPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("ArreUnicornio.webp"));
        byte[] cabrasMontesasPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("CabrasMontesas.webp"));
        byte[] bangPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Bang.webp"));
        byte[] cercaYLejosPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("CercaYLejos.webp"));
        byte[] coatlPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Coatl.webp"));
        byte[] polillaTramposaPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("PolillaTramposa.webp"));
        byte[] pelusasPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Pelusas.webp"));
        byte[] munchkinPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Munchkin.webp"));
        byte[] cafePhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Cafe.webp"));
        byte[] holiPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Holi.webp"));
        byte[] ladrillazoPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Ladrillazo.webp"));
        byte[] lavinaPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("LaVina.webp"));
        byte[] notAlonePhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("NotAlone.webp"));
        byte[] picnicPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Picnic.webp"));
        byte[] rebelPrincessPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("RebelPrincess.webp"));
        byte[] sagradaPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Sagrada.webp"));
        byte[] samuraiSwordPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("SamuraiSword.webp"));
        byte[] samoaPhoto = dB.getBytesFromBitmap(dB.getBitmapFromAssets("Samoa.webp"));



        gameId = dB.addBoardgame("Exploding Kittens", explodingKittensPhoto, "Un juego de cartas de estrategia y humor en el que los jugadores deben evitar explotar.", 2015, "2-5", "15 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        explodingKittensPhoto = null;

        gameId = dB.addBoardgame("Sushi Go!", sushiGoPhoto, "Un juego de cartas de draft rápido en el que los jugadores intentan crear la mejor combinación de sushi.", 2013, "2-5", "15 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Draft"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        sushiGoPhoto = null;

        gameId = dB.addBoardgame("The Mind", theMindPhoto, "Un juego cooperativo donde los jugadores intentan sincronizar sus cartas en orden ascendente sin comunicación verbal.", 2018, "2-4", "20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cooperativo"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        theMindPhoto = null;

        gameId = dB.addBoardgame("Catan", catanPhoto, "Un juego de estrategia donde los jugadores intentan construir y gestionar colonias en la isla de Catan.", 1995, "3-4", "60-120 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Construcción"));
        catanPhoto = null;

        gameId = dB.addBoardgame("Cubirds", cubirdsPhoto, "Un juego de cartas en el que los jugadores intentan recolectar bandadas de aves colocando cartas estratégicamente.", 2018, "2-5", "20-30 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        cubirdsPhoto = null;

        gameId = dB.addBoardgame("Unstable Unicorns", unstableUnicornsPhoto, "Un juego de cartas en el que los jugadores construyen un ejército de unicornios y usan su magia para sabotear a los demás.", 2017, "2-8", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        unstableUnicornsPhoto = null;

        gameId = dB.addBoardgame("Virus", virusPhoto, "Un juego de cartas en el que los jugadores intentan infectar a otros mientras protegen sus órganos sanos.", 2015, "2-6", "20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        virusPhoto = null;

        gameId = dB.addBoardgame("Here to Slay", hereToSlayPhoto, "Un juego de cartas estratégico y competitivo en el que los jugadores forman un equipo de héroes y luchan contra monstruos.", 2020, "2-6", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        hereToSlayPhoto = null;

        gameId = dB.addBoardgame("Paradice", paradicePhoto, "Un juego de cartas de estrategia y azar en el que los jugadores deben crear su propio paraíso mientras compiten por la supremacía.", 2020, "2-6", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        paradicePhoto = null;

        gameId = dB.addBoardgame("Dioses!", diosesPhoto, "Un juego de estrategia y cartas donde los jugadores asumen el papel de dioses en un mundo mitológico, buscando dominar a los demás a través de poderes divinos.", 2021, "2-4", "45-75 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        diosesPhoto = null;

        gameId = dB.addBoardgame("Azul", azulPhoto, "Un juego de estrategia abstracto en el que los jugadores deben completar patrones con losetas de colores.", 2017, "2-4", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Colocación de Losetas"));
        azulPhoto = null;

        gameId = dB.addBoardgame("Bar Bestial", barBestialPhoto, "Un juego de estrategia rápida en el que los animales intentan entrar en el mejor bar de la ciudad.", 2011, "2-4", "20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Familiar"));
        barBestialPhoto = null;

        gameId = dB.addBoardgame("Desplumados", desplumadosPhoto, "Un juego de cartas en el que los jugadores buscan tener el mejor gallinero del vecindario.", 2016, "2-5", "15-30 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        desplumadosPhoto = null;

        gameId = dB.addBoardgame("¡Arre Unicornio!", arreUnicornioPhoto, "Un juego caótico y divertido en el que los jugadores deben cruzar la línea de meta con sus unicornios.", 2020, "3-6", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Party"));
        arreUnicornioPhoto = null;

        gameId = dB.addBoardgame("Cabras Montesas", cabrasMontesasPhoto, "Un juego de estrategia en el que los jugadores compiten para llevar a sus cabras a la cima de la montaña.", 2021, "2-4", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Habilidad/destreza"));
        cabrasMontesasPhoto = null;

        gameId = dB.addBoardgame("Bang!", bangPhoto, "Un juego del Salvaje Oeste donde forajidos, alguaciles y renegados luchan entre sí.", 2002, "4-7", "20-40 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Temáticos"));
        bangPhoto = null;

        gameId = dB.addBoardgame("Cerca y Lejos", cercaYLejosPhoto, "Un juego de aventuras y exploración donde los jugadores buscan riquezas en tierras lejanas.", 2017, "2-4", "90-120 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Aventura"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Gestión de recursos"));
        cercaYLejosPhoto = null;

        gameId = dB.addBoardgame("Coatl", coatlPhoto, "Un juego de estrategia en el que los jugadores construyen serpientes emplumadas para ganar puntos.", 2020, "2-4", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Construcción"));
        coatlPhoto = null;

        gameId = dB.addBoardgame("Polilla Tramposa", polillaTramposaPhoto, "Un juego de cartas donde está permitido hacer trampas... ¡si no te pillan!", 2011, "3-5", "15-20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Party"));
        polillaTramposaPhoto = null;

        gameId = dB.addBoardgame("Pelusas", pelusasPhoto, "Un juego de cartas ligero y rápido donde los jugadores se deshacen de cartas mientras mienten sobre sus jugadas.", 2018, "2-5", "15-20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Party"));
        pelusasPhoto = null;

        gameId = dB.addBoardgame("Munchkin", munchkinPhoto, "Un juego de cartas lleno de humor en el que los jugadores suben de nivel y traicionan a sus amigos.", 2001, "3-6", "60-120 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Role-playing (RPG)"));
        munchkinPhoto = null;

        gameId = dB.addBoardgame("Café", cafePhoto, "Un juego estratégico donde los jugadores gestionan plantaciones de café y cadenas de suministro.", 2020, "1-4", "20-40 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Gestión de recursos"));
        cafePhoto = null;

        gameId = dB.addBoardgame("Holi: Festival of Colors", holiPhoto, "Un juego abstracto y estratégico donde los jugadores lanzan colores para cubrir la mayor área posible.", 2021, "2-4", "20-40 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Tablero abstractos"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        holiPhoto = null;

        gameId = dB.addBoardgame("Ladrillazo", ladrillazoPhoto, "Un juego de cartas satírico sobre el mundo de la especulación inmobiliaria.", 2017, "3-5", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        ladrillazoPhoto = null;

        gameId = dB.addBoardgame("La Viña", lavinaPhoto, "Un juego de gestión y colección de uvas para crear los mejores vinos.", 2019, "2-5", "45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Gestión de recursos"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Set collection"));
        lavinaPhoto = null;

        gameId = dB.addBoardgame("Not Alone", notAlonePhoto, "Un juego de supervivencia asimétrico donde un jugador es una criatura que persigue a los demás.", 2016, "2-7", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Aventura"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Temáticos"));
        notAlonePhoto = null;

        gameId = dB.addBoardgame("Picnic", picnicPhoto, "Un juego rápido y competitivo donde los jugadores recogen alimentos para hacer el mejor picnic.", 2019, "2-6", "15-20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Partida Rápida"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        picnicPhoto = null;

        gameId = dB.addBoardgame("Rebel Princess", rebelPrincessPhoto, "Un juego cooperativo en el que los jugadores ayudan a una princesa rebelde a recuperar su reino.", 2021, "2-4", "45-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cooperativo"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Aventura"));
        rebelPrincessPhoto = null;

        gameId = dB.addBoardgame("Sagrada", sagradaPhoto, "Un juego de colocación de dados donde los jugadores crean coloridos vitrales.", 2017, "1-4", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Tiradas de Dados"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        sagradaPhoto = null;

        gameId = dB.addBoardgame("Samurai Sword", samuraiSwordPhoto, "Un juego de cartas al estilo Bang! ambientado en el Japón feudal.", 2012, "3-7", "20-40 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Temáticos"));
        samuraiSwordPhoto = null;

        gameId = dB.addBoardgame("Samoa", samoaPhoto, "Un juego de estrategia y bluff donde los jugadores intentan reunir recursos para su tribu.", 2009, "3-6", "45-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Negociación"));
        samoaPhoto = null;
        System.gc();
    }

}