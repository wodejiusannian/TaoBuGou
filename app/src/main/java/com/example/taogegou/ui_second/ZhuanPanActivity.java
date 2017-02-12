package com.example.taogegou.ui_second;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.custom.LuckyPanView;
import com.example.taogegou.custom.MySelfDialog;
import com.example.taogegou.mob.Share;
import com.example.taogegou.utils.MySharedPreferences;
import com.example.taogegou.utils.Utils;
import com.example.taogegou.utils.UtilsInternet;

import java.io.UnsupportedEncodingException;

public class ZhuanPanActivity extends BaseActivity implements View.OnClickListener, UtilsInternet.XCallBack, MySelfDialog.OnNoClickListener, MySelfDialog.OnYesClickListener {
    private LuckyPanView mLuckyPanView;
    private ImageView mStartBtn, mBack;
    private WebView mPrize;
    private TextView mCount;
    private int count = 0;
    private String userId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(ZhuanPanActivity.this, "恭喜你中了二等奖", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuan_pan);
    }

    @Override
    public void initView() {
        mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
        mStartBtn = (ImageView) findViewById(R.id.id_start_btn);
        mBack = (ImageView) findViewById(R.id.iv_zhuanpan_back);
        mPrize = (WebView) findViewById(R.id.wb_prize_info);
        mCount = (TextView) findViewById(R.id.tv_zhuanpan_count);
    }

    @Override
    public void initData() {
        userId = MySharedPreferences.getUserId(this);
        if (!TextUtils.isEmpty(userId)) {
            String url = String.format(NetConfig.GET_CURRENT_SHARE, userId);
            UtilsInternet.getInstance().get(url, null, this);
        } else {
            showLoginDialog();

        }
    }


    @Override
    public void setData() {
        mPrize.loadUrl("http://baidu.com");
        mPrize.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void setListener() {
        mStartBtn.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.id_start_btn:
                if (!TextUtils.isEmpty(userId)) {
                    startPrize();
                } else {
                    showLoginDialog();
                }
                break;
            case R.id.iv_zhuanpan_back:
                finish();
                break;
            default:

                break;
        }


    }

    //关于抽奖的判断
    private void startPrize() {
        if (!mLuckyPanView.isStart() && count > 0) {
            mStartBtn.setImageResource(R.drawable.stop);
            mLuckyPanView.luckyStart(1);
        } else if (count > -1) {
            if (!mLuckyPanView.isShouldEnd()) {
                mStartBtn.setImageResource(R.drawable.start);
                mLuckyPanView.luckyEnd();
                mHandler.sendEmptyMessageDelayed(1, 3000);
            }
        } else {
            showShareDialog();
        }
        count--;
        if (count < 0) {
            mCount.setText("0");
        } else {
            mCount.setText(count + "");
        }
    }

    private void showShareDialog() {
        MySelfDialog dialog = new MySelfDialog(this);
        dialog.setMessage("亲，您现在没有抽奖的机会呢，可以分享好友注册淘不够，获取抽奖机会");
        dialog.setOnNoListener("取消", null);
        dialog.setOnYesListener("去分享", this);
        dialog.show();
    }

    @Override
    public void onResponse(String result) {
        mCount.setText(result);
        count = Integer.parseInt(result);
    }


    @Override
    public void onClick() {
        String shareUrl = getShareUrl();
        Share.showShare(this, "我是尝试分享", "开始分享了", "这个是内容", shareUrl, shareUrl);
    }

    private void showLoginDialog() {
        MySelfDialog dialog = new MySelfDialog(this);
        dialog.setMessage("亲，要先登录，确认是否有抽奖机会哦");
        dialog.show();
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
}
