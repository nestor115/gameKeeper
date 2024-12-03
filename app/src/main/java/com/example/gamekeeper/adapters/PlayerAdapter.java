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

import com.bumptech.glide.Glide;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.ListElement;
import com.example.gamekeeper.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ListElementViewHolder> {
    private DatabaseHelper db;
    private int currentUserId;
    private List<ListElement> listElements = new ArrayList<>();
    private OnItemClickListener listener;
    private List<String> playerNames;
    private RecyclerView recyclerView;
    private boolean[][] checkBoxStates;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public PlayerAdapter(List<String> playerNames) {
        this.playerNames = playerNames;
    }
    public PlayerAdapter(List<String> playerNames, DatabaseHelper db, int currentUserId) {
        this.playerNames = playerNames;
        this.db = db;
        this.currentUserId = currentUserId;
    }

    public void submitList(List<ListElement> newListElements) {
        listElements.clear();
        if (newListElements != null) {
            listElements.addAll(newListElements);
        }
        checkBoxStates = new boolean[listElements.size()][playerNames.size()];
        notifyDataSetChanged();
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
    private void updatePlayerInfo(ListElementViewHolder holder, int playerIndex, int position, boolean alreadyPlayed) {
        if (playerIndex < playerNames.size()) {
            holder.getPlayerName(playerIndex).setText(playerNames.get(playerIndex));
            holder.getCheckBox(playerIndex).setVisibility(View.VISIBLE);
            holder.getCheckBox(playerIndex).setChecked(alreadyPlayed);
            holder.getCheckBox(playerIndex).setEnabled(!alreadyPlayed); // Deshabilita el CheckBox si ya ha jugado

            if (!alreadyPlayed) {
                holder.getCheckBox(playerIndex).setOnCheckedChangeListener((buttonView, isChecked) -> {
                    checkBoxStates[position][playerIndex] = isChecked;
                });
            } else {
                holder.getCheckBox(playerIndex).setOnCheckedChangeListener(null); // Elimina el listener para evitar conflictos
            }
        }
    }

    public CheckBox getCheckBoxByIndex(int itemIndex, int playerIndex) {
        if (recyclerView == null) return null;

        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(itemIndex);
        if (holder instanceof PlayerAdapter.ListElementViewHolder) {
            PlayerAdapter.ListElementViewHolder viewHolder = (PlayerAdapter.ListElementViewHolder) holder;

            switch (playerIndex) {
                case 0: return viewHolder.checkBox1;
                case 1: return viewHolder.checkBox2;
                case 2: return viewHolder.checkBox3;
                case 3: return viewHolder.checkBox4;
            }
        }
        return null;
    }
    public boolean getCheckBoxState(int itemIndex, int playerIndex) {
        return checkBoxStates[itemIndex][playerIndex];  // Obtener el estado del CheckBox
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

        // Método para obtener el nombre del jugador
        public TextView getPlayerName(int index) {
            switch (index) {
                case 0: return itemName1;
                case 1: return itemName2;
                case 2: return itemName3;
                case 3: return itemName4;
                default: return null;
            }
        }

        // Método para obtener el CheckBox
        public CheckBox getCheckBox(int index) {
            switch (index) {
                case 0: return checkBox1;
                case 1: return checkBox2;
                case 2: return checkBox3;
                case 3: return checkBox4;
                default: return null;
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(ListElement listElement);
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
