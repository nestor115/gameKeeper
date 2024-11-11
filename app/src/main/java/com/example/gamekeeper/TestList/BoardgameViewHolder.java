package com.example.gamekeeper.TestList;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gamekeeper.R;
import androidx.recyclerview.widget.RecyclerView;

public class BoardgameViewHolder extends RecyclerView.ViewHolder {

        ImageView ivItemBoardgame;
        TextView tvTittleItemBoardgame;
        Button btnDelete;

        public BoardgameViewHolder(View itemView) {
            super(itemView);
            ivItemBoardgame = itemView.findViewById(R.id.iv_ItemBoardgame);
            tvTittleItemBoardgame = itemView.findViewById(R.id.tv_TittleItemBoardgame);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

}
