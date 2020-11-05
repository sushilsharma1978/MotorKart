package com.app.root.motorkart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class LocationSpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> locationlist,locationlistid;
    public LocationSpinnerAdapter(Context context, List<String> locationlist, List<String> locationlistid) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
