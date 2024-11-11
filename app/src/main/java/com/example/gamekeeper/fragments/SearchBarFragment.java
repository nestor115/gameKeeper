package com.example.gamekeeper.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gamekeeper.R;
import com.example.gamekeeper.activities.ListElement;
import com.example.gamekeeper.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchBarFragment extends Fragment {
    private DatabaseHelper dB;
    private EditText editTextSearch;
    private OnSearchListener searchListener;

    public interface OnSearchListener {
        void onSearchResults(List<ListElement> results); // Añadir este método
    }

    public SearchBarFragment() {
        // Constructor vacío requerido
    }

    public static SearchBarFragment newInstance() {
        return new SearchBarFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dB = new DatabaseHelper(context);

        try {
            searchListener = (OnSearchListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar OnSearchListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_bar, container, false);

        editTextSearch = view.findViewById(R.id.et_Search);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        performSearch("");

        return view;
    }

    private void performSearch(String query) {
        Cursor cursor;

        if (query.isEmpty()) {
            cursor = dB.getAllBoardgames();
        } else {
            cursor = dB.searchBoardgamesByName(query);
        }

        List<ListElement> searchResults = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int columnIndexName = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NAME);
                    int columnIndexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_ID);

                    if (columnIndexName != -1 && columnIndexId != -1) {
                        String name = cursor.getString(columnIndexName);
                        int id = cursor.getInt(columnIndexId);
                        ListElement element = new ListElement(name, id);
                        searchResults.add(element);
                    } else {
                        Log.e("SEARCH_BAR_FRAGMENT", "Columnas no encontradas");
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        // Notificar a la actividad con los resultados
        if (searchListener != null) {
            searchListener.onSearchResults(searchResults); // Llamar al método correcto
        }


        // Notificar a la actividad con los resultados
        if (searchListener != null) {
            searchListener.onSearchResults(searchResults);
        }
    }
}