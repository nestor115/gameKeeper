package com.example.gamekeeper.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.gamekeeper.Types.ListType;
import com.example.gamekeeper.models.ListElement;
import com.example.gamekeeper.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchBarFragment extends Fragment {
    private DatabaseHelper dB;
    private EditText editTextSearch;
    private OnSearchListener searchListener;
    private ListType listType;

    public interface OnSearchListener {
        void onSearchResults(List<ListElement> results); // Añadir este método
    }

    public SearchBarFragment() {
        // Constructor vacío requerido
    }

    public static SearchBarFragment newInstance(ListType listType) {
        SearchBarFragment fragment = new SearchBarFragment();
        Bundle args = new Bundle();
        args.putSerializable("LIST_TYPE", listType);
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listType = (ListType) getArguments().getSerializable("LIST_TYPE");
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
                performSearch(s.toString(), listType);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        performSearch("", listType);

        return view;
    }

    private void performSearch(String query, ListType listType) {
        Cursor cursor = null;
        int currentUserId = getCurrentUserId();

        if (listType == ListType.HOME) {
            if (query.isEmpty()) {
                cursor = dB.getAllBoardgamesForUser(currentUserId);  // Cambiar a la tabla correcta
            } else {
                cursor = dB.searchBoardgamesForUserByName(currentUserId, query);  // Cambiar a la tabla correcta
            }
        } else if (listType == ListType.SEARCH) {
            if (query.isEmpty()) {
                cursor = dB.getAllBoardgames();  // Cambiar a la tabla correcta
            } else {
                cursor = dB.searchBoardgamesByName(query);  // Cambiar a la tabla correcta
            }
        }

        List<ListElement> searchResults = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int columnIndexName = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_NAME);
                    int columnIndexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_ID);
                    int columnIndexImage = cursor.getColumnIndex(DatabaseHelper.COLUMN_BOARDGAME_PHOTO); // Asegúrate de incluir la columna de la imagen

                    if (columnIndexName != -1 && columnIndexId != -1 && columnIndexImage != -1) {
                        String name = cursor.getString(columnIndexName);
                        int id = cursor.getInt(columnIndexId);
                        byte[] image = cursor.getBlob(columnIndexImage); // Obtener la imagen en formato byte[]
                        ListElement element = new ListElement(name, id, image); // Crear ListElement con la imagen
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
            searchListener.onSearchResults(searchResults);  // Llamar al método correcto
        }
    }
    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}