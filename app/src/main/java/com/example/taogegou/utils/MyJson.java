package com.example.taogegou.utils;

import android.text.TextUtils;

import com.example.taogegou.bean.JsonBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小五 on 2017/1/14.
 */
public class MyJson {

    private  List<JsonBean> mData = new ArrayList<>();

    public  List<JsonBean> getData(String result){
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray array = obj.getJSONArray("result");
            for(int i = 0; i < array.length(); i++) {
                JSONObject jb = array.getJSONObject(i);
                String cid = jb.getString("Cid");
                JsonBean bean = new JsonBean();
                bean.setTitle(jb.getString("Title"));
                bean.setPic(jb.getString("Pic"));
                bean.setIsTmall(jb.getString("IsTmall"));
                bean.setSales_num(jb.getString("Sales_num"));
                bean.setOrg_Price(jb.getString("Org_Price"));
                bean.setPrice(jb.getDouble("Price")+"");
                bean.setQuan_price(jb.getString("Quan_price"));
                bean.setQuan_time(jb.getString("Quan_time"));
                bean.setQuan_link(jb.getString("Quan_link"));
                bean.setGoodsID(jb.getString("GoodsID"));
                bean.setQuan_id(jb.getString("Quan_id"));
                mData.add(bean);
            }
            return mData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  List<JsonBean> getJsonResult(String result,String str1,String str2){
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray array = obj.getJSONArray("result");
            for(int i = 0; i < array.length(); i++) {
                JSONObject jb = array.getJSONObject(i);
                String cid = jb.getString("Cid");
                if (TextUtils.equals(str1,cid)||TextUtils.equals(str2,cid)){
                    JsonBean bean = new JsonBean();
                    bean.setTitle(jb.getString("Title"));
                    bean.setPic(jb.getString("Pic"));
                    bean.setIsTmall(jb.getString("IsTmall"));
                    bean.setSales_num(jb.getString("Sales_num"));
                    bean.setOrg_Price(jb.getString("Org_Price"));
                    bean.setPrice(jb.getDouble("Price")+"");
                    bean.setQuan_price(jb.getString("Quan_price"));
                    bean.setQuan_time(jb.getString("Quan_time"));
                    bean.setQuan_link(jb.getString("Quan_link"));
                    bean.setGoodsID(jb.getString("GoodsID"));
                    mData.add(bean);
                }
            }
            return mData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
