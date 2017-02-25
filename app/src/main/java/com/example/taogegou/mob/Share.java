package com.example.taogegou.mob;

import android.content.Context;

import cn.sharesdk.framework.ShareSDK;
import onekeyshare.OnekeyShare;

public class Share {

    public static void showShareApp(Context context, String title,
                                    String text, String content, String url, String titleUrl) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setTitleUrl(titleUrl);
        oks.setText(text);
        oks.setUrl(url);
        oks.setComment(content);
        oks.show(context);
    }

    public static void showShareShop(Context context, String title,
                                     String text, String content, String url, String titleUrl/*, String imageUrl*/) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setTitleUrl(titleUrl);
        oks.setText(text);
        oks.setUrl(url);
        oks.setComment(content);
        oks.setImagePath("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        oks.show(context);
    }
}
