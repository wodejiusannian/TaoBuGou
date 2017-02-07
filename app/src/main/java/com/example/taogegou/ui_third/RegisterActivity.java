package com.example.taogegou.ui_third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.utils.MySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImageViewBack;
    private EditText mEditPhone,mEditAuth,mEditPWD,mEditSure;
    private Button mGetAuth,mRegister;
    private static final int SUBMIT_VERIFICATION_CODE_COMPLETE = 1;
    private static final int GET_VERIFICATION_CODE_COMPLETE = 2;
    private static final int RESULT_ERROR = 3;
    private String phone,pwd;
    private boolean flag = true;
    private int time = 90;
    private boolean isGetAuth = false;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;
            mGetAuth.setText(time+"s后重新获取");
            if(time==0) {
                flag = true;
                mGetAuth.setText("重新获取");
                time = 90;
                return;
            }
            mHandler.sendEmptyMessageDelayed(1,1000);
        }
    };
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SUBMIT_VERIFICATION_CODE_COMPLETE:
                    Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case GET_VERIFICATION_CODE_COMPLETE:
                    upRegister();
                    break;
                case RESULT_ERROR:
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private EventHandler eh = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                // 回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 提交验证码正确成功
                    handler.sendEmptyMessage(GET_VERIFICATION_CODE_COMPLETE);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功
                    handler.sendEmptyMessage(SUBMIT_VERIFICATION_CODE_COMPLETE);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else if (result == SMSSDK.RESULT_ERROR) {// 错误情况
                Throwable throwable = (Throwable) data;
                throwable.printStackTrace();
                JSONObject object;
                try {
                    object = new JSONObject(throwable.getMessage());
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("status", object.optInt("status"));// 错误代码
                    bundle.putString("detail", object.optString("detail"));// 错误描述
                    msg.setData(bundle);
                    msg.what = RESULT_ERROR;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initView() {
        mImageViewBack = (ImageView) findViewById(R.id.iv_register_back);
        mEditPhone = (EditText) findViewById(R.id.et_register_phone);
        mEditAuth = (EditText) findViewById(R.id.et_register_auth);
        mEditPWD = (EditText) findViewById(R.id.et_register_pwd);
        mEditSure = (EditText) findViewById(R.id.et_register_sure_pwd);
        mGetAuth = (Button) findViewById(R.id.btn_register_get_auth);
        mRegister = (Button) findViewById(R.id.btn_register_register);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        mImageViewBack.setOnClickListener(this);
        mGetAuth.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_register_back:
                finish();
                break;
            case R.id.btn_register_get_auth:
                phone = mEditPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)&&phone.length()==11){
                    if (flag){
                        getAuth(phone);
                        mHandler.sendEmptyMessageDelayed(1,1000);
                        flag = false;
                        isGetAuth = true;
                    }else{
                        Toast.makeText(RegisterActivity.this, "亲，不要重复获取哦", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register_register:
                pwd = mEditPWD.getText().toString().trim();
                String pwds = mEditSure.getText().toString().trim();
                String auth = mEditAuth.getText().toString().trim();
                if (isGetAuth){
                    if (TextUtils.equals(pwd,pwds)){
                        if (!TextUtils.isEmpty(pwd)&&pwd.length()>5&&pwd.length()<17){
                            SMSSDK.submitVerificationCode("86", phone, auth);
                        }else{
                            Toast.makeText(RegisterActivity.this, "密码长度为6-16位", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "两次密码输入不一样", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "亲，获取下验证码哦", Toast.LENGTH_SHORT).show();
                }
                break;
            default:

            break;
        }
    }
   //提交注册
    private void upRegister() {
        String shareId = MySharedPreferences.getShareId(this);
        String url = String.format(NetConfig.REGISTER_PATH, phone,pwd,shareId);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                switch(result){
                    case "1":
                        Toast.makeText(RegisterActivity.this, "该手机号已注册", Toast.LENGTH_SHORT).show();
                        break;
                    case "2":
                        Toast.makeText(RegisterActivity.this, "这个", Toast.LENGTH_SHORT).show();
                        break;
                    case "3":
                        Toast.makeText(RegisterActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case "4":
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:

                        break;
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
    //获取验证码
    private void getAuth(String phone) {
        SMSSDK.initSDK(this, "1a74acb38745b", "65b5b019679f3ddc906aa683632f026b");
        SMSSDK.registerEventHandler(eh);
        SMSSDK.getVerificationCode("86", phone);
    }
}
