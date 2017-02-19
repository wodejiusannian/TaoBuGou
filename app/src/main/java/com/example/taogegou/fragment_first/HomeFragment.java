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
import com.example.taogegou.fragment_second.Fragment_bag;
import com.example.taogegou.fragment_second.Fragment_car;
import com.example.taogegou.fragment_second.Fragment_child_dress;
import com.example.taogegou.fragment_second.Fragment_huazhuangpin;
import com.example.taogegou.fragment_second.Fragment_jujiayongpin;
import com.example.taogegou.fragment_second.Fragment_live;
import com.example.taogegou.fragment_second.Fragment_man_dress;
import com.example.taogegou.fragment_second.Fragment_neiyi;
import com.example.taogegou.fragment_second.Fragment_tea;
import com.example.taogegou.fragment_second.Fragment_women_dress;
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
    private String[] strings = {"输入您想购买的宝贝（例如：女装）", "化妆品", "零食大礼包", "女装", "男装", "女连衣裙"};
    private int i = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            i++;
            if (i == strings.length) {
                i = 0;
            }
            mJumpSearch.setHint(strings[i]);
            mHandler.sendEmptyMessageDelayed(1, 10000);
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
        for (int i = 0; i < 10; i++) {
            switch (i) {
                case 0:
                    mData.add(new Fragment_women_dress());
                    mTitles.add("女装");
                    break;
                case 1:
                    mData.add(new Fragment_man_dress());
                    mTitles.add("男装");
                    break;
                case 2:
                    mData.add(new Fragment_child_dress());
                    mTitles.add("童装");
                    break;
                case 3:
                    mData.add(new Fragment_huazhuangpin());
                    mTitles.add("化妆品");
                    break;
                case 4:
                    mData.add(new Fragment_jujiayongpin());
                    mTitles.add("居家用品");
                    break;
                case 5:
                    mData.add(new Fragment_bag());
                    mTitles.add("鞋 | 包");
                    break;
                case 6:
                    mData.add(new Fragment_tea());
                    mTitles.add("茶 | 养生 | 酒水");
                    break;
                case 7:
                    mData.add(new Fragment_car());
                    mTitles.add("汽车用品 | 文具");
                    break;
                case 8:
                    mData.add(new Fragment_live());
                    mTitles.add("生活用品 | 电子商品");
                    break;
                case 9:
                    mData.add(new Fragment_neiyi());
                    mTitles.add("内衣");
                    break;
                default:
                    break;
            }

        }
        mAdapter = new HomeAdapter_vp(getFragmentManager(), mData, mTitles);
        mHandler.sendEmptyMessageDelayed(1, 10000);
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
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        Map<String, Object> map = new HashMap<>();
        String title = mJumpSearch.getHint().toString();
        map.put("title", title);
        ActivityUtils.switchTo(getActivity(), SearchActivity.class, map);
    }
}
