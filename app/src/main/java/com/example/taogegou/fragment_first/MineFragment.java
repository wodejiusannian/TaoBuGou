package com.example.taogegou.fragment_first;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseFragment;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.custom.MySelfDialog;
import com.example.taogegou.inter.MyThirdData;
import com.example.taogegou.inter.ReFreshUserInfo;
import com.example.taogegou.mob.Login;
import com.example.taogegou.mob.Share;
import com.example.taogegou.ui_first.MainActivity;
import com.example.taogegou.ui_second.LoginActivity;
import com.example.taogegou.ui_second.SettingActivity;
import com.example.taogegou.ui_third.ConnectionActivity;
import com.example.taogegou.ui_third.FeedBackActivity;
import com.example.taogegou.ui_third.LogisticActivity;
import com.example.taogegou.ui_third.MallActivity;
import com.example.taogegou.ui_third.UseActivity;
import com.example.taogegou.utils.ActivityUtils;
import com.example.taogegou.utils.MySharedPreferences;
import com.example.taogegou.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class MineFragment extends BaseFragment implements View.OnClickListener, MyThirdData, ReFreshUserInfo {
    private LinearLayout mLineShare, mUse, mFeedback, mConnection, mMall, mLogistic;
    private RelativeLayout mUserInfo;
    private ImageView mImageViewQQ, mImageViewWeChat, mSetting;
    private SimpleDraweeView mPhoto;
    private Login mLogin;
    private TextView mName;
    private String userId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String url = data.getString("url");
            String userName = data.getString("userName");
            mPhoto.setImageURI(url);
            mName.setText(userName);
        }
    };

    @Override
    protected View initView() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine, null);
        getChildView(inflate);
        return inflate;
    }

    private void getChildView(View inflate) {
        mLineShare = (LinearLayout) inflate.findViewById(R.id.ll_mine_share);
        mLogistic = (LinearLayout) inflate.findViewById(R.id.ll_mine_logistic);
        mUserInfo = (RelativeLayout) inflate.findViewById(R.id.rl_mine_info);
        mImageViewQQ = (ImageView) inflate.findViewById(R.id.iv_mine_qq);
        mImageViewWeChat = (ImageView) inflate.findViewById(R.id.iv_mine_wechat);
        mSetting = (ImageView) inflate.findViewById(R.id.iv_mine_setting);
        mPhoto = (SimpleDraweeView) inflate.findViewById(R.id.iv_mine_photo);
        mName = (TextView) inflate.findViewById(R.id.tv_mine_name);
        mUse = (LinearLayout) inflate.findViewById(R.id.ll_mine_at_me_use);
        mFeedback = (LinearLayout) inflate.findViewById(R.id.ll_mine_at_me_feedback);
        mConnection = (LinearLayout) inflate.findViewById(R.id.ll_mine_at_me_connection);
        mMall = (LinearLayout) inflate.findViewById(R.id.ll_mine_mall);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mLineShare.setOnClickListener(this);
        mUserInfo.setOnClickListener(this);
        mImageViewQQ.setOnClickListener(this);
        mImageViewWeChat.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mUse.setOnClickListener(this);
        mFeedback.setOnClickListener(this);
        mConnection.setOnClickListener(this);
        mMall.setOnClickListener(this);
        mLogistic.setOnClickListener(this);
        MainActivity activity = (MainActivity) getActivity();
        activity.setFresh(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mLogin = new Login();
        mLogin.getData(this);
        userId = MySharedPreferences.getUserId(getContext());
        reFreshUser();
    }


    private void reFreshUser() {
        userId = MySharedPreferences.getUserId(getContext());
        String userInfo = MySharedPreferences.getUserInfo(getContext());
        userId = MySharedPreferences.getUserId(getContext());
        if (!TextUtils.equals(",", userInfo)) {
            String[] split = userInfo.split(",");
            String photo = split[0];
            String name = split[1];
            if (!TextUtils.isEmpty(name)) {
                mName.setText(name);
            }
            if (!TextUtils.isEmpty(photo)) {
                mPhoto.setImageURI(photo);
            }
        } else {
            if (TextUtils.isEmpty(userId)) {
                mName.setText("注册/登录");
                Uri parse = Uri.parse("res:///" + R.mipmap.red);
                mPhoto.setImageURI(parse);
            } else {
                mName.setText("TGG_146" + userId);
                Uri parse = Uri.parse("res:///" + R.mipmap.blue);
                mPhoto.setImageURI(parse);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mine_share:
                if (!TextUtils.isEmpty(userId)) {
                    String shareUrl = getShareUrl();
                    Share.showShare(getContext(), "我是尝试分享", "开始分享了", "这个是内容", shareUrl, shareUrl);
                } else {
                    noLoginButShare();
                }
                break;
            case R.id.rl_mine_info:
                if (TextUtils.isEmpty(userId)) {
                    ActivityUtils.switchTo(getActivity(), LoginActivity.class);
                } else {
                    ActivityUtils.switchTo(getActivity(), SettingActivity.class);
                }
                break;
            case R.id.iv_mine_qq:
                mLogin.whileLogin(getContext(), QQ.NAME);
                break;
            case R.id.iv_mine_wechat:
                mLogin.whileLogin(getContext(), Wechat.NAME);
                break;
            case R.id.iv_mine_setting:
                ActivityUtils.switchTo(getActivity(), SettingActivity.class);
                break;
            case R.id.ll_mine_at_me_use:
                ActivityUtils.switchTo(getActivity(), UseActivity.class);
                break;
            case R.id.ll_mine_at_me_connection:
                ActivityUtils.switchTo(getActivity(), ConnectionActivity.class);
                break;
            case R.id.ll_mine_at_me_feedback:
                ActivityUtils.switchTo(getActivity(), FeedBackActivity.class);
                break;
            case R.id.ll_mine_mall:
                ActivityUtils.switchTo(getActivity(), MallActivity.class);
                break;
            case R.id.ll_mine_logistic:
                ActivityUtils.switchTo(getActivity(), LogisticActivity.class);
                break;
            default:

                break;
        }
    }


    private String getShareUrl() {
        String userId = MySharedPreferences.getUserId(getContext());
        String sceneUrl = Utils.HTTP_SERVER + "/test/" + Utils.APPKEY;
        String params = "";
        try {
            byte[] values = Base64.encode(userId.getBytes("UTF-8"), Base64.URL_SAFE);
            params = "jsonParam=" + new String(values, "utf-8");
            return sceneUrl + "?" + params;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void getData(final String url, final String openid, final String userName) {
        if (openid != null) {
            String thirdPath = String.format(NetConfig.LOGIN_PATH_THIRD, openid);
            RequestParams params = new RequestParams(thirdPath);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i("TAG", "result" + result);
                    int parseInt = Integer.parseInt(result);
                    if (parseInt > 0) {
                        MySharedPreferences.WriteUserId(getContext(), result);
                        userId = MySharedPreferences.getUserId(getContext());
                        MySharedPreferences.WriteUserInfo(getContext(), url, userName);
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        bundle.putString("userName", userName);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
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

        }

    }

    @Override
    public void reFresh() {
        reFreshUser();
    }

    private void noLoginButShare() {
        MySelfDialog mySelfDialog = new MySelfDialog(getContext());
        mySelfDialog.setMessage("没有登录分享，不能参与抽奖哦");
        mySelfDialog.setOnNoListener("去登陆", new MySelfDialog.OnNoClickListener() {
            @Override
            public void onClick() {
                ActivityUtils.switchTo(getActivity(), LoginActivity.class);
            }
        });
        mySelfDialog.setOnYesListener("继续分享", new MySelfDialog.OnYesClickListener() {
            @Override
            public void onClick() {
                String shareUrl = getShareUrl();
                Share.showShare(getContext(), "我是尝试分享", "开始分享了", "这个是内容", shareUrl, shareUrl);
            }
        });
        mySelfDialog.show();
    }
}
