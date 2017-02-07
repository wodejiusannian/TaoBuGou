package com.example.taogegou.ui_first;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.fragment_first.HomeFragment;
import com.example.taogegou.fragment_first.MineFragment;
import com.example.taogegou.inter.ReFreshUserInfo;
import com.example.taogegou.utils.MySharedPreferences;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallListener;
import com.fm.openinstall.model.AppData;
import com.fm.openinstall.model.Error;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,AppInstallListener {

    private static boolean isExit = false;
    /*private Fragment mCurrentFragment;
    private FragmentManager mManger;*/
    private RadioGroup mRadioGroup;
    private ReFreshUserInfo mInfo;

    private HomeFragment mHome;
    private MineFragment mMine;
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
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_main_swap);
    }

    @Override
    public void initData() {

        mHome = new HomeFragment();
        mMine = new MineFragment();
        fragments = new Fragment[] { mHome, mMine };
        getSupportFragmentManager().beginTransaction().add(R.id.rl_main_occupied, mHome)
                .add(R.id.rl_main_occupied, mMine).hide(mMine).show(mHome)
                .commit();
        /*mManger = getSupportFragmentManager();
        showAndHide(R.id.rl_main_occupied, HomeFragment.class);*/
        OpenInstall.getInstall(this, this);
    }

    @Override
    public void setData() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }

    @Override
    public void setListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
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
    /*private void showAndHide(int contentId, Class<? extends Fragment> clazz) {
        //判断当前的fragment和需要替换的fragment是否是统一一个
        if (mCurrentFragment != null && mCurrentFragment.getClass().getSimpleName().equals(clazz.getSimpleName())) {
            return;
        }

        //判断fragment有没有添加过
        FragmentTransaction transaction = mManger.beginTransaction();
        //
        Fragment fragment = null;
        try {

            Fragment fragmentByTag = mManger.findFragmentByTag(clazz.getSimpleName());

            if (fragmentByTag != null) {
                //显示需要的fragment
                transaction.show(fragmentByTag);
                //隐藏当前的fragment
                transaction.hide(mCurrentFragment);
                //让记录当前的fragment赋值为显示的fragment
                mCurrentFragment = fragmentByTag;
            } else {
                //通过无参的 公开的构造函数来创建Fragment实例
                fragment = clazz.newInstance();
                //当前的Fragment没有添加过 把Fragment添加到manager里面
                transaction.add(contentId, fragment, clazz.getSimpleName());
                if (mCurrentFragment != null) {
                    //隐藏当前的Fragment
                    transaction.hide(mCurrentFragment);
                }
                //记录当前的Fragment是那个
                mCurrentFragment = fragment;
            }
            transaction.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }*/
    /*private void showAndHide(int contentId, Class<? extends Fragment> clazz){
        //判断当前的fragment是否和需要替换掉的fragment是否一致
        if (mCurrentFragment != null && mCurrentFragment.getClass().getSimpleName().equals(clazz.getCanonicalName())) {
            return;
        }
        FragmentTransaction transaction = mManager.beginTransaction();
        Fragment fragment = null;
        try {
            Fragment fragmentByTag = mManager.findFragmentByTag(clazz.getSimpleName());
            if(fragmentByTag!=null){
                transaction.show(fragmentByTag);
                transaction.hide(mCurrentFragment);
                mCurrentFragment = fragmentByTag;
            }else{
                fragment = clazz.newInstance();
                transaction.add(contentId,fragment,clazz.getSimpleName());
                if(mCurrentFragment!=null){
                    transaction.hide(mCurrentFragment);
                }
                mCurrentFragment = fragment;
            }
            transaction.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

       switch (checkedId){
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
        /*Class<? extends Fragment> clazz = null;
        switch (checkedId) {
            case R.id.rb_main_home:
                clazz = HomeFragment.class;
                break;
            case R.id.rb_main_mine:
                clazz = MineFragment.class;
                break;
            default:
                break;
        }
        showAndHide(R.id.rl_main_occupied, clazz);*/

    }


    // broadcast receiver
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend")){
                mInfo.reFresh();
            }
        }
    };


    public void setFresh(ReFreshUserInfo info){
        mInfo = info;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }


    @Override
    public void onInstallFinish(AppData appData, Error error) {
        if (error == null) {
            Toast.makeText(this, "推荐者UserId"+appData.data, Toast.LENGTH_SHORT).show();
            MySharedPreferences.WriteShareId(this,appData.data);
        } else {
           Toast.makeText(this, MySharedPreferences.getShareId(this), Toast.LENGTH_SHORT).show();
        }
    }

}
