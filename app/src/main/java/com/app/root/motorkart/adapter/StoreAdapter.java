package com.app.root.motorkart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.root.motorkart.PartStore;
import com.app.root.motorkart.R;
import com.app.root.motorkart.ServiceCentre;
import com.app.root.motorkart.WashingPoints;
import com.app.root.motorkart.modelclass.StoreModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> implements Filterable {
Context context;
    public List<StoreModel> mItemList;
    public List<StoreModel> contactListFiltered;
    public StoreAdapter(Context context,List<StoreModel> itemList) {
        this.context = context;
        mItemList = itemList;
        contactListFiltered = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_store,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
holder.tv_st_name.setText(contactListFiltered.get(position).getName());
        Picasso.with(context).load("http://demo.digitalsolutionsplanet.com/motorkart/uploads/testimonials/"+contactListFiltered.get(position).getAvtar()).into(holder.iv_st_image);
    holder.ll_store.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(contactListFiltered.get(position).getName().equals("Bike Parts Store")){
                Intent intent=new Intent(context, PartStore.class);
                intent.putExtra("tid",contactListFiltered.get(position).getTid());
                intent.putExtra("name",contactListFiltered.get(position).getName());
                context.startActivity(intent);
            }
            else if(contactListFiltered.get(position).getName().equals("Washing Points")){
                Intent intent=new Intent(context, WashingPoints.class);
                intent.putExtra("tid",contactListFiltered.get(position).getTid());
                intent.putExtra("name",contactListFiltered.get(position).getName());
                context.startActivity(intent);
            }
            else {
                Intent intent=new Intent(context, ServiceCentre.class);
                intent.putExtra("tid",contactListFiltered.get(position).getTid());
                intent.putExtra("name",contactListFiltered.get(position).getName());
                context.startActivity(intent);
            }

        }
    });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = mItemList;
                } else {
                    List<StoreModel> filteredList = new ArrayList<>();
                    for (StoreModel row : mItemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getName().toLowerCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<StoreModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_store;
        ImageView iv_st_image;
        TextView tv_st_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_store=itemView.findViewById(R.id.ll_store);
            iv_st_image=itemView.findViewById(R.id.iv_st_image);
            tv_st_name=itemView.findViewById(R.id.tv_st_name);
        }
    }
}
