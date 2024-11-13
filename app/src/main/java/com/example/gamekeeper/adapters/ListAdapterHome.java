package com.example.gamekeeper.adapters;

import android.content.Context;
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
import com.example.gamekeeper.activities.ListElement;
import com.example.gamekeeper.helpers.DatabaseHelper;
import com.example.gamekeeper.models.Boardgame;
import java.util.List;

public class ListAdapterHome extends RecyclerView.Adapter<ListAdapterHome.ViewHolder> {
        private List<ListElement> listElements;
        private Context context;
        private int userId;
        private OnItemClickListener onItemClickListener;

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.onItemClickListener = listener;
        }

        public ListAdapterHome(List<ListElement> listElements, Context context, int userId) {
            this.listElements = listElements;
            this.context = context;
            this.userId = userId;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_boardgame, parent, false);
            return new ViewHolder(view, onItemClickListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ListElement listElement = listElements.get(position);
            holder.textViewName.setText(listElement.getName());

            // Mostrar la imagen
            if (listElement.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(listElement.getImage(), 0, listElement.getImage().length);
                holder.imageView.setImageBitmap(bitmap);
            }
            // Si se requiere un clic en el ítem
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int pos = holder.getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(pos);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return listElements.size();
        }


        public void updateData(List<ListElement> newListElements) {
            this.listElements = newListElements;
            Log.d("ListAdapterHome", "Datos actualizados: " + newListElements.size() + " elementos");
            notifyDataSetChanged(); // Notificar que los datos han cambiado y el RecyclerView debe actualizarse
        }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView; // Agregar un ImageView para la imagen

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_TittleItemBoardgame);
            imageView = itemView.findViewById(R.id.iv_ItemBoardgame); // Asegúrate de que este ID exista en tu layout

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}