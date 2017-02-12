package com.example.taogegou.ui_third;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.taogegou.R;
import com.example.taogegou.adapter.LogisticAdapter;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.bean.Logistic;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.custom.MySelfDialog;
import com.example.taogegou.mob.Share;
import com.example.taogegou.ui_second.LoginActivity;
import com.example.taogegou.utils.ActivityUtils;
import com.example.taogegou.utils.MySharedPreferences;
import com.example.taogegou.utils.Utils;
import com.example.taogegou.utils.UtilsInternet;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LogisticActivity extends BaseActivity implements UtilsInternet.XCallBack, MySelfDialog.OnYesClickListener, View.OnClickListener {
    private ListView mShow;
    private LogisticAdapter adapter;
    private List<Logistic.ResultBean> mData;
    private Button mLogin;
    private static final String TAG = "LogisticActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistic);
    }

    @Override
    public void initView() {
        mShow = (ListView) findViewById(R.id.lv_logistic_show);
        mLogin = (Button) findViewById(R.id.btn_logistic_login);
    }

    @Override
    public void initData() {
        mData = new ArrayList<>();
        adapter = new LogisticAdapter(mData, this);
        loadMore();
    }

    private void loadMore() {
        String userId = MySharedPreferences.getUserId(this);
        if (!TextUtils.isEmpty(userId)) {
            String url = String.format(NetConfig.GET_LOGISTIC, userId);
            UtilsInternet.getInstance().get(url, null, this);
        } else {
            mLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setData() {
        mShow.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        mLogin.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onResponse(String result) {
        Gson gson = new Gson();
        Logistic logistic = gson.fromJson(result, Logistic.class);
        List<Logistic.ResultBean> bean = logistic.getResult();
        mData.addAll(bean);
        if (mData.size() == 0) {
            showShareDialog();
        }
        adapter.notifyDataSetChanged();
    }

    private void showShareDialog() {
        MySelfDialog dialog = new MySelfDialog(this);
        dialog.setMessage("亲，您现在没有抽奖的机会呢，可以分享好友注册淘不够，获取抽奖机会");
        dialog.setOnNoListener("取消", null);
        dialog.setOnYesListener("去分享", this);
        dialog.show();
    }

    @Override
    public void onClick() {
        String shareUrl = getShareUrl();
        Share.showShare(this, "我是尝试分享", "开始分享了", "这个是内容", shareUrl, shareUrl);
    }

    private String getShareUrl() {
        String userId = MySharedPreferences.getUserId(this);
        String sceneUrl = Utils.HTTP_SERVER + "/test/" + Utils.APPKEY;
        String params = "";
        try {
            byte[] values = Base64.encode(userId.getBytes("UTF-8"), Base64.URL_SAFE);
            params = "jsonParam=" + new String(values, "utf-8");
            return sceneUrl + "?" + params;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logistic_login:
                ActivityUtils.switchTo(this, LoginActivity.class);
                finish();
                break;
        }
    }
}
