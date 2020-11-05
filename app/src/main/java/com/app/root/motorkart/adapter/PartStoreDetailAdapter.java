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

import com.app.root.motorkart.Enquiry;
import com.app.root.motorkart.MapsActivity;
import com.app.root.motorkart.PartStore_Location;
import com.app.root.motorkart.R;
import com.app.root.motorkart.interfaces.SaveBookmark;
import com.app.root.motorkart.modelclass.StoreDetailModel;
import com.app.root.motorkart.modelclass.StoreModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PartStoreDetailAdapter extends RecyclerView.Adapter<PartStoreDetailAdapter.ViewHolder>
        implements Filterable {

    Context context;
    SaveBookmark saveBookmark;
    String vid_as_tid, tl_name;
    boolean check = false;
    public List<StoreDetailModel> mItemList;
    public List<StoreDetailModel> contactListFiltered;

    public PartStoreDetailAdapter(Context context, List<StoreDetailModel> mItemList,
                                  String vid_as_tid, String tl_name, SaveBookmark saveBookmark) {
        this.context = context;
        this.mItemList = mItemList;
        this.contactListFiltered = mItemList;
        this.vid_as_tid = vid_as_tid;
        this.tl_name = tl_name;
        this.saveBookmark = saveBookmark;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_layout_storedetail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_st_number.setText(contactListFiltered.get(position).getStore_num());
        holder.tv_st_inst.setText("Rs. " + contactListFiltered.get(position).getInstt_charge());
        holder.tv_st_dsct.setText(contactListFiltered.get(position).getDiscount() + "%");
        holder.tv_st_name.setText(contactListFiltered.get(position).getName());
        if (contactListFiltered.get(position).getRatings().equals("1")) {
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.border_star);
            holder.iv_star_three.setImageResource(R.drawable.border_star);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if (contactListFiltered.get(position).getRatings().equals("2")) {
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.border_star);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if (contactListFiltered.get(position).getRatings().equals("3")) {
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.border_star);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if (contactListFiltered.get(position).getRatings().equals("4")) {
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.star_solid);
            holder.iv_star_five.setImageResource(R.drawable.border_star);
        }
        if (contactListFiltered.get(position).getRatings().equals("5")) {
            holder.iv_star_one.setImageResource(R.drawable.star_solid);
            holder.iv_star_two.setImageResource(R.drawable.star_solid);
            holder.iv_star_three.setImageResource(R.drawable.star_solid);
            holder.iv_star_four.setImageResource(R.drawable.star_solid);
            holder.iv_star_five.setImageResource(R.drawable.star_solid);
        }

        holder.tv_vw_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PartStore_Location.class);
                intent.putExtra("store_id", contactListFiltered.get(position).getSid());
                intent.putExtra("store_name", contactListFiltered.get(position).getName());

                context.startActivity(intent);
            }
        });

        holder.tv_vw_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Enquiry.class);
                intent.putExtra("vid", vid_as_tid);
                intent.putExtra("serviceinquiry_name", tl_name);
                intent.putExtra("subcatname", contactListFiltered.get(position).getName());
                intent.putExtra("subcatnameid", contactListFiltered.get(position).getSid());
                context.startActivity(intent);
            }
        });
        holder.iv_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == false) {
                    holder.iv_wish.setImageResource(R.drawable.filledheart);
                    check = true;
                    saveBookmark.addbookmark("1", contactListFiltered.get(position).getSid());

                } else {
                    holder.iv_wish.setImageResource(R.drawable.emptyheart);
                    check = false;
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_star_one, iv_star_two, iv_star_three, iv_star_four, iv_star_five, iv_wish;
        TextView tv_st_number, tv_st_inst, tv_st_dsct, tv_vw_enquiry, tv_vw_store, tv_st_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_st_number = itemView.findViewById(R.id.tv_st_number);
            tv_st_inst = itemView.findViewById(R.id.tv_st_inst);
            tv_st_dsct = itemView.findViewById(R.id.tv_st_dsct);
            iv_star_one = itemView.findViewById(R.id.iv_star_one);
            iv_star_two = itemView.findViewById(R.id.iv_star_two);
            iv_star_three = itemView.findViewById(R.id.iv_star_three);
            iv_star_four = itemView.findViewById(R.id.iv_star_four);
            iv_star_five = itemView.findViewById(R.id.iv_star_five);
            tv_vw_enquiry = itemView.findViewById(R.id.tv_vw_enquiry);
            tv_vw_store = itemView.findViewById(R.id.tv_vw_store);
            tv_st_name = itemView.findViewById(R.id.tv_st_name);
            iv_wish = itemView.findViewById(R.id.iv_wish);
        }
    }
}