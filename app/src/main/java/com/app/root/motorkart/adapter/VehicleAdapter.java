package com.app.root.motorkart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.motorkart.BookBikeService;
import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.DeleteVehicle;
import com.app.root.motorkart.modelclass.VehicleModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {
DeleteVehicle dv;
    Context context;
    List<VehicleModel> vehicleList;
    public VehicleAdapter(Context context, List<VehicleModel> vehicleList,DeleteVehicle dv) {
        this.context = context;
        this.vehicleList = vehicleList;
        this.dv = dv ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.custom_layout_vehicles,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
holder.tv_vname.setText(vehicleList.get(position).getBrand_name());
holder.tv_vregno.setText(vehicleList.get(position).getRegistration_num());
holder.tv_vdate.setText(vehicleList.get(position).getCreated_on());
//holder.tv_vmodel.setText(vehicleList.get(position).getCreated_on());
holder.tv_vbrand.setText(vehicleList.get(position).getBrand_name());
holder.tv_vmodel.setText(vehicleList.get(position).getModel_name());

holder.ivsv_book.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context, BookBikeService.class);
        intent.putExtra("veh_idd",vehicleList.get(position).getId());
        context.startActivity(intent);
    }
});

holder.ivsv_delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dv.deletevehicle(vehicleList.get(position).getId());
    }
});
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_vregno,tv_vname,tv_vdate,tv_vbrand,tv_vmodel;
        ImageView ivsv_book,ivsv_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_vregno=itemView.findViewById(R.id.tv_vregno);
            tv_vname=itemView.findViewById(R.id.tv_vname);
            tv_vdate=itemView.findViewById(R.id.tv_vdate);
            tv_vbrand=itemView.findViewById(R.id.tv_vbrand);
            tv_vmodel=itemView.findViewById(R.id.tv_vmodel);
            ivsv_book=itemView.findViewById(R.id.ivsv_book);
            ivsv_delete=itemView.findViewById(R.id.ivsv_delete);
        }
    }
}
