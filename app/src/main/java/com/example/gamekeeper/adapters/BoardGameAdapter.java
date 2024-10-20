package com.example.gamekeeper.adapters;

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
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.BoardGame;
import java.util.List;

public class BoardGameAdapter extends RecyclerView.Adapter<BoardGameAdapter.BoardGameViewHolder> {

    private final List<BoardGame> boardGames;
    private final DatabaseHelper dbHelper; // Añadir una referencia a DatabaseHelper
    private final int userId; // Añadir el userId
    // Constructor
    public BoardGameAdapter(List<BoardGame> boardGames, DatabaseHelper dbHelper, int userId) {
        this.boardGames = boardGames;
        this.dbHelper = dbHelper; // Inicializar DatabaseHelper
        this.userId = userId; // Inicializar userId
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

        holder.deleteButton.setOnClickListener(v -> {
            Log.d("BoardGameAdapter", "Removing boardgame with ID: " + boardGame.getId() + " for user ID: " + userId);

            boolean success = dbHelper.removeUserBoardgame(userId, boardGame.getId()); // Usar el userId
            if (success) {
                boardGames.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, boardGames.size());
                Toast.makeText(holder.itemView.getContext(), "Juego eliminado.", Toast.LENGTH_SHORT).show(); // Mensaje de éxito
            } else {
                // Manejo de errores: muestra un mensaje al usuario si la eliminación falla
                Toast.makeText(holder.itemView.getContext(), "Error al eliminar el juego.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public int getItemCount() {
        return boardGames.size();
    }

    // ViewHolder interno para representar cada elemento del RecyclerView
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
            titleTextView.setText(boardGame.getName()); // Asegúrate de que `getTitle()` sea correcto

            // Cargar la imagen
            byte[] photo = boardGame.getPhoto(); // Asegúrate de que el método getPhoto() exista en tu modelo
            if (photo != null && photo.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                imageView.setImageBitmap(bitmap);
            } else {
                // Cargar imagen por defecto
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
    }
}
