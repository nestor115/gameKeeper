package com.example.gamekeeper.activities;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamekeeper.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<ListElement> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, parent,false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder,final int position) {
        holder.bindData(mData.get(position));
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setItems(List<ListElement> items) { mData = items;}

   public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView name;
        ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
        }
        void bindData(final ListElement item){
            name.setText(item.getName());

        }
   }
}
