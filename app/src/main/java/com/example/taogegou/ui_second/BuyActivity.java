package com.example.taogegou.ui_second;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.mob.Share;

public class BuyActivity extends BaseActivity implements View.OnClickListener {
    private String imagePath, buyUrl, title, content;
    private WebView mWebView;
    private ImageView mImageViewBack, mShareApp;
    private ProgressBar mLoading;
    private TextView mTitle;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
    }

    @Override
    public void initView() {
        mWebView = (WebView) findViewById(R.id.wv_buy_show);
        mImageViewBack = (ImageView) findViewById(R.id.iv_buy_back);
        mLoading = (ProgressBar) findViewById(R.id.pb_buy_loading);
        mTitle = (TextView) findViewById(R.id.tv_buy_title);
        mShareApp = (ImageView) findViewById(R.id.iv_buy_share_app);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String GoodsID = intent.getStringExtra("GoodsID");
        String quan_id = intent.getStringExtra("quan_id");
        imagePath = intent.getStringExtra("imagePath");
        title = intent.getStringExtra("title");
        String base_price = intent.getStringExtra("base_price");
        String after_price = intent.getStringExtra("after_price");
        buyUrl = String.format(NetConfig.TRANSLATE_PATH, quan_id, GoodsID);
        content = "我正在淘不够购买此商品，原价¥" + base_price + "券后¥" + after_price + "优惠券点击领取";
    }

    @Override
    public void setData() {
        loadindUrl(buyUrl);
    }

    private void loadindUrl(String url) {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //支持对网页缩放
        webSettings.setSupportZoom(true);
        //默认缩放模式
        mWebView.setInitialScale(100);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        WebChromeClient wvcc = new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    mLoading.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mLoading.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mLoading.setProgress(newProgress);//设置进度值
                }

            }

        };
        mWebView.setWebChromeClient(wvcc);
        if (url != null) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void setListener() {
        mImageViewBack.setOnClickListener(this);
        mShareApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_buy_back:
                finish();
                break;
            case R.id.iv_buy_share_app:
                getPopupWindow();
                popupWindow.showAsDropDown(v);
                break;
            case R.id.ll_show_pop_share_app:
                Share.showShareApp(this, "淘不够", "淘不够包含近十万款天猫淘宝优惠商品，半价销售", "淘不够包含近十万款天猫淘宝优惠商品，半价销售", "https://fir.im/rpd4", "https://fir.im/rpd4");
                popupWindow.dismiss();
                break;
            case R.id.ll_show_pop_share_shop:
                Share.showShareShop(this, title, content, content, buyUrl, buyUrl, imagePath);
                popupWindow.dismiss();
                break;
            default:

                break;
        }
    }


    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow() {
        View view = getLayoutInflater().inflate(R.layout.show_pop, null);
        View share_shop = view.findViewById(R.id.ll_show_pop_share_shop);
        View share_app = view.findViewById(R.id.ll_show_pop_share_app);
        share_app.setOnClickListener(this);
        share_shop.setOnClickListener(this);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }
}
