package com.app.root.motorkart.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.app.root.motorkart.HomeScreeeen;
import com.app.root.motorkart.MainActivity;
import com.app.root.motorkart.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

Context context;
    List<String> citylist;
int row_index=0;
    SharedPreferences sp_login;
    SharedPreferences.Editor ed_login;

    public CityListAdapter(Context context, List<String> citylist) {
        this.context = context;
        this.citylist = citylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_city,viewGroup,false);
       return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        row_index=i;
        viewHolder.tv_cityy.setText(citylist.get(i));

        viewHolder.cd_cityloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, ""+citylist.get(i), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, HomeScreeeen.class);

//                intent.putExtra("change_city",citylist.get(i));
                ed_login.putString("user_location",citylist.get(i));
                ed_login.commit();
                context.startActivity(intent);
                ((Activity)context).finishAffinity();




            }
        });
        if(row_index==0){
            viewHolder.cd_cityloc.setBackgroundColor(Color.parseColor("#fb8030"));
        }
        else
        {
            viewHolder.cd_cityloc.setBackgroundColor(Color.parseColor("#ffffff"));
        }



        viewHolder.cd_cityloc.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    v.setBackgroundColor(Color.parseColor("#F1ECEC"));
                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return citylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_cityy;
        CardView cd_cityloc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cityy=itemView.findViewById(R.id.tv_cityy);
            cd_cityloc=itemView.findViewById(R.id.cd_cityloc);

            sp_login=context.getSharedPreferences("LOGINDETAILS",Context.MODE_PRIVATE);
            ed_login=sp_login.edit();
        }
    }
}
