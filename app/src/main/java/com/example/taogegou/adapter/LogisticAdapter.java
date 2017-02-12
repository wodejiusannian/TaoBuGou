package com.example.taogegou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.bean.Logistic;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/12.
 */

public class LogisticAdapter extends BaseAdapter {
    private List<Logistic.ResultBean> mData;
    private Context mContext;

    public LogisticAdapter(List<Logistic.ResultBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size() == 0 ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_logistic, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        Logistic.ResultBean bean = mData.get(position);
        holder.mSv.setImageURI("https://www.baidu.com/img/bd_logo1.png");
        holder.phone.setText(bean.getPhone_number());
        holder.award.setText(bean.getAward_type() + "");
        holder.address.setText(bean.getAddress());
        holder.name.setText(bean.getName());
        return convertView;
    }


    public static class MyViewHolder {
        private SimpleDraweeView mSv;
        private TextView name, address, award, phone;

        public MyViewHolder(View itemView) {
            mSv = (SimpleDraweeView) itemView.findViewById(R.id.sv_item_logistic);
            name = (TextView) itemView.findViewById(R.id.sv_item_logistic_name);
            address = (TextView) itemView.findViewById(R.id.sv_item_logistic_address);
            award = (TextView) itemView.findViewById(R.id.sv_item_logistic_award_type);
            phone = (TextView) itemView.findViewById(R.id.sv_item_logistic_phone);
        }

    }
}
