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
import android.widget.Toast;

import com.app.root.motorkart.BookBikeService;
import com.app.root.motorkart.BookServicefromStore;
import com.app.root.motorkart.Enquiry;
import com.app.root.motorkart.PartStore_Location;
import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.SaveBookmark;
import com.app.root.motorkart.modelclass.StoreDetailModel;
import com.app.root.motorkart.modelclass.WashingPointModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ServiceCenterAdapter extends RecyclerView.Adapter<ServiceCenterAdapter.ViewHolder>
implements Filterable {
SaveBookmark saveBookmark;
    Context context;
    public List<StoreDetailModel> mItemList;
    public List<StoreDetailModel> contactListFiltered;
    String tl_name,vid_as_tid;
    boolean check=false;
    public ServiceCenterAdapter(Context context, List<StoreDetailModel> contactListFiltered,
                                String tlName, String vid_as_tid,SaveBookmark saveBookmark) {
        this.context = context;
        this.contactListFiltered = contactListFiltered;
        this.mItemList = contactListFiltered;
        this.tl_name = tlName;
        this.vid_as_tid = vid_as_tid;
        this.saveBookmark = saveBookmark;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.custom_layout_service_center,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_sv_number.setText(contactListFiltered.get(position).getStore_num());
        holder.tv_sv_name.setText(contactListFiltered.get(position).getName());
        holder.tv_sv_charges.setText("Rs. "+contactListFiltered.get(position).getInstt_charge());
        holder.tv_sv_discount.setText(contactListFiltered.get(position).getDiscount()+"%");
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

        holder.tvsv_vw_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PartStore_Location.class);
                intent.putExtra("store_id",contactListFiltered.get(position).getSid());
                intent.putExtra("store_name",contactListFiltered.get(position).getName());
                context.startActivity(intent);
            }
        });

        holder.tv_sv_inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Enquiry.class);
                intent.putExtra("vid",vid_as_tid);
                intent.putExtra("serviceinquiry_name",tl_name);
                //Toast.makeText(context, ""+storedetaillist.get(position).getName(), Toast.LENGTH_SHORT).show();
                intent.putExtra("subcatname",contactListFiltered.get(position).getName());
                intent.putExtra("subcatnameid",contactListFiltered.get(position).getSid());
                context.startActivity(intent);
            }
        });

        holder.tv_sw_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, BookServicefromStore.class);
                intent.putExtra("servicecntr_name",contactListFiltered.get(position).getName());
                context.startActivity(intent);

            }
        });

        holder.iv_wish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == false) {
                    holder.iv_wish2.setImageResource(R.drawable.filledheart);
                    check = true;
                    saveBookmark.addbookmark("1",contactListFiltered.get(position).getSid());
                }
                else {
                    holder.iv_wish2.setImageResource(R.drawable.emptyheart);
                    check=false;
                    saveBookmark.addbookmark("0",contactListFiltered.get(position).getSid());

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
                    List<StoreDetailModel> filteredList = new ArrayList<>();
                    for (StoreDetailModel row : mItemList) {

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
                contactListFiltered = (ArrayList<StoreDetailModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_star_one,iv_star_two,iv_star_three,iv_star_four,iv_star_five,iv_wish2;
        TextView tv_sv_discount,tv_sv_charges,tv_sv_number,tvsv_vw_center,tv_sv_inquire,tv_sw_book,tv_sv_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_sv_number=itemView.findViewById(R.id.tv_sv_number);
            tv_sv_charges=itemView.findViewById(R.id.tv_sv_charges);
            tv_sv_discount=itemView.findViewById(R.id.tv_sv_discount);
            iv_star_one=itemView.findViewById(R.id.ivsv_star_one);
            iv_star_two=itemView.findViewById(R.id.ivsv_star_two);
            iv_star_three=itemView.findViewById(R.id.ivsv_star_three);
            iv_star_four=itemView.findViewById(R.id.ivsv_star_four);
            iv_star_five=itemView.findViewById(R.id.ivsv_star_five);
            tvsv_vw_center=itemView.findViewById(R.id.tvsv_vw_center);
            tv_sv_inquire=itemView.findViewById(R.id.tv_sv_inquire);
            tv_sw_book=itemView.findViewById(R.id.tv_sw_book);
            tv_sv_name=itemView.findViewById(R.id.tv_sv_name);
            iv_wish2=itemView.findViewById(R.id.iv_wish2);
        }
    }
}