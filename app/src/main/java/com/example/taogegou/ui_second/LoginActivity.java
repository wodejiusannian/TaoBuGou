package com.example.taogegou.ui_second;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.ui_third.FoundPWDActivity;
import com.example.taogegou.ui_third.RegisterActivity;
import com.example.taogegou.utils.ActivityUtils;
import com.example.taogegou.utils.MySharedPreferences;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImageViewBack;
    private Button mButtonLogin;
    private EditText mEditTextPhone,mEditTextPWD;
    private TextView mTextRegister,mTextViewFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView() {
        mImageViewBack = (ImageView) findViewById(R.id.iv_login_back);
        mButtonLogin = (Button) findViewById(R.id.btn_login_login);
        mEditTextPhone = (EditText) findViewById(R.id.et_login_phone);
        mEditTextPWD = (EditText) findViewById(R.id.et_login_pwd);
        mTextRegister= (TextView) findViewById(R.id.tv_login_register);
        mTextViewFound = (TextView) findViewById(R.id.tv_login_found_pwd);
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
        mButtonLogin.setOnClickListener(this);
        mTextRegister.setOnClickListener(this);
        mTextViewFound.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_login_back:
                finish();
                break;
            case R.id.btn_login_login:
                login();
                break;
            case R.id.tv_login_register:
                ActivityUtils.switchTo(this, RegisterActivity.class);
                break;
            case R.id.tv_login_found_pwd:
                ActivityUtils.switchTo(this, FoundPWDActivity.class);
                break;
            default:

                break;
        }
    }
    //登录的方法
    private void login() {
        String phone = mEditTextPhone.getText().toString().trim();
        String pwd = mEditTextPWD.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(pwd)){
            if (phone.length()==11){
                String loginUrl = String.format(NetConfig.LOGIN_PATH_PHONE, phone, pwd);
                RequestParams params = new RequestParams(loginUrl);
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("TAG", "result"+result);
                        int parseInt = Integer.parseInt(result);
                        if (parseInt>0){
                            MySharedPreferences.WriteUserId(LoginActivity.this,result);
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            sendBroad();
                            finish();
                        }else if (parseInt == -6){
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }else if (parseInt == -5){
                            Toast.makeText(LoginActivity.this, "亲，此手机号还没有注册哦，可以使用手机号快速注册", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(LoginActivity.this, R.string.err_internet, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }else{
                Toast.makeText(LoginActivity.this, "亲，请输入正确的手机号哦", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(LoginActivity.this, "亲，账号或者用户名不能为空哦", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendBroad(){
        Intent intent = new Intent();
        intent.setAction("action.refreshFriend");
        sendBroadcast(intent);
    }
}
