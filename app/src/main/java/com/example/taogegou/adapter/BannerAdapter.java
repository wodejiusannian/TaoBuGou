package com.example.taogegou.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.taogegou.R;
import com.example.taogegou.bean.JsonBean;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.List;


public class BannerAdapter extends LoopPagerAdapter {

    private int[] images = {R.mipmap.shares, R.mipmap.starting, R.mipmap.recommend};
    private List<JsonBean> mBanner;
    private Context mContext;

    public BannerAdapter(RollPagerView viewPager, List<JsonBean> banner, Context context) {
        super(viewPager);
        mBanner = banner;
        mContext = context;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        SimpleDraweeView view = new SimpleDraweeView(container.getContext());
      /* *//* Picasso.with(mContext).load(mBanner.get(position).getPhotopath()).into(view);*//*
        view.setImageResource(images[position]);*/
        String pic = mBanner.get(position).getPic();
        view.setImageURI(pic);
        /*view.setScaleType(ImageView.ScaleType.CENTER_CROP);*/
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getRealCount() {
        return mBanner.size() == 0 ? 0 : mBanner.size();
    }

}
