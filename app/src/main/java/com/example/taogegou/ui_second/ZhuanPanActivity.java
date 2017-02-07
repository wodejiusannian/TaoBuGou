package com.example.taogegou.ui_second;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.custom.LuckyPanView;

public class ZhuanPanActivity extends BaseActivity implements View.OnClickListener {
    private LuckyPanView mLuckyPanView;
    private ImageView mStartBtn,mBack;
    private Handler mHandler = new Handler(){
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
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {
    }

    @Override
    public void setListener() {
        mStartBtn.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.id_start_btn:
                if (!mLuckyPanView.isStart())
                {
                    mStartBtn.setImageResource(R.drawable.stop);
                    mLuckyPanView.luckyStart(1);
                } else
                {
                    if (!mLuckyPanView.isShouldEnd())

                    {
                        mStartBtn.setImageResource(R.drawable.start);
                        mLuckyPanView.luckyEnd();
                        mHandler.sendEmptyMessageDelayed(1,3000);
                    }
                }
            break;
            case R.id.iv_zhuanpan_back:
                finish();
                break;
            default:

            break;
        }


    }
}
