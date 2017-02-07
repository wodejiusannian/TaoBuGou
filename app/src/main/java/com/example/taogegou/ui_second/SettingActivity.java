package com.example.taogegou.ui_second;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.download.DownLoadUtils;
import com.example.taogegou.download.DownloadApk;
import com.example.taogegou.utils.MySharedPreferences;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBack;
    private LinearLayout mUpdate,mExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.iv_setting_back);
        mUpdate = (LinearLayout) findViewById(R.id.ll_setting_update);
        mExit = (LinearLayout) findViewById(R.id.ll_setting_exit);
    }

    @Override
    public void initData() {
        //1.注册下载广播接收器
        DownloadApk.registerBroadcast(this);
        //2.删除已存在的Apk
        DownloadApk.removeFile(this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        mBack.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_setting_back:
                finish();
                break;
            case R.id.ll_setting_update:
                update();
                break;
            case R.id.ll_setting_exit:
                MySharedPreferences.WriteUserInfo(this,null,null);
                MySharedPreferences.WriteUserId(this,null);
                Toast.makeText(SettingActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                sendBroad();
                break;
            default:

            break;
        }
    }

    private void update() {
        if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
            DownloadApk.downloadApk(getApplicationContext(), "http://www.huiqu.co/public/download/apk/huiqu.apk", "Hobbees更新", "Hobbees");
        } else {
            DownLoadUtils.getInstance(getApplicationContext()).skipToDownloadManager();
        }
    }

    @Override
    protected void onDestroy() {
        //4.反注册广播接收器
        DownloadApk.unregisterBroadcast(this);
        super.onDestroy();
    }


    public void sendBroad(){
        Intent intent = new Intent();
        intent.setAction("action.refreshFriend");
        sendBroadcast(intent);
    }
}
