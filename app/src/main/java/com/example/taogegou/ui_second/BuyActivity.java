package com.example.taogegou.ui_second;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.config.NetConfig;

public class BuyActivity extends BaseActivity implements View.OnClickListener {
    private String url, buyUrl;
    private WebView mWebView;
    private ImageView mImageViewBack;
    private ProgressBar mLoading;
    private TextView mTitle;

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
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String GoodsID = intent.getStringExtra("GoodsID");
        String quan_id = intent.getStringExtra("quan_id");
        buyUrl = String.format(NetConfig.TRANSLATE_PATH, quan_id, GoodsID);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_buy_back:
                finish();
                break;

            default:

                break;
        }
    }
}
