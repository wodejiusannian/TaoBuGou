package com.example.taogegou.application;

import android.app.Application;

import com.example.taogegou.download.SystemParams;
import com.example.taogegou.utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.fm.openinstall.OpenInstall;

import org.xutils.x;

import cn.sharesdk.framework.ShareSDK;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        ShareSDK.initSDK(this);
        SystemParams.init(this);
        Fresco.initialize(this);
        OpenInstall.init(this, Utils.APPKEY);
        OpenInstall.setDebug(true);
    }
}
