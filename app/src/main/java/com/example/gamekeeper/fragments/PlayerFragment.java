package com.example.gamekeeper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.gamekeeper.R;
import com.example.gamekeeper.activities.PlayerBoardgameActivity;

public class PlayerFragment extends Fragment {

    private SearchView searchView;  // Usar android.widget.SearchView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        // Inicializar el SearchView
        searchView = view.findViewById(R.id.searchView);

        // Configurar el listener para el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No necesitamos hacer nada cuando el texto se envíe, solo filtrar
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Llamar al método en la actividad para filtrar la lista de nombres
                if (getActivity() instanceof PlayerBoardgameActivity) {
                    PlayerBoardgameActivity activity = (PlayerBoardgameActivity) getActivity();
                    activity.filterList(newText); // Pasar el texto de búsqueda a la actividad
                }
                return true;
            }
        });

        return view;
    }
}