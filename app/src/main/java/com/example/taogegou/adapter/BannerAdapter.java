package com.example.taogegou.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.taogegou.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;


public class BannerAdapter extends LoopPagerAdapter {

    private int[] images = {R.mipmap.shares,R.mipmap.starting,R.mipmap.recommend};
   /* private List<Banner.ListBean> mBanner;*/
    private Context mContext;
    public BannerAdapter(RollPagerView viewPager/*, List<Banner.ListBean> banner,*/, Context context)
    {
        super(viewPager);
       /* mBanner = banner;*/
        mContext = context;
    }

    @Override
    public View getView(ViewGroup container, int position)
    {
        ImageView view = new ImageView(container.getContext());
       /* Picasso.with(mContext).load(mBanner.get(position).getPhotopath()).into(view);*/
        view.setImageResource(images[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getRealCount()
    {
        return 3;
    }

}
