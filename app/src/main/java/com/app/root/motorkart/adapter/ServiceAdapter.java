package com.app.root.motorkart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.DeleteVehicle;
import com.app.root.motorkart.modelclass.ServicesModel;
import com.app.root.motorkart.modelclass.VehicleModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    Context context;
    List<ServicesModel> serviceList;

    public ServiceAdapter(Context context, List<ServicesModel> serviceList) {
        this.context = context;
        this.serviceList = serviceList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_layout_service, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_svname.setText(serviceList.get(position).getMessage());
        holder.tv_svregno.setText(serviceList.get(position).getRegistration_num());
        holder.tv_svdate.setText(serviceList.get(position).getCreated_on());


    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_svregno, tv_svname, tv_svdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_svregno = itemView.findViewById(R.id.tv_svregno);
            tv_svname = itemView.findViewById(R.id.tv_svname);
            tv_svdate = itemView.findViewById(R.id.tv_svdate);

        }
    }
}