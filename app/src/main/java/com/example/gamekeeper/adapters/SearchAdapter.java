package com.example.gamekeeper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamekeeper.R;
import com.example.gamekeeper.models.ListElement;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ListElementViewHolder> {
    private List<ListElement> listElements;
    private OnItemClickListener itemClickListener;


    //constructor vacio
    public SearchAdapter() {
        this.listElements = new ArrayList<>();
    }

    public void submitList(List<ListElement> newListElements) {
        if (listElements == null) {
            listElements = new ArrayList<>();
        }
        listElements.clear();
        if (newListElements != null) {
            listElements.addAll(newListElements);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_search, parent, false);
        return new ListElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListElementViewHolder holder, int position) {
        ListElement listElement = listElements.get(position);
        holder.textViewName.setText(listElement.getName());

        String imageUrl = listElement.getImage();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }

    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public class ListElementViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageView;
        View detailsButton;

        public ListElementViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_BoardgameName);
            imageView = itemView.findViewById(R.id.iv_Boardgame);
            detailsButton = itemView.findViewById(R.id.btn_details);
            //Listener para los clics en los elementos de la lista
            detailsButton.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(listElements.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(ListElement listElement);
    }
}


