package com.app.root.motorkart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.SElectVehicle;
import com.app.root.motorkart.modelclass.FuelModel;
import com.app.root.motorkart.modelclass.Modelmodel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {
    Context context;
    SElectVehicle sv;
    List<Modelmodel> modellist;
    public ModelAdapter(Context context, List<Modelmodel> modellist,SElectVehicle sv) {
        this.context = context;
        this.modellist = modellist;
        this.sv = sv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_fuel,parent,false) ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_fuel_name.setText(modellist.get(position).getName());

        holder.ll_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
sv.selectvehiclemodel(position,modellist.get(position).getId(),modellist.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_fuel_name;
        LinearLayout ll_fuel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fuel_name=itemView.findViewById(R.id.tv_fuel_name);
            ll_fuel=itemView.findViewById(R.id.ll_fuel);

        }
    }
}
