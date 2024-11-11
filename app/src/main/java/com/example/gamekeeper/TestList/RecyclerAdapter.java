package com.example.gamekeeper.TestList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gamekeeper.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.activities.ListElement;
import com.example.gamekeeper.models.Boardgame;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<BoardgameViewHolder> {

    private List<ListElement> elementsList;
    private ListType listType;

    // Constructor
    public RecyclerAdapter(List<ListElement> elementsList, ListType listType) {
        this.elementsList = elementsList;
        this.listType = listType;
    }

    @NonNull
    @Override
    public BoardgameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (listType) {
            case COLLECTION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_boardgame, parent, false);
                break;
            case SEARCH:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, parent, false);
                break;
           /* case FRIENDS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boardgame_friends, parent, false);
                break;*/
            default:
                throw new IllegalStateException("Unexpected value: " + listType);
        }
        return new BoardgameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardgameViewHolder holder, int position) {

        ListElement element = elementsList.get(position);
        holder.tvTittleItemBoardgame.setText(element.getName());


        switch (listType) {
            case COLLECTION:

                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setOnClickListener(v -> {


                });
                break;

            case SEARCH:
                holder.btnDelete.setVisibility(View.GONE);

                break;

            case FRIENDS:

                holder.btnDelete.setVisibility(View.GONE);

                break;

            default:

                throw new IllegalStateException("Unexpected value: " + listType);
        }
    }

    @Override
    public int getItemCount() {
        return elementsList.size(); // Número de juegos en la lista
    }






}
