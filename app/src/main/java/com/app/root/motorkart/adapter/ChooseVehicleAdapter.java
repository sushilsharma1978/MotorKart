package com.app.root.motorkart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.root.motorkart.ChooseVehicle;
import com.app.root.motorkart.R;
import com.app.root.motorkart.SelectVehicleBrand;
import com.app.root.motorkart.modelclass.ChooseVehiclemodel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseVehicleAdapter extends RecyclerView.Adapter<ChooseVehicleAdapter.ViewHolder> {
Context context;
int row_index;
    List<ChooseVehiclemodel> chooseVehiclemodelList;
    public ChooseVehicleAdapter(Context context, List<ChooseVehiclemodel> chooseVehiclemodelList) {
        this.context = context;
        this.chooseVehiclemodelList = chooseVehiclemodelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_choose_vehicle,parent,false) ;
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Picasso.with(context).load("http://digitalsolutionsplanet.com/demo/motorkart/uploads/product/"+
                chooseVehiclemodelList.get(position).getImage()).into(holder.iv_ch_image);
        holder.tv_chh_name.setText(chooseVehiclemodelList.get(position).getName());
        holder.ll_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*row_index=position;

notifyDataSetChanged();*/
                row_index = position;
                notifyDataSetChanged();
                holder.ll_car.setBackgroundColor(context.getResources().getColor(R.color.orange));
                    Intent intent=new Intent(context, SelectVehicleBrand.class);
                    intent.putExtra("vehiclee",chooseVehiclemodelList.get(position).getName());
                    intent.putExtra("vehicleeid",chooseVehiclemodelList.get(position).getCategory_id());
                    context.startActivity(intent);



            }
        });

        if(row_index==position){

        }
        else
        {
            holder.ll_car.setBackgroundColor(Color.parseColor("#DAD4D4"));

        }

    }

    @Override
    public int getItemCount() {
        return chooseVehiclemodelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_image1,ll_car;
TextView tv_chh_name;
ImageView iv_ch_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_image1=itemView.findViewById(R.id.ll_image1);
            ll_car=itemView.findViewById(R.id.ll_car);
            iv_ch_image=itemView.findViewById(R.id.iv_image_ch);
            tv_chh_name=itemView.findViewById(R.id.tv_chh_name);
        }
    }
}
