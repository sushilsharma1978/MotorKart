package com.app.root.motorkart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.SElectVehicle;
import com.app.root.motorkart.modelclass.BrandModel;
import com.app.root.motorkart.modelclass.FuelModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FuelAdapter extends RecyclerView.Adapter<FuelAdapter.ViewHolder> {
    Context context;
    SElectVehicle sv;
    List<FuelModel> fuelModelList;
    public FuelAdapter(Context context, List<FuelModel> fuelModelList,SElectVehicle sv) {
        this.context = context;
        this.fuelModelList = fuelModelList;
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
        holder.tv_fuel_name.setText(fuelModelList.get(position).getType());
        holder.ll_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
sv.selectvehiclefuel(position,fuelModelList.get(position).getId(),fuelModelList.get(position).getType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return fuelModelList.size();
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
