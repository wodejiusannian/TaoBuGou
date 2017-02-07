package com.example.taogegou.ui_third;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBack;
    private EditText mWrite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.iv_feedback_back);
        mWrite = (EditText) findViewById(R.id.et_feedback_write);
    }

    @Override
    public void initData() {

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
            case R.id.iv_feedback_back:
                finish();
                break;
            case R.id.et_feedback_write:
                String write = mWrite.getText().toString().trim();
                if (!TextUtils.isEmpty(write)){

                }else {
                    Toast.makeText(FeedBackActivity.this, "请输入反馈内容哦", Toast.LENGTH_SHORT).show();
                }
                break;
            default:

            break;
        }
    }
}
