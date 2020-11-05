package com.app.root.motorkart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.DeleteBooking;
import com.app.root.motorkart.interfaces.DeleteVehicle;
import com.app.root.motorkart.modelclass.BookingStatusModel;
import com.app.root.motorkart.modelclass.VehicleModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookingStatusAdapter extends RecyclerView.Adapter<BookingStatusAdapter.ViewHolder> {
    DeleteBooking dv;
    Context context;
    List<BookingStatusModel> modelList;
    public BookingStatusAdapter(Context context, List<BookingStatusModel> modelList, DeleteBooking dv) {
        this.context = context;
        this.modelList = modelList;
        this.dv = dv ;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.custom_layout_booking,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_vname.setText(modelList.get(position).getName());
        holder.tv_vregno.setText(modelList.get(position).getRegistration_num());
        holder.tv_vdate.setText(modelList.get(position).getCreated_on());



        holder.ivsv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.deletbooking(modelList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_vregno,tv_vname,tv_vdate,id_tv_bkstatus;
        Button ivsv_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_vregno=itemView.findViewById(R.id.tv_bkregno);
            tv_vname=itemView.findViewById(R.id.tv_bkname);
            tv_vdate=itemView.findViewById(R.id.tv_bkdate);
            ivsv_delete=itemView.findViewById(R.id.ivbk_delete);
            id_tv_bkstatus=itemView.findViewById(R.id.id_tv_bkstatus);
        }
    }
}
