package com.dueeeke.videoplayer.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by NING on 2018/8/10.
 */

public class VideoPagerAdapter extends PagerAdapter {

    private static final String TAG = "DouYinAdapter";

    private List<View> mViews;

    public VideoPagerAdapter(List<View> views) {
        this.mViews = views;
    }


    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }
}
