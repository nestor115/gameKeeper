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


    //Constructor
    public SearchAdapter() {
        this.listElements = new ArrayList<>();
    }

    // Método para actualizar la lista de elementos
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

    //Crea nuevas vistas de los elementos
    @NonNull
    @Override
    public ListElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_search, parent, false);
        return new ListElementViewHolder(view);
    }

    //Vincula los datos a las vistas
    @Override
    public void onBindViewHolder(@NonNull ListElementViewHolder holder, int position) {
        ListElement listElement = listElements.get(position);
        holder.textViewName.setText(listElement.getName());

        String imageUrl = listElement.getImage();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageView);
        } else {//Si no encuentra la imagen pone una por defecto
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }

    }

    //Devuelve el número de elementos en la lista
    @Override
    public int getItemCount() {
        return listElements.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    //Define el comportamiento de los elementos de la lista
    public class ListElementViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageView;
        View detailsButton;
        View editButton;

        //Constructor de la clase
        public ListElementViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_BoardgameName);
            imageView = itemView.findViewById(R.id.iv_Boardgame);
            detailsButton = itemView.findViewById(R.id.btn_details);
            editButton = itemView.findViewById(R.id.btn_edit);
            //Listener para los el boton de detalles
            detailsButton.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(listElements.get(getAdapterPosition()));
                }
            });
            //Listener para los el boton de editar
            editButton.setOnClickListener(v -> {
                if (editClickListener != null) {
                    editClickListener.onEditClick(listElements.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(ListElement listElement);
    }

    public interface OnEditClickListener {
        void onEditClick(ListElement listElement);
    }

    private OnEditClickListener editClickListener;

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }
}


