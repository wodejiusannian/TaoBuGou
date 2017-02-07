package com.example.taogegou.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.taogegou.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(layoutResID);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setNavigationBarTintEnabled(true);
        systemBarTintManager.setTintResource(R.color.main_color);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
        initData();
        setData();
        setListener();
    }
    public abstract void initView();
    public abstract void initData();
    public abstract void setData();
    public abstract void setListener();



}
