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

import com.example.gamekeeper.R;
import com.example.gamekeeper.models.ListElement;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ListElementViewHolder> {

    private List<ListElement> listElements = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnDeleteClickListener deleteClickListener;

    // Constructor vacío
    public HomeAdapter() {
    }

    // Método para actualizar la lista
    public void submitList(List<ListElement> newListElements) {
        listElements.clear();
        if (newListElements != null) {
            listElements.addAll(newListElements);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_boardgame, parent, false);
        return new ListElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListElementViewHolder holder, int position) {
        ListElement listElement = listElements.get(position);
        holder.textViewName.setText(listElement.getName());

        // Convertir el byte[] a Bitmap
        byte[] elementImage = listElement.getImage();
        if (elementImage != null && elementImage.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(elementImage, 0, elementImage.length);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground); // Imagen por defecto
        }
    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }

    // ViewHolder: maneja la vista individual de cada elemento de la lista
    public class ListElementViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageView;
        View deleteButton;

        public ListElementViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_TittleItemBoardgame);
            imageView = itemView.findViewById(R.id.iv_ItemBoardgame);
            deleteButton = itemView.findViewById(R.id.btn_delete);

            // Configurar el clic en el elemento
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null && v != deleteButton) {
                    itemClickListener.onItemClick(listElements.get(getAdapterPosition()));
                }
            });

            // Configurar el clic en el botón de borrar
            deleteButton.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(listElements.get(getAdapterPosition()));
                }
            });
        }
    }

    // Interface para manejar los clics en los elementos
    public interface OnItemClickListener {
        void onItemClick(ListElement listElement);
    }

    // Interface para manejar los clics en el botón de borrar
    public interface OnDeleteClickListener {
        void onDeleteClick(ListElement listElement);
    }

    // Métodos para asignar los listeners de clic
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }
}
