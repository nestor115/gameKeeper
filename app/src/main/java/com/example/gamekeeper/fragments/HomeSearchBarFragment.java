package com.example.gamekeeper.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gamekeeper.R;

public class HomeSearchBarFragment extends Fragment {
    private EditText etSearch;
    private OnSearchListener onSearchListener;

    public interface OnSearchListener {
        void onSearch(String query);
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.onSearchListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_bar, container, false);

        etSearch = rootView.findViewById(R.id.et_Search);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (onSearchListener != null) {
                    onSearchListener.onSearch(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return rootView;
    }
}