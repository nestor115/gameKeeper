package com.example.gamekeeper.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.models.ListElement;
import com.example.gamekeeper.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ListElementViewHolder> {

    private List<ListElement> listElements = new ArrayList<>(); // Lista de ListElement
    private OnItemClickListener listener;
    private List<String> playerNames;


    public PlayerAdapter(List<String> playerNames) {
        this.playerNames = playerNames;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_boardgame, parent, false);
        return new ListElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListElementViewHolder holder, int position) {
        ListElement listElement = listElements.get(position);
        holder.itemTitle.setText(listElement.getName());


        byte[] elementImage = listElement.getImage();
        if (elementImage != null && elementImage.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(elementImage, 0, elementImage.length);
            holder.itemIcon.setImageBitmap(bitmap);
        } else {
            holder.itemIcon.setImageResource(R.drawable.ic_launcher_foreground);
        }
        if (playerNames != null && !playerNames.isEmpty()) {
            updatePlayerInfo(holder.itemName1, holder.checkBox1, 0);
            updatePlayerInfo(holder.itemName2, holder.checkBox2, 1);
            updatePlayerInfo(holder.itemName3, holder.checkBox3, 2);
            updatePlayerInfo(holder.itemName4, holder.checkBox4, 3);
        }
    }
    private void updatePlayerInfo(TextView itemName, CheckBox checkBox, int playerIndex) {
        if (playerNames.size() > playerIndex) {
            itemName.setText(playerNames.get(playerIndex));
            checkBox.setVisibility(View.VISIBLE);
        } else {
            itemName.setText("");
            checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }

    public class ListElementViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemName1, itemName2, itemName3, itemName4;
        ImageView itemIcon;
        CheckBox checkBox1, checkBox2, checkBox3, checkBox4;

        public ListElementViewHolder(@NonNull View itemView) {
            super(itemView);

            // Referencias a las vistas
            itemTitle = itemView.findViewById(R.id.item_title);
            itemIcon = itemView.findViewById(R.id.item_icon);

            // Referencias a los CheckBoxes y TextViews para los nombres
            checkBox1 = itemView.findViewById(R.id.checkBox1);
            checkBox2 = itemView.findViewById(R.id.checkBox2);
            checkBox3 = itemView.findViewById(R.id.checkBox3);
            checkBox4 = itemView.findViewById(R.id.checkBox4);

            itemName1 = itemView.findViewById(R.id.item_name1);
            itemName2 = itemView.findViewById(R.id.item_name2);
            itemName3 = itemView.findViewById(R.id.item_name3);
            itemName4 = itemView.findViewById(R.id.item_name4);

            // Configurar el clic en el elemento (este es el comportamiento original que tenías)
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(listElements.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }


    // Interface para manejar los clics
    public interface OnItemClickListener {
        void onItemClick(ListElement listElement);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
