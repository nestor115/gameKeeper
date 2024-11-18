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

import com.example.gamekeeper.models.ListElement;
import com.example.gamekeeper.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterFriends extends RecyclerView.Adapter<ListAdapterFriends.ListElementViewHolder> {

    private List<ListElement> listElements = new ArrayList<>(); // Lista de ListElement
    private OnItemClickListener listener;

    // Constructor vacío
    public ListAdapterFriends() {
    }

    // Método para actualizar la lista
    public void submitList(List<ListElement> newListElements) {
        listElements.clear(); // Limpiamos la lista anterior
        if (newListElements != null) {
            listElements.addAll(newListElements); // Añadimos los nuevos items
        }
        notifyDataSetChanged(); // Notificamos que los datos han cambiado
    }

    @NonNull
    @Override
    public ListElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_boardgame, parent, false);
        return new ListElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListElementViewHolder holder, int position) {
        ListElement listElement = listElements.get(position);
        holder.itemTitle.setText(listElement.getName());

        // Convertir el byte[] a Bitmap
        byte[] elementImage = listElement.getImage();
        if (elementImage != null && elementImage.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(elementImage, 0, elementImage.length);
            holder.itemIcon.setImageBitmap(bitmap);
        } else {
            holder.itemIcon.setImageResource(R.drawable.ic_launcher_foreground); // Imagen por defecto
        }
    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }

    // ViewHolder: maneja la vista individual de cada elemento de la lista
    public class ListElementViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView itemIcon;

        public ListElementViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemIcon = itemView.findViewById(R.id.item_icon);

            // Configurar el clic en el elemento
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(listElements.get(getAdapterPosition()));
                }
            });
        }
    }

    // Interface para manejar los clics
    public interface OnItemClickListener {
        void onItemClick(ListElement listElement);
    }

    // Método para asignar el listener de clic
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
