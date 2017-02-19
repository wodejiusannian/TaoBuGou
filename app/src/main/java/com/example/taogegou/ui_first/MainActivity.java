package com.example.taogegou.ui_first;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.custom.MySelfDialog;
import com.example.taogegou.download.DownLoadUtils;
import com.example.taogegou.download.DownloadApk;
import com.example.taogegou.fragment_first.HomeFragment;
import com.example.taogegou.fragment_first.MineFragment;
import com.example.taogegou.inter.ReFreshUserInfo;
import com.example.taogegou.utils.MySharedPreferences;
import com.example.taogegou.utils.UtilsInternet;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallListener;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;

public class MainActivity extends BaseActivity implements /*RadioGroup.OnCheckedChangeListener,*/ AppInstallListener, UtilsInternet.XCallBack, MySelfDialog.OnYesClickListener {

    private static boolean isExit = false;
    /*private RadioGroup mRadioGroup;*/
    private ReFreshUserInfo mInfo;
    private HomeFragment mHome;
    private MineFragment mMine;
    private UtilsInternet instance = UtilsInternet.getInstance();
    private Fragment[] fragments;
    private int currentTabIndex;
    private int index;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public void initView() {
        /*mRadioGroup = (RadioGroup) findViewById(R.id.rg_main_swap);*/
    }

    @Override
    public void initData() {

        mHome = new HomeFragment();
        mMine = new MineFragment();
        fragments = new Fragment[]{mHome, mMine};
        getSupportFragmentManager().beginTransaction().add(R.id.rl_main_occupied, mHome)
                .add(R.id.rl_main_occupied, mMine).hide(mMine).show(mHome)
                .commit();
        OpenInstall.getInstall(this, this);
        instance.get(NetConfig.BANBEN_PATH, null, this);
    }

    @Override
    public void setData() {

       /* IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);*/
    }

    @Override
    public void setListener() {
       /* mRadioGroup.setOnCheckedChangeListener(this);*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            System.exit(0);
        }
    }

   /* @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_main_home:
                index = 0;
                break;
            case R.id.rb_main_mine:
                index = 1;
                break;
        }

        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments[currentTabIndex]);
        if (!fragments[index].isAdded()) {
            trx.replace(R.id.rl_main_occupied, fragments[index]);
        }
        trx.show(fragments[index]).commit();
        currentTabIndex = index;
    }*/


    // broadcast receiver
   /* private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend")) {
                mInfo.reFresh();
            }
        }
    };*/


/*
    public void setFresh(ReFreshUserInfo info) {
        mInfo = info;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }*/


    @Override
    public void onInstallFinish(AppData appData, Error error) {
        if (error == null) {
            MySharedPreferences.WriteShareId(this, appData.data);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadApk.unregisterBroadcast(this);
    }

    @Override
    public void onResponse(String result) {
        String versionName = getVersionName();
        if (!TextUtils.equals(versionName, result)) {
            showDownDialog();
        }
    }

    private void showDownDialog() {
        MySelfDialog dialog = new MySelfDialog(this);
        dialog.setTitle("更新提示");
        dialog.setMessage("1:更新了UI" + "\n" + "2:添加了部分功能" + "\n" + "3:解决了已知bug");
        dialog.setOnNoListener("取消", null);
        dialog.setOnYesListener("确定", this);
        dialog.show();
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

    @Override
    public void onClick() {
        //1.注册下载广播接收器
        DownloadApk.registerBroadcast(this);
        //2.删除已存在的Apk
        DownloadApk.removeFile(this);
        if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
            DownloadApk.downloadApk(getApplicationContext(), "http://hmyc365.net:8081/file/wb/apk/longwen.apk", "淘不够更新", "Hobbees");
        } else {
            DownLoadUtils.getInstance(getApplicationContext()).skipToDownloadManager();
        }
    }


}
