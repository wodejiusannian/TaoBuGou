package com.example.taogegou.ui_third;

import android.os.Bundle;
import android.view.View;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;

public class ConnectionActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
    }

    @Override
    public void initView() {
        this.findViewById(R.id.iv_connection_back).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_connection_back:
                finish();
                break;

            default:

            break;
        }
    }
}
