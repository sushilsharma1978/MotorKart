package com.app.root.motorkart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.motorkart.Enquiry;
import com.app.root.motorkart.PartStore_Location;
import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.SaveBookmark;
import com.app.root.motorkart.modelclass.StoreDetailModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyStoreDetailAdapter extends RecyclerView.Adapter<MyStoreDetailAdapter.ViewHolder> {

    Context context;
    List<StoreDetailModel> storedetaillist;
    boolean check=false;
    SaveBookmark saveBookmark;
    public MyStoreDetailAdapter(Context context, List<StoreDetailModel> storedetaillist,SaveBookmark saveBookmark
                              ) {
        this.context = context;
        this.storedetaillist = storedetaillist;
        this.saveBookmark = saveBookmark;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.custom_mystores,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_st_number.setText(storedetaillist.get(position).getStore_num());
        holder.tv_st_inst.setText("Rs. "+storedetaillist.get(position).getInstt_charge());
        holder.tv_st_dsct.setText(storedetaillist.get(position).getDiscount()+"%");
        holder.tv_st_name.setText(storedetaillist.get(position).getName());
        if(storedetaillist.get(position).getRatings().equals("1")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.border_star);
            holder.iv_star_three.setImageResource(R.drawable.border_star);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if(storedetaillist.get(position).getRatings().equals("2")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.border_star);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if(storedetaillist.get(position).getRatings().equals("3")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if(storedetaillist.get(position).getRatings().equals("4")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.star_solid);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if(storedetaillist.get(position).getRatings().equals("5")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.star_solid);
            holder.iv_star_five.setImageResource(R.drawable.star_solid);
        }


        holder.tv_vw_store1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PartStore_Location.class);
                intent.putExtra("store_id", storedetaillist.get(position).getSid());
                intent.putExtra("store_name", storedetaillist.get(position).getName());

                context.startActivity(intent);
            }
        });
        holder.iv_wish3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.iv_wish3.setImageResource(R.drawable.emptyheart);
                    saveBookmark.addbookmark("0",storedetaillist.get(position).getSid());

            }
        });
    }

    @Override
    public int getItemCount() {
        return storedetaillist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_star_one,iv_star_two,iv_star_three,iv_star_four,iv_star_five,iv_wish3;
        TextView tv_st_number,tv_st_inst,tv_st_dsct,tv_vw_enquiry,tv_vw_store1,tv_st_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_st_number=itemView.findViewById(R.id.tv_st_number1);
            tv_st_inst=itemView.findViewById(R.id.tv_st_inst1);
            tv_st_dsct=itemView.findViewById(R.id.tv_st_dsct1);
            iv_star_one=itemView.findViewById(R.id.iv_star_one1);
            iv_star_two=itemView.findViewById(R.id.iv_star_two1);
            iv_star_three=itemView.findViewById(R.id.iv_star_three1);
            iv_star_four=itemView.findViewById(R.id.iv_star_four1);
            iv_star_five=itemView.findViewById(R.id.iv_star_five1);
            tv_st_name=itemView.findViewById(R.id.tv_st_name1);
            tv_vw_store1=itemView.findViewById(R.id.tv_vw_store1);
            iv_wish3=itemView.findViewById(R.id.iv_wish3);
        }
    }
}