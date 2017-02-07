package com.example.taogegou.ui_first;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.taogegou.R;
import com.example.taogegou.adapter.LoadingAdapter_vp;
import com.example.taogegou.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private List<ImageView> mImages;
    private int[] images = {R.mipmap.buy1,R.mipmap.buy2,R.mipmap.buy3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView();
        initData();
        setData();
        setListner();
    }

    private void setListner() {
        mImages.get(2).setOnClickListener(this);
    }

    private void setData() {
        LoadingAdapter_vp adapter = new LoadingAdapter_vp(mImages);
        mViewPager.setAdapter(adapter);
    }

    private void initData() {
        mImages = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
          ImageView image = new ImageView(this);
          image.setImageResource(images[i]);
          mImages.add(image);
        }
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_loading_guide);
    }


    @Override
    public void onClick(View v) {
        ActivityUtils.switchTo(this,MainActivity.class);
        finish();
    }
}
