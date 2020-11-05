package com.app.root.motorkart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.root.motorkart.R;

import java.util.ConcurrentModificationException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EngineAdapter extends RecyclerView.Adapter<EngineAdapter.ViewHolder> {

    Context context;
    Integer[] enginelist;
    public EngineAdapter(Context context, Integer[] enginelist) {
        this.context = context;
        this.enginelist = enginelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_engine,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.image_engine.setImageResource(enginelist[position]);
    }

    @Override
    public int getItemCount() {
        return enginelist.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_engine;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_engine=itemView.findViewById(R.id.image_engine);
        }
    }
}
