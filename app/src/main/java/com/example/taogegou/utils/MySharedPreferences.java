package com.example.taogegou.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    public static int IsFirstLoading(Context context){
        SharedPreferences isFirstLoading = context.getSharedPreferences("isFirstLoading", 0);
        int loading = isFirstLoading.getInt("Loading", 0);
        return loading;
    }
    public static void WriteLoading(Context context){
        SharedPreferences isFirstLoading = context.getSharedPreferences("isFirstLoading", 0);
        SharedPreferences.Editor edit = isFirstLoading.edit();
        edit.putInt("Loading", 1);
        edit.commit();
    };

    public static String getUserId(Context context){
        SharedPreferences UserId = context.getSharedPreferences("UserId", 0);
        String userId = UserId.getString("userId","");

        return userId;
    }

    public static void WriteUserId(Context context,String result){
        SharedPreferences isFirstLoading = context.getSharedPreferences("UserId", 0);
        SharedPreferences.Editor edit = isFirstLoading.edit();
        edit.putString("userId", result);
        edit.commit();
    };

    public static void WriteUserInfo(Context context,String userName,String userPhoto){
        SharedPreferences isFirstLoading = context.getSharedPreferences("UserId", 0);
        SharedPreferences.Editor edit = isFirstLoading.edit();
        edit.putString("userName", userName);
        edit.putString("userPhoto", userPhoto);
        edit.commit();
    };


    public static String getUserInfo(Context context){
        SharedPreferences UserId = context.getSharedPreferences("UserId", 0);
        String userName = UserId.getString("userName", "");
        String userPhoto = UserId.getString("userPhoto", "");
        return userName+","+userPhoto;
    }

    public static void WriteShareId(Context context,String shareId){
        SharedPreferences isFirstLoading = context.getSharedPreferences("ShareId", 0);
        SharedPreferences.Editor edit = isFirstLoading.edit();
        edit.putString("shareId", shareId);
        edit.commit();
    };

    public static String getShareId(Context context){
        SharedPreferences UserId = context.getSharedPreferences("ShareId", 0);
        String shareId = UserId.getString("shareId", "");
        return shareId;
    }
}
