package com.example.taogegou.ui_third;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.utils.MySharedPreferences;
import com.example.taogegou.utils.UtilsInternet;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonActivity extends BaseActivity implements View.OnClickListener, UtilsInternet.XCallBack {
    private static final int REQUEST_CAMERA_CODE = 11;
    private RelativeLayout mPhoto;
    private UtilsInternet instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
    }

    @Override
    public void initView() {
        mPhoto = (RelativeLayout) findViewById(R.id.rl_person_photo);
    }

    @Override
    public void initData() {
        instance = UtilsInternet.getInstance();
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        mPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_person_photo:
                openCamera();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(String result) {
        Log.i("TAG", "onResponse: " + result);
        Toast.makeText(this, "url" + result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Map<String, File> map = new HashMap<>();
                    Map<String, String> maps = new HashMap<>();
                    map.put("msg", new File(list.get(0)));
                    maps.put("userID", MySharedPreferences.getUserId(this));
                    instance.upLoadFile("http://120.77.46.28:8080/test/servlet/uploadHeadPortrait", maps, map, this);
                    break;
            }
        }
    }

    public void back(View view) {
        finish();
    }

    //打开照相机
    private void openCamera() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true);
        intent.setMaxTotal(1);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }


}
