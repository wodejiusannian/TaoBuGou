package com.example.taogegou.fragment_first;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.taogegou.R;
import com.example.taogegou.adapter.HomeAdapter_vp;
import com.example.taogegou.base.BaseFragment;
import com.example.taogegou.fragment_second.Fragment_Baby_Cosmetic;
import com.example.taogegou.fragment_second.Fragment_Clothing_Acc;
import com.example.taogegou.fragment_second.Fragment_Electrical_Product;
import com.example.taogegou.fragment_second.Fragment_Food_Live;
import com.example.taogegou.ui_second.SearchActivity;
import com.example.taogegou.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mData;
    private List<String> mTitles;
    private HomeAdapter_vp mAdapter;
    private Button mJumpSearch;
    private ImageView mSearch;
    private String[] strings = {"输入您想购买的宝贝（例如：女装）","化妆品","零食大礼包","女装","男装","女连衣裙"};
    private int i = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            i++;
            if (i==strings.length){
                i = 0;
            }
            mJumpSearch.setHint(strings[i]);
            mHandler.sendEmptyMessageDelayed(1,10000);
        }
    };
    @Override
    protected View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        getChildView(view);
        return view;
    }

    private void getChildView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tb_home_titles);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_home_content);
        mJumpSearch = (Button) view.findViewById(R.id.btn_home_search);
        mSearch = (ImageView) view.findViewById(R.id.iv_home_search);
    }

    @Override
    protected void initData() {
        super.initData();
        mData = new ArrayList<>();
        mTitles = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            if(i==0) {
                mData.add(new Fragment_Clothing_Acc());
                mTitles.add("衣服 | 配饰");
            }else if(i==1) {
                mData.add(new Fragment_Baby_Cosmetic());
                mTitles.add("母婴 | 美肤");
            }else if(i==2) {
                mData.add(new Fragment_Food_Live());
                mTitles.add("美食 | 居家");
            }else if(i==3) {
                mData.add(new Fragment_Electrical_Product());
                mTitles.add("文体 | 百货");
            }
        }
        mAdapter = new HomeAdapter_vp(getFragmentManager(),mData,mTitles);
        mHandler.sendEmptyMessageDelayed(1,10000);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mJumpSearch.setOnClickListener(this);
        mSearch.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        super.setData();
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_home_search:
                jump();
                break;
            case R.id.iv_home_search:
                jump();
                break;
            default:

            break;
        }
    }

    private void jump() {
        Map<String,Object> map = new HashMap<>();
        String title = mJumpSearch.getHint().toString();
        map.put("title",title);
        ActivityUtils.switchTo(getActivity(),SearchActivity.class,map);
    }
}
