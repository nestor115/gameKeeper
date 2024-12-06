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
import com.example.gamekeeper.activities.HomeActivity;
import com.example.gamekeeper.activities.PlayerBoardgameActivity;
import com.example.gamekeeper.activities.SearchActivity;

import java.util.List;

public class PlayerFragment extends Fragment {
    private SearchView searchView;
    private Spinner genreSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        searchView = view.findViewById(R.id.searchView);
        genreSpinner = view.findViewById(R.id.genreSpinner);

        setupSpinner();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearchChange(newText);
                return true;
            }
        });

        searchView.setOnClickListener(v -> searchView.setIconified(false));

        return view;
    }

    private void setupSpinner() {
        if (getActivity() instanceof PlayerBoardgameActivity) {
            setupSpinnerForActivity(getActivity());
        } else if (getActivity() instanceof HomeActivity) {
            setupSpinnerForActivity(getActivity());
        } else if (getActivity() instanceof SearchActivity) {
            setupSpinnerForActivity(getActivity());
        }
    }

    private void setupSpinnerForActivity(Object activity) {
        List<String> genres = null;

        if (activity instanceof PlayerBoardgameActivity) {
            genres = ((PlayerBoardgameActivity) activity).getGenres();
        } else if (activity instanceof HomeActivity) {
            genres = ((HomeActivity) activity).getGenres();
        }else if (activity instanceof SearchActivity) {
            genres = ((SearchActivity) activity).getGenres();
        }

        if (genres != null) {
            genres.add(0, "Todos");

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, genres);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genreSpinner.setAdapter(spinnerAdapter);

            genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedGenre = (String) parent.getItemAtPosition(position);
                    handleSearchChange(searchView.getQuery().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void handleSearchChange(String query) {
        String selectedGenre = (String) genreSpinner.getSelectedItem();

        if (getActivity() instanceof PlayerBoardgameActivity) {
            PlayerBoardgameActivity activity = (PlayerBoardgameActivity) getActivity();
            activity.filterList(query, selectedGenre);
        } else if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.filterList(query, selectedGenre);
        } else if (getActivity() instanceof SearchActivity) {
            SearchActivity activity = (SearchActivity) getActivity();
            activity.filterList(query, selectedGenre);
        }
    }
}