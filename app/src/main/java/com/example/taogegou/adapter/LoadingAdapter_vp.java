package com.example.taogegou.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class LoadingAdapter_vp extends PagerAdapter{

    private List<ImageView> mImageViews;

    public LoadingAdapter_vp(List<ImageView> imageViews){
        mImageViews = imageViews;
    }
    @Override
    public int getCount() {
        return mImageViews==null?0:mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mImageViews.get(position));
        return mImageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViews.get(position));
    }
}
