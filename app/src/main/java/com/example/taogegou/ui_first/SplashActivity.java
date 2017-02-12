package com.example.taogegou.ui_first;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.download.DownloadApk;
import com.example.taogegou.utils.ActivityUtils;
import com.example.taogegou.utils.MySharedPreferences;
import com.example.taogegou.utils.UtilsInternet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener, UtilsInternet.XCallBack {
    private int isFirst = 0;
    private TextView mTextViewJump;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (isFirst) {
                case 0:
                    ActivityUtils.switchTo(SplashActivity.this, LoadingActivity.class);
                    break;
                case 1:
                    ActivityUtils.switchTo(SplashActivity.this, MainActivity.class);
                    break;
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView();
        initData();
        setData();
        setListener();
    }

    public void initView() {
        mTextViewJump = (TextView) findViewById(R.id.tv_splash_jump);
        //1.注册下载广播接收器
        DownloadApk.registerBroadcast(this);
        //2.删除已存在的Apk
        DownloadApk.removeFile(this);
    }

    public void initData() {
        String url = String.format(NetConfig.SHOP_PATH, 1);
        UtilsInternet.getInstance().get(url, null, this);
        isFirst = MySharedPreferences.IsFirstLoading(this);
        MySharedPreferences.WriteLoading(this);
        //isFresh();
    }

    public void setData() {
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    public void setListener() {
        mTextViewJump.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_splash_jump:
                switch (isFirst) {
                    case 0:
                        ActivityUtils.switchTo(SplashActivity.this, LoadingActivity.class);
                        break;
                    case 1:
                        ActivityUtils.switchTo(SplashActivity.this, MainActivity.class);
                        break;
                }
                finish();
                break;
        }
    }

    @Override
    public void onResponse(String result) {
        save(result);
    }

    public void save(String result) {
        try {
            FileOutputStream outStream = this.openFileOutput("taogegou.txt", Context.MODE_PRIVATE);
            outStream.write(result.getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }

    }

    /*private void isFresh() {
        final SystemParams instance = SystemParams.getInstance();
        final boolean isFresh = instance.getBoolean("isFresh");
        RequestParams params = new RequestParams(NetConfig.IS_FRESH_PATH);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String serverVersion = obj.getString("serverVersion");
                    final String url = obj.getString("url");
                    String msg = obj.getString("msg");
                    String lastForce = obj.getString("lastForce");
                    if (!TextUtils.equals(serverVersion, getVersionName())) {
                        if (TextUtils.equals("Y", lastForce) || TextUtils.equals("y", lastForce)) {
                            if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
                                DownloadApk.downloadApk(getApplicationContext(), url, "慧美衣橱正在更新...", "hobbees");
                            } else {
                                DownLoadUtils.getInstance(getApplicationContext()).skipToDownloadManager();
                            }
                        } else {
                            if (!isFresh) {
                                MySelfDialog mDialog = new MySelfDialog(SplashActivity.this);
                                mDialog.setTitle("是否更新");
                                mDialog.setMessage(msg);
                                mDialog.setOnYesListener("确定", new MySelfDialog.OnYesClickListener() {
                                    @Override
                                    public void onClick() {
                                        if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
                                            DownloadApk.downloadApk(getApplicationContext(), url, "慧美衣橱正在更新...", "huimeiApk");
                                        } else {
                                            DownLoadUtils.getInstance(getApplicationContext()).skipToDownloadManager();
                                        }
                                    }
                                });
                                mDialog.setOnNoListener("取消", new MySelfDialog.OnNoClickListener() {
                                    @Override
                                    public void onClick() {
                                        instance.setBoolean("isFresh", true);
                                    }
                                });
                                mDialog.show();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
