package com.app.root.motorkart.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.root.motorkart.R;
import com.app.root.motorkart.modelclass.StoreImages;
import com.app.root.motorkart.modelclass.VehicleModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

public class LocationStoreImagesAdapter extends PagerAdapter {
Context context;
List<StoreImages> storeimageslist;
    public LocationStoreImagesAdapter(Context context, List<StoreImages> storeimageslist) {
        this.context = context;
        this.storeimageslist = storeimageslist;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return storeimageslist.size();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.custom_layout_storeimages, view, false);

        assert imageLayout != null;
        final ImageView top_slider_image = (ImageView) imageLayout
                .findViewById(R.id.iv_storeimage);

        Picasso.with(context).
                load("http://demo.digitalsolutionsplanet.com/motorkart/uploads/gallary/"+
                        storeimageslist.get(position).getImage()).into(top_slider_image);

        //  top_slider_image.setImageResource(R.drawable.slider_image);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
