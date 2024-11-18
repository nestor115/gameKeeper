package com.example.gamekeeper.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;
import com.example.gamekeeper.adapters.ListAdapterPlayers;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardgameActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ListAdapterPlayers adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_boardgame);

        // Obtener el RecyclerView del layout
        recyclerView = findViewById(R.id.recyclerView);

        // Crear el adaptador
        adapter = new ListAdapterPlayers();

        // Asignar un listener para los clics de los elementos (opcional)
        adapter.setOnItemClickListener(listElement -> {
            // Aquí puedes manejar el clic en un elemento
            Toast.makeText(PlayerBoardgameActivity.this, "Clic en: " + listElement.getName(), Toast.LENGTH_SHORT).show();
        });

        // Configurar el RecyclerView con el adaptador
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Para que se muestre como lista vertical
        recyclerView.setAdapter(adapter);

        // Llamar al método para enviar la lista de elementos al adaptador
        loadData();  // Este es el método donde obtendrás la lista de ListElement
    }

    // Método para cargar los datos de ejemplo (puedes reemplazar esto por datos reales)
    private void loadData() {
        List<ListElement> elements = new ArrayList<>();

        // Crear algunos elementos de ejemplo
        elements.add(new ListElement("Monopoly", 1, new byte[0]));  // En este caso, la imagen es un array vacío, pero puedes poner una imagen real
        elements.add(new ListElement("Risk", 2, new byte[0]));
        elements.add(new ListElement("Catan", 3, new byte[0]));

        // Enviar la lista al adaptador
        adapter.submitList(elements);
    }
}
