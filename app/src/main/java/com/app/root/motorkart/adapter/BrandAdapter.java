package com.app.root.motorkart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.SElectVehicle;
import com.app.root.motorkart.modelclass.BrandModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
Context context;
int row_index=-1;
    List<BrandModel> brandModelList;
    SElectVehicle sv;
    public BrandAdapter(Context context, List<BrandModel> brandModelList,SElectVehicle sv) {
        this.context = context;
        this.brandModelList = brandModelList;
        this.sv = sv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_brand,parent,false) ;
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Picasso.with(context).load(brandModelList.get(position).getImage()).into(holder.iv_brand_image);
        holder.tv_br_name.setText(brandModelList.get(position).getBrand_name());

        holder.ll_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            sv.selectvehiclebrand(position,brandModelList.get(position).getBrand_id(),brandModelList.get(position).getBrand_name());
            }
        });

    }

    @Override
    public int getItemCount() {
        return brandModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_br_name;
        ImageView iv_brand_image;
        LinearLayout ll_brand;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_br_name=itemView.findViewById(R.id.tv_br_name);
            iv_brand_image=itemView.findViewById(R.id.iv_br_image);
            ll_brand=itemView.findViewById(R.id.ll_brand);
        }
    }
}
