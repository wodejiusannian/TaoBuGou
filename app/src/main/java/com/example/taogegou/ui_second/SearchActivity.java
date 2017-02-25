package com.example.taogegou.ui_second;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.custom.MyRelativeLayout;
import com.example.taogegou.ui_third.ResultActivity;
import com.example.taogegou.utils.ActivityUtils;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {
    private ImageView mBack;
    private String title;
    private EditText mWrite;
    private ImageView mGo;
    private MyRelativeLayout mHot;
    private String[] arr = {"新款女装", "女连衣裙", "女鞋", "女外套",
            "男裤", "零食", "男外套", "化妆品", "理疗盐袋", "男春装", "男鞋"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.iv_search_back);
        mWrite = (EditText) findViewById(R.id.et_search_write);
        mGo = (ImageView) findViewById(R.id.iv_search_go);
        mHot = (MyRelativeLayout) findViewById(R.id.mr_search_content);
    }

    @Override
    public void initData() {
        title = getIntent().getStringExtra("title");
        mWrite.setHint(title);
    }

    @Override
    public void setData() {
        for (int i = 0; i < arr.length; i++) {
            TextView mTv = new TextView(this);
            mTv.setText(arr[i]);
            mTv.setTag(i);
            mTv.setBackgroundResource(R.drawable.mback);
            mTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    goSearch(arr[tag]);
                }
            });
            mHot.addView(mTv);
        }
    }

    @Override
    public void setListener() {
        mBack.setOnClickListener(this);
        mGo.setOnClickListener(this);
        mWrite.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.iv_search_go:
                searchGo();
                break;
            default:

                break;
        }
    }

    private void searchGo() {
        String write = mWrite.getText().toString().trim();
        String hintWrite = mWrite.getHint().toString().trim();
        if (!TextUtils.isEmpty(write)) {
            //搜索的方法
            goSearch(write);
        } else {
            goSearch(hintWrite);
        }
    }

    //搜索的方法
    private void goSearch(String search) {
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        ActivityUtils.switchTo(this, ResultActivity.class, map);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            searchGo();
        }
        return true;
    }
}
