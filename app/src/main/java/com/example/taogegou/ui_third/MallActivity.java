package com.example.taogegou.ui_third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.adapter.HomeAdapter_vp;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.custom.MySelfDialog;
import com.example.taogegou.fragment_second.Fragment_First;
import com.example.taogegou.fragment_second.Fragment_Second;
import com.example.taogegou.fragment_second.Fragment_Third;
import com.example.taogegou.utils.MySharedPreferences;
import com.example.taogegou.utils.UtilsInternet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MallActivity extends BaseActivity implements UtilsInternet.XCallBack, MySelfDialog.OnYesClickListener {
    private TabLayout mTitle;
    private ViewPager mContent;
    private HomeAdapter_vp mAdapter;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private String userId;
    private List<String> mPrize;
    private MySelfDialog dialog;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
    }

    @Override
    public void initView() {
        mTitle = (TabLayout) findViewById(R.id.tb_mall_titles);
        mContent = (ViewPager) findViewById(R.id.vp_mall_content);
        dialog = new MySelfDialog(this);
    }

    @Override
    public void initData() {
        userId = MySharedPreferences.getUserId(this);
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
        mPrize = new ArrayList<>();
        mFragments.add(new Fragment_First());
        mFragments.add(new Fragment_Second());
        mFragments.add(new Fragment_Third());
        mTitles.add("一等奖");
        mTitles.add("二等奖");
        mTitles.add("三等奖");
    }

    @Override
    public void setData() {
        mAdapter = new HomeAdapter_vp(getSupportFragmentManager(), mFragments, mTitles);
        mTitle.setupWithViewPager(mContent);
        mContent.setAdapter(mAdapter);
    }

    @Override
    public void setListener() {
        String url = String.format(NetConfig.GET_PRIZE, userId);
        UtilsInternet.getInstance().get(url, null, this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onResponse(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray prize = obj.getJSONArray("result");
            for (int i = 0; i < prize.length(); i++) {
                JSONObject jsonObject = prize.getJSONObject(i);
                mPrize.add(jsonObject.getString("awardType"));
            }
            if (mPrize.size()==0){
                dialog.setMessage("你没有获取奖品，可以分享好友，获取奖品哦");
                dialog.setTitle("奖品详情");
                dialog.setOnYesListener("去分享",this);
                dialog.setOnNoListener("取消",null);
                dialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick() {
        Toast.makeText(this, "去分享喽", Toast.LENGTH_SHORT).show();
    }
}
