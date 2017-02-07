package com.example.taogegou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class HomeAdapter_vp extends FragmentPagerAdapter{
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public HomeAdapter_vp(FragmentManager fm,List<Fragment> fragments,List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }




    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size()==0?0:mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
