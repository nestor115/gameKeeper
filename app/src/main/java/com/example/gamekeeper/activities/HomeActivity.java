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
import com.example.gamekeeper.fragments.SearchGenreFragment;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;
import com.example.gamekeeper.utils.IntentExtras;

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
        setContentView(R.layout.activity_home);
        dB = new DatabaseHelper(this);
        currentUserId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);
        //Si es la primera vez que se abre la app, se cargan los juegos
        if (isFirstLaunch()) {
            insertData();
            setFirstLaunchFlag();
        }
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new HomeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //Maneja el click de cada elemento de la lista en el boton de detalle
        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement listElement) {


                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                intent.putExtra(IntentExtras.BOARDGAME_ID, listElement.getId());

                intent.putExtra(IntentExtras.NAME_VIEW, "HOME");
                startActivity(intent);
            }
        });
        //Maneja el click de cada elemento de la lista en el boton de borrar
        adapter.setOnDeleteClickListener(listElement -> {
            int position = fullList.indexOf(listElement);
            boolean isDeleted = dB.removeUserBoardgame(currentUserId, listElement.getId());

            if (isDeleted) {
                Toast.makeText(this, "Juego borrado exitosamente", Toast.LENGTH_SHORT).show();
                fullList.remove(position);
                adapter.submitList(new ArrayList<>(fullList));
            } else {
                Toast.makeText(this, "Error al borrar el juego", Toast.LENGTH_SHORT).show();
            }
        });
        //Le pasa los juegos al adapter
        loadData();
        loadSearchFragment();
    }

    //Carga el fragment de busqueda de juegos
    private void loadSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SearchGenreFragment());
        transaction.commit();
    }

    //Le pasa los juegos al adapter
    private void loadData() {
        List<ListElement> elements = dB.getUserBoardgames(currentUserId);

        if (elements != null && !elements.isEmpty()) {
            fullList = new ArrayList<>(elements);
            adapter.submitList(fullList);
        }
    }

    public List<String> getGenres() {
        return dB.getGenres();
    }

    //Filtra los juegos por nombre y género
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

    //Verifica si es la primera vez que se abre la app
    private boolean isFirstLaunch() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getBoolean("first_launch", true);
    }

    //Marca que no es la primera vez que se abre la app
    private void setFirstLaunchFlag() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_launch", false);
        editor.apply();
    }

    //Carga los juegos en la base de datos, la primera vez que se abre la app
    private void insertData() {
        dB.addGenre("Estrategia");
        dB.addGenre("Rol");
        dB.addGenre("Cooperativo");
        dB.addGenre("Cartas");
        dB.addGenre("Construcción de mazos");
        dB.addGenre("Construcción");
        dB.addGenre("Party");
        dB.addGenre("Selección de cartas");
        dB.addGenre("Familiar");
        dB.addGenre("Deducción");
        dB.addGenre("Palabras");
        dB.addGenre("Trivia");
        dB.addGenre("Colocación de trabajadores");
        dB.addGenre("Juegos de guerra");
        dB.addGenre("Miniaturas");
        dB.addGenre("Tableros abstractos");
        dB.addGenre("Economía");
        dB.addGenre("Aventura");
        dB.addGenre("Gestión de recursos");
        dB.addGenre("Colección de sets");
        dB.addGenre("Negociación");
        dB.addGenre("Memoria");
        dB.addGenre("Habilidad/destreza");
        dB.addGenre("Partida rápida");
        dB.addGenre("Rol");
        dB.addGenre("Tiradas de dados");
        dB.addGenre("Colocación de losetas");
        dB.addGenre("Temáticos");

        long gameId;
        String explodingKittensPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738305/ExplodingKittens_cn9atc.webp";
        String sushiGoPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738307/SushiGo_diof8y.webp";
        String theMindPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738307/TheMind_s56v7u.webp";
        String catanPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738308/Catan_huslox.webp";
        String cubirdsPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738304/Cubirds_timhhw.webp";
        String unstableUnicornsPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738307/UnstableUnicorns_gstjke.webp";
        String virusPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738307/Virus_i6jr1j.webp";
        String hereToSlayPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738305/HereToSlay_jokect.webp";
        String paradicePhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738306/Paradice_y1ljq4.webp";
        String diosesPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738305/Dioses_xps1gm.webp";
        String azulPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738307/Azul_mq3ctb.webp";
        String barBestialPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738308/BarBestial_ihxfl7.webp";
        String desplumadosPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738305/Desplumados_gc51k6.webp";
        String arreUnicornioPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738307/ArreUnicornio_e9bhha.webp";
        String cabrasMontesasPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738308/CabrasMontesas_gdzcri.webp";
        String bangPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738308/Bang_jt2mrc.webp";
        String cercaYLejosPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738308/CercaYLejos_jzgkue.webp";
        String coatlPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738308/Coatl_jmdbnq.webp";
        String polillaTramposaPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738306/PolillaTramposa_q4dlic.webp";
        String pelusasPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738306/Pelusas_jxv7hj.webp";
        String munchkinPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738306/Munchkin_u6akrt.webp";
        String cafePhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738308/Cafe_wtys2t.webp";
        String holiPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738305/Holi_k5usuz.webp";
        String ladrillazoPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738306/Ladrillazo_jg6zxb.webp";
        String lavinaPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738305/LaVina_yjr5ut.webp";
        String notAlonePhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738305/NotAlone_ovvgpv.webp";
        String picnicPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738306/Picnic_thfogw.webp";
        String rebelPrincessPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738306/RebelPrincess_qx4afn.webp";
        String sagradaPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738307/Sagrada_kokrkg.webp";
        String samuraiSwordPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738307/SamuraiSword_bb5gvs.webp";
        String samoaPhoto = "https://res.cloudinary.com/dxgk71sz7/image/upload/v1732738306/Samoa_zzelmr.webp";


        gameId = dB.addBoardgame("Exploding Kittens", explodingKittensPhoto, "Un juego de cartas de estrategia y humor en el que los jugadores deben evitar explotar.", 2015, "2-5", "15 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));


        gameId = dB.addBoardgame("Sushi Go!", sushiGoPhoto, "Un juego de cartas de draft rápido en el que los jugadores intentan crear la mejor combinación de sushi.", 2013, "2-5", "15 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Selección de cartas"));
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


        gameId = dB.addBoardgame("Paradice", paradicePhoto, "Un juego de feria donde los jugadores deben completar casetas con dados de colores para ganar tickets y sorpresas. ¡Una experiencia llena de diversión y estrategia!", 2020, "2-6", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Tiradas de dados"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Familiar"));


        gameId = dB.addBoardgame("Dioses!", diosesPhoto, "Un juego de cartas en el que los jugadores combinan dioses y mitologías de diferentes culturas para crear poderosos grupos y ganar la partida.", 2021, "2-4", "45-75 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));


        gameId = dB.addBoardgame("Azul", azulPhoto, "Un juego de estrategia abstracto en el que los jugadores deben completar patrones con losetas de colores.", 2017, "2-4", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Colocación de losetas"));


        gameId = dB.addBoardgame("Bar Bestial", barBestialPhoto, "Un juego de estrategia rápida en el que los animales intentan entrar en el mejor bar de la ciudad.", 2011, "2-4", "20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Familiar"));


        gameId = dB.addBoardgame("Desplumados", desplumadosPhoto, "Un juego de cartas en el que los jugadores buscan tener el mejor gallinero del vecindario.", 2016, "2-5", "15-30 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));


        gameId = dB.addBoardgame("¡Arre Unicornio!", arreUnicornioPhoto, "Un juego de cartas caótico en el que los jugadores asumen el rol de pacientes o doctores en un manicomio, tratando de escapar o impedir la huida, respectivamente. ¡Engaños, estrategias y mucho caos garantizado!", 2020, "3-6", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Party"));


        gameId = dB.addBoardgame("Cabras Montesas", cabrasMontesasPhoto, "Un juego de estrategia en el que los jugadores compiten para llevar a sus cabras a la cima de la montaña.", 2021, "2-4", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Habilidad/destreza"));


        gameId = dB.addBoardgame("Bang!", bangPhoto, "Un juego del Salvaje Oeste donde forajidos, alguaciles y renegados luchan entre sí.", 2002, "4-7", "20-40 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Temáticos"));


        gameId = dB.addBoardgame("Cerca y Lejos", cercaYLejosPhoto, "Un juego de aventuras y exploración donde los jugadores buscan riquezas en tierras lejanas.", 2017, "2-4", "90-120 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Aventura"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Gestión de recursos"));


        gameId = dB.addBoardgame("Coatl", coatlPhoto, "Un juego de estrategia en el que los jugadores construyen serpientes emplumadas para ganar puntos.", 2020, "2-4", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Construcción"));


        gameId = dB.addBoardgame("Polilla Tramposa", polillaTramposaPhoto, "Un juego de cartas donde está permitido hacer trampas... ¡si no te pillan!", 2011, "3-5", "15-20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Party"));


        gameId = dB.addBoardgame("Pelusas", pelusasPhoto, "Un juego un juego de cartas rápido donde los jugadores deben acumular pelusas y decidir cuándo arriesgarse a seguir jugando para no perder lo ganado. Ideal para partidas ágiles y dinámicas.", 2018, "2-5", "15-20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Party"));


        gameId = dB.addBoardgame("Munchkin", munchkinPhoto, "Un juego de cartas lleno de humor en el que los jugadores suben de nivel y traicionan a sus amigos.", 2001, "3-6", "60-120 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Rol"));


        gameId = dB.addBoardgame("Café", cafePhoto, "Un juego estratégico donde los jugadores gestionan plantaciones de café y cadenas de suministro.", 2020, "1-4", "20-40 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Gestión de recursos"));


        gameId = dB.addBoardgame("Holi: Festival of Colors", holiPhoto, "Un juego abstracto y estratégico donde los jugadores lanzan colores para cubrir la mayor área posible.", 2021, "2-4", "20-40 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Tablero abstractos"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));


        gameId = dB.addBoardgame("Ladrillazo", ladrillazoPhoto, "Un juego de cartas satírico sobre el mundo de la especulación inmobiliaria.", 2017, "3-5", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));


        gameId = dB.addBoardgame("La Viña", lavinaPhoto, "Un juego de gestión y colección de uvas para crear los mejores vinos.", 2019, "2-5", "45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Gestión de recursos"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Colección de sets"));


        gameId = dB.addBoardgame("Not Alone", notAlonePhoto, "Un juego de supervivencia donde un jugador es una criatura que persigue a los demás.", 2016, "2-7", "30-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Aventura"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Temáticos"));


        gameId = dB.addBoardgame("Picnic", picnicPhoto, "Un juego rápido y competitivo donde los jugadores recogen alimentos para hacer el mejor picnic.", 2019, "2-6", "15-20 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Partida rápida"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));


        gameId = dB.addBoardgame("Rebel Princess", rebelPrincessPhoto, "Un juego de cartas en el que los jugadores deben evitar propuestas matrimoniales de príncipes y ranas encantadas, buscando la libertad. Con un toque de humor y estrategia, el objetivo es tomar decisiones inteligentes para eludir los compromisos y salir victoriosos.", 2021, "2-4", "45-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Party"));


        gameId = dB.addBoardgame("Sagrada", sagradaPhoto, "Un juego de colocación de dados donde los jugadores crean coloridos vitrales.", 2017, "1-4", "30-45 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Tiradas de dados"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));


        gameId = dB.addBoardgame("Samurai Sword", samuraiSwordPhoto, "Un juego de cartas al estilo Bang! ambientado en el Japón feudal.", 2012, "3-7", "20-40 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Temáticos"));


        gameId = dB.addBoardgame("Samoa", samoaPhoto, "Un juego de cartas en el que los jugadores decoran máscaras mientras intentan adivinar las de los demás y evitar cartas de mala suerte. ¡Un desafío de deducción y estrategia con un toque de faroleo!", 2009, "3-6", "45-60 min");
        dB.addBoardgameGenre(gameId, dB.getGenreId("Estrategia"));
        dB.addBoardgameGenre(gameId, dB.getGenreId("Cartas"));

    }

}