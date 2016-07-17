package com.toshevski.android.reklama5.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> links;
    private TextView counter;

    public ImageSliderAdapter(Context context, ArrayList<String> links, TextView counter) {
        this.mContext = context;
        this.links = links;
        this.counter = counter;
    }

    @Override
    public int getCount() {
        return links.size();
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(mContext).load(links.get(i)).into(mImageView);
        container.addView(mImageView, 0);
        counter.setText(" " + (i + 1) + " / " + links.size() + " ");
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((ImageView) obj);
    }
}
