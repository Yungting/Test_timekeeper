package com.example.user.timekeeper_testtest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class normal_music_adapter extends RecyclerView.Adapter<normal_music_adapter.viewholder> {
    @NonNull
    private ArrayList<normal_music_item> itemArrayList;
    private OnItemClickListener nListener;

    //監聽事件
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        nListener = listener;
    }

    //adapter
    public normal_music_adapter(ArrayList<normal_music_item> itemarraylist){
        itemArrayList = itemarraylist;
    }

    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.music_item, viewGroup, false);
        viewholder vh = new viewholder(v, nListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder viewholder, int i) {
        normal_music_item item = itemArrayList.get(i);
        viewholder.music_name.setText(item.getMusic_name());
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder{
        public TextView music_name;

        public viewholder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            music_name = itemView.findViewById(R.id.music_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}
