package com.example.taogegou.ui_third;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;

public class UseActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBack;
    private WebView mShow;
    private ProgressBar mLoading;
    private TextView mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use);
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.iv_use_back);
        mShow = (WebView) findViewById(R.id.wv_use_show);
        mLoading = (ProgressBar) findViewById(R.id.pb_use_loading);
        mTitle = (TextView) findViewById(R.id.tv_buy_title);
    }

    @Override
    public void initData() {
        loadindUrl("file:///D:/html.html");
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_use_back:
                finish();
                break;

            default:

            break;
        }
    }

    private void loadindUrl(String url) {

        WebSettings seting=mShow.getSettings();
        seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle.setText(title);
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if(newProgress==100){
                    mLoading.setVisibility(View.GONE);//加载完网页进度条消失
                }else{
                    mLoading.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mLoading.setProgress(newProgress);//设置进度值
                }

            }
        };
        mShow.setWebChromeClient(wvcc);
        mShow.loadUrl(url);
    }
}
