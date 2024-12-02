package com.example.gamekeeper.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamekeeper.R;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ListElementViewHolder> {

    private List<ListElement> listElements = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnDeleteClickListener deleteClickListener;

    public HomeAdapter() {
    }
    // Método para actualizar la lista de elementos
    public void submitList(List<ListElement> newListElements) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_boardgame, parent, false);
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
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
//Devuelve el número de elementos en la lista
    @Override
    public int getItemCount() {
        return listElements.size();
    }
    //Define el comportamiento de los elementos de la lista
    public class ListElementViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageView;
        View deleteButton;
        //Constructor de la clase
        public ListElementViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_TittleItemBoardgame);
            imageView = itemView.findViewById(R.id.iv_ItemBoardgame);
            deleteButton = itemView.findViewById(R.id.btn_delete);
            //Listener para los clics en los elementos de la lista
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null && v != deleteButton) {
                    itemClickListener.onItemClick(listElements.get(getAdapterPosition()));
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(listElements.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ListElement listElement);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(ListElement listElement);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }
}
