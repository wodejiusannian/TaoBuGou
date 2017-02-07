package com.example.taogegou.ui_second;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;

public class WaitActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextViewTitle;
    private ImageView mImageViewBack;
    private String style;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
    }

    @Override
    public void initView() {
        mTextViewTitle = (TextView) findViewById(R.id.tv_waiting_title);
        mImageViewBack = (ImageView) findViewById(R.id.iv_waiting_back);
    }

    @Override
    public void initData() {
         style = getIntent().getStringExtra("style");
    }

    @Override
    public void setData() {
        mTextViewTitle.setText(style);
    }

    @Override
    public void setListener() {
        mImageViewBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_waiting_back:
                finish();
            break;

            default:

            break;
        }
    }
}
