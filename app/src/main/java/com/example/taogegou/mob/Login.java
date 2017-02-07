package com.example.taogegou.mob;

import android.content.Context;
import android.util.Log;

import com.example.taogegou.inter.MyThirdData;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class Login implements PlatformActionListener {
    public MyThirdData mThirdData;

    public void getData(MyThirdData thirdData){
        mThirdData = thirdData;
    }

    public  void whileLogin(Context context, String name){
        ShareSDK.initSDK(context);
        Platform plat= ShareSDK.getPlatform(name);
        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }
        plat.setPlatformActionListener(this);
        plat.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String userIcon = platform.getDb().getUserIcon();
        String userId = platform.getDb().getUserId();
        String userName = platform.getDb().getUserName();
        Log.i("TAG", "=====>>>"+userIcon+"==="+userId+"==="+userName);
        mThirdData.getData(userIcon,userId,userName);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    public void remove(Context context,String name){

    }
}
