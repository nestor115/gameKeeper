package com.example.gamekeeper.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamekeeper.R;
import com.example.gamekeeper.activities.DetailActivity;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.BoardGame;
import java.util.List;

public class BoardGameAdapter extends RecyclerView.Adapter<BoardGameAdapter.BoardGameViewHolder> {

    private final List<BoardGame> boardGames;
    private final DatabaseHelper dbHelper;
    private final int userId;
    // Constructor
    public BoardGameAdapter(List<BoardGame> boardGames, DatabaseHelper dbHelper, int userId) {
        this.boardGames = boardGames;
        this.dbHelper = dbHelper;
        this.userId = userId;
    }

    @NonNull
    @Override
    public BoardGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_boardgame, parent, false);
        return new BoardGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardGameViewHolder holder, int position) {
        BoardGame boardGame = boardGames.get(position);
        holder.bind(boardGame);
        // Log para verificar el ID del juego cuando se hace clic
        Log.d("BoardGameAdapter", "ID del juego seleccionado: " + boardGame.getId());
        holder.deleteButton.setOnClickListener(v -> {
            Log.d("BoardGameAdapter", "Removing boardgame with ID: " + boardGame.getId() + " for user ID: " + userId);

            boolean success = dbHelper.removeUserBoardgame(userId, boardGame.getId());
            if (success) {
                boardGames.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, boardGames.size());
                Toast.makeText(holder.itemView.getContext(), "Juego eliminado.", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(holder.itemView.getContext(), "Error al eliminar el juego.", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Log.d("BoardGameAdapter", "Clic en el juego con ID: " + boardGame.getId());
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.BOARDGAME_ID, boardGame.getId());
            intent.putExtra(DetailActivity.NAME_VIEW, "HOME");

            holder.itemView.getContext().startActivity(intent);
        });
    }




    @Override
    public int getItemCount() {
        return boardGames.size();
    }


    public static class BoardGameViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleTextView;
        private final Button deleteButton;

        public BoardGameViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_ItemBoardgame);
            titleTextView = itemView.findViewById(R.id.tv_TittleItemBoardgame);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(BoardGame boardGame) {
            titleTextView.setText(boardGame.getName());

            // Cargar la imagen
            byte[] photo = boardGame.getPhoto();
            if (photo != null && photo.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                imageView.setImageBitmap(bitmap);
            } else {

                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
    }
}
