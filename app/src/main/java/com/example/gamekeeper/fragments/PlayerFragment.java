package com.example.gamekeeper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.gamekeeper.R;
import com.example.gamekeeper.activities.PlayerBoardgameActivity;

import java.util.List;

public class PlayerFragment extends Fragment {
    private SearchView searchView;
    private Spinner genreSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        searchView = view.findViewById(R.id.searchView);
        genreSpinner = view.findViewById(R.id.genreSpinner);

        // Configurar el Spinner
        setupSpinner();

        // Configurar el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No hacemos nada al enviar la búsqueda
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Llamar al método en la actividad para filtrar
                if (getActivity() instanceof PlayerBoardgameActivity) {
                    PlayerBoardgameActivity activity = (PlayerBoardgameActivity) getActivity();
                    String selectedGenre = (String) genreSpinner.getSelectedItem(); // Obtener el género seleccionado
                    activity.filterList(newText, selectedGenre); // Pasar búsqueda y género
                }
                return true;
            }
        });

        // Hacer que el SearchView sea clickeable en todo el área
        searchView.setOnClickListener(v -> {
            // Esto asegura que al hacer clic en cualquier parte del SearchView se abre el campo de búsqueda
            searchView.setIconified(false);
        });

        return view;
    }

    private void setupSpinner() {
        if (getActivity() instanceof PlayerBoardgameActivity) {
            PlayerBoardgameActivity activity = (PlayerBoardgameActivity) getActivity();
            List<String> genres = activity.getGenres(); // Obtener géneros desde la actividad
            genres.add(0, "Todos"); // Opción por defecto

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genres);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genreSpinner.setAdapter(spinnerAdapter);

            // Listener del Spinner
            genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedGenre = (String) parent.getItemAtPosition(position);
                    if (getActivity() instanceof PlayerBoardgameActivity) {
                        PlayerBoardgameActivity activity = (PlayerBoardgameActivity) getActivity();
                        String query = searchView.getQuery().toString(); // Obtener texto de búsqueda actual
                        activity.filterList(query, selectedGenre); // Filtrar con búsqueda y género
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // No hacer nada
                }
            });
        }
    }
}