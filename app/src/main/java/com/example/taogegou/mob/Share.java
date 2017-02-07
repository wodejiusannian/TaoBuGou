package com.example.taogegou.mob;

import android.content.Context;

import cn.sharesdk.framework.ShareSDK;
import onekeyshare.OnekeyShare;

public class Share {

    public static void showShare(Context context,String title,
                                 String text,String content,String url,String titleUrl){
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

}
