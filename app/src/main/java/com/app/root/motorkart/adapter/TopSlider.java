package com.app.root.motorkart.adapter;

import android.content.Context;
        import android.content.Intent;
        import android.os.Parcelable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;


import com.app.root.motorkart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public class TopSlider extends PagerAdapter {

    private List<String> IMAGES;
    private Context context;


    public TopSlider(Context context, List<String> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
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
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.sliding_images, view, false);

        assert imageLayout != null;
        final ImageView top_slider_image = (ImageView) imageLayout
                .findViewById(R.id.slider_image);

        Picasso.with(context).load("http://demo.digitalsolutionsplanet.com/motorkart/uploads/banner/"+
                IMAGES.get(position)).into(top_slider_image);
      //  top_slider_image.setImageResource(R.drawable.slider_image);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}