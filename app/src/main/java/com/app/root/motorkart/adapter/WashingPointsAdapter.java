package com.app.root.motorkart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.root.motorkart.BookServicefromStore;
import com.app.root.motorkart.Enquiry;
import com.app.root.motorkart.PartStore_Location;
import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.SaveBookmark;
import com.app.root.motorkart.modelclass.StoreDetailModel;
import com.app.root.motorkart.modelclass.StoreModel;
import com.app.root.motorkart.modelclass.WashingPointModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class WashingPointsAdapter extends RecyclerView.Adapter<WashingPointsAdapter.ViewHolder>
implements Filterable {

    Context context;
    public List<WashingPointModel> mItemList;
    public List<WashingPointModel> contactListFiltered;
    String vid_as_tid,tl_name;
    boolean check=false;
    SaveBookmark saveBookmark;
    public WashingPointsAdapter(Context context, List<WashingPointModel> mItemList,
                                String vid_as_tid,String tl_name,SaveBookmark saveBookmark) {
        this.context = context;
        this.mItemList = mItemList;
        this.contactListFiltered = mItemList;
        this.vid_as_tid = vid_as_tid;
        this.tl_name = tl_name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.custom_layout_washingpts,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_wt_job.setText(contactListFiltered.get(position).getStore_num());
        holder.tv_wt_name.setText(contactListFiltered.get(position).getName());
        holder.tv_wt_amount.setText("Rs. "+contactListFiltered.get(position).getInstt_charge());
        holder.tv_wt_date.setText(contactListFiltered.get(position).getCreated_on());
        if(contactListFiltered.get(position).getRatings().equals("1")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.border_star);
            holder.iv_star_three.setImageResource(R.drawable.border_star);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if(contactListFiltered.get(position).getRatings().equals("2")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.border_star);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if(contactListFiltered.get(position).getRatings().equals("3")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if(contactListFiltered.get(position).getRatings().equals("4")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.star_solid);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if(contactListFiltered.get(position).getRatings().equals("5")){
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.star_solid);
            holder.iv_star_five.setImageResource(R.drawable.star_solid);
        }

        holder.tvwt_vw_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PartStore_Location.class);
                intent.putExtra("store_id",contactListFiltered.get(position).getSid());
                intent.putExtra("store_name",contactListFiltered.get(position).getName());
                context.startActivity(intent);
            }
        });

        holder.tvwt_inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Enquiry.class);
                intent.putExtra("vid",vid_as_tid);
                intent.putExtra("serviceinquiry_name",tl_name);
                intent.putExtra("subcatname",contactListFiltered.get(position).getName());
                intent.putExtra("subcatnameid",contactListFiltered.get(position).getSid());
                context.startActivity(intent);
            }
        });

        holder.tvwt_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, BookServicefromStore.class);
                intent.putExtra("servicecntr_name",contactListFiltered.get(position).getName());
                context.startActivity(intent);

            }
        });
        holder.iv_wish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == false) {
                    holder.iv_wish1.setImageResource(R.drawable.filledheart);
                    check = true;
                    saveBookmark.addbookmark("1", contactListFiltered.get(position).getSid());

                }
                else {
                    holder.iv_wish1.setImageResource(R.drawable.emptyheart);
                    check=false;
                    saveBookmark.addbookmark("0", contactListFiltered.get(position).getSid());

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
                    List<WashingPointModel> filteredList = new ArrayList<>();
                    for (WashingPointModel row : mItemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getStore_num().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getStore_num().toLowerCase().contains(charString.toUpperCase())) {
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
                contactListFiltered = (ArrayList<WashingPointModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_star_one,iv_star_two,iv_star_three,iv_star_four,iv_star_five,iv_wish1;
        TextView tv_wt_job,tv_wt_date,tv_wt_amount,tvwt_vw_center,tvwt_inquire,tv_wt_name,tvwt_book;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_wt_job=itemView.findViewById(R.id.tv_wt_job);
            tvwt_book=itemView.findViewById(R.id.tvwt_book);
            tv_wt_date=itemView.findViewById(R.id.tv_wt_date);
            tv_wt_amount=itemView.findViewById(R.id.tv_wt_amount);
            iv_star_one=itemView.findViewById(R.id.ivwt_star_one);
            iv_star_two=itemView.findViewById(R.id.ivwt_star_two);
            iv_star_three=itemView.findViewById(R.id.ivwt_star_three);
            iv_star_four=itemView.findViewById(R.id.ivwt_star_four);
            iv_star_five=itemView.findViewById(R.id.ivwt_star_five);
            tvwt_vw_center=itemView.findViewById(R.id.tvwt_vw_center);
            tvwt_inquire=itemView.findViewById(R.id.tvwt_inquire);
            tv_wt_name=itemView.findViewById(R.id.tv_wt_name);
            iv_wish1=itemView.findViewById(R.id.iv_wish1);
        }
    }
}