package com.app.root.motorkart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.root.motorkart.R;
import com.app.root.motorkart.modelclass.RateListModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder> {

Context context;
    List<RateListModel> rateList;
    public RateAdapter(Context context, List<RateListModel> rateList) {
        this.context = context;
        this.rateList = rateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_ratelist,parent,false);
       return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.name.setText(rateList.get(position).getName());
holder.price.setText("Rs."+rateList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tv_ratename);
            price=itemView.findViewById(R.id.tv_rate_price);
        }
    }
}
