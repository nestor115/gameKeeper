package com.example.gamekeeper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;
import com.example.gamekeeper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ListElementViewHolder> {
    private DatabaseHelper db;
    private int currentUserId;
    private List<ListElement> listElements = new ArrayList<>();
    private OnItemClickListener listener;
    private List<String> playerNames;
    private RecyclerView recyclerView;
    private boolean[][] checkBoxStates;
    private Map<Integer, boolean[]> checkBoxStateMap = new HashMap<>();  // Mapa para guardar el estado de los CheckBox

    // Inicializa la lista de nombres de jugadores, la base de datos y el ID del usuario actual.
    public PlayerAdapter(List<String> playerNames, DatabaseHelper db, int currentUserId) {
        this.playerNames = playerNames;
        this.db = db;
        this.currentUserId = currentUserId;
    }

    // Método para establecer el
    //Actuañiza la lista de elementos de la lista y notifica al adaptador del cambio.
    public void submitList(List<ListElement> newListElements) {
        listElements.clear();
        if (newListElements != null) {
            listElements.addAll(newListElements);
        }
        int maxPlayers = Math.min(playerNames.size(), 4);
        checkBoxStates = new boolean[listElements.size()][maxPlayers]; // Asegúrate de que el tamaño se actualice con la nueva lista.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_player_boardgame, parent, false);
        return new ListElementViewHolder(view);
    }

    //Vincula los datos a las vistas
    @Override
    public void onBindViewHolder(@NonNull ListElementViewHolder holder, int position) {
        ListElement listElement = listElements.get(position);
        holder.itemTitle.setText(listElement.getName());

        String imageUrl = listElement.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.itemIcon);
        } else {
            holder.itemIcon.setImageResource(R.drawable.ic_launcher_foreground);
        }

        for (int i = 0; i < playerNames.size(); i++) {
            int playerId = db.getPlayerIdByName(playerNames.get(i), currentUserId);
            boolean alreadyPlayed = db.hasPlayerAlreadyPlayed(playerId, listElement.getId());
            updatePlayerInfo(holder, i, position, alreadyPlayed);
        }

        for (int i = playerNames.size(); i < 4; i++) {
            holder.getCheckBox(i).setVisibility(View.GONE);
            holder.getPlayerName(i).setVisibility(View.GONE);
        }
    }

    //Actualiza la información de los checkbox en la vista
    private void updatePlayerInfo(ListElementViewHolder holder, int playerIndex, int position, boolean alreadyPlayed) {
        if (position < 0 || position >= checkBoxStates.length || playerIndex < 0 || playerIndex >= checkBoxStates[position].length) {
            return;
        }
        // Establece el nombre del jugador, muestra los checkbox y actualiza su estado
        if (playerIndex < playerNames.size()) {
            holder.getPlayerName(playerIndex).setText(playerNames.get(playerIndex));
            holder.getCheckBox(playerIndex).setVisibility(View.VISIBLE);
            holder.getCheckBox(playerIndex).setChecked(alreadyPlayed);
            holder.getCheckBox(playerIndex).setEnabled(!alreadyPlayed);

            if (!alreadyPlayed) {
                //Actualiza el estado cuando se selecciona o deselecciona el checkbox
                holder.getCheckBox(playerIndex).setOnCheckedChangeListener((buttonView, isChecked) -> {
                    checkBoxStates[position][playerIndex] = isChecked;
                    updateCheckBoxState(position, playerIndex, isChecked);
                });
            } else {
                //Quita el listener si el jugador ya ha jugado y no se puede clickar el checkbox
                holder.getCheckBox(playerIndex).setOnCheckedChangeListener(null);
            }
        }
    }

    //Actualiza el estado del checkbox de un jugador en una posición específica
    private void updateCheckBoxState(int position, int playerIndex, boolean isChecked) {
        if (!checkBoxStateMap.containsKey(position)) {
            checkBoxStateMap.put(position, new boolean[4]);
        }
        checkBoxStateMap.get(position)[playerIndex] = isChecked;
    }


    //Obtiene el estado del checkbox de un jugador en una posición específica
    public boolean getCheckBoxState(int itemIndex, int playerIndex) {
        return checkBoxStateMap.containsKey(itemIndex) && checkBoxStateMap.get(itemIndex)[playerIndex];
    }

    //Devuelve el número de elementos en la lista
    @Override
    public int getItemCount() {
        return listElements.size();
    }

    //Define el comportamiento de los elementos de la lista
    public class ListElementViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemName1, itemName2, itemName3, itemName4;
        ImageView itemIcon;
        CheckBox checkBox1, checkBox2, checkBox3, checkBox4;

        //Constructor de la clase
        public ListElementViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.item_title);
            itemIcon = itemView.findViewById(R.id.item_icon);
            checkBox1 = itemView.findViewById(R.id.checkBox1);
            checkBox2 = itemView.findViewById(R.id.checkBox2);
            checkBox3 = itemView.findViewById(R.id.checkBox3);
            checkBox4 = itemView.findViewById(R.id.checkBox4);

            itemName1 = itemView.findViewById(R.id.item_name1);
            itemName2 = itemView.findViewById(R.id.item_name2);
            itemName3 = itemView.findViewById(R.id.item_name3);
            itemName4 = itemView.findViewById(R.id.item_name4);
        }

        //Devuelve el nombre del jugador en una posición específica
        public TextView getPlayerName(int index) {
            switch (index) {
                case 0:
                    return itemName1;
                case 1:
                    return itemName2;
                case 2:
                    return itemName3;
                case 3:
                    return itemName4;
                default:
                    return null;
            }
        }

        //Devuelve el checkbox en una posición específica
        public CheckBox getCheckBox(int index) {
            switch (index) {
                case 0:
                    return checkBox1;
                case 1:
                    return checkBox2;
                case 2:
                    return checkBox3;
                case 3:
                    return checkBox4;
                default:
                    return null;
            }
        }
    }

    //Interfaz para manejar los clics en los elementos de la lista
    public interface OnItemClickListener {
        void onItemClick(ListElement listElement);
    }

}
