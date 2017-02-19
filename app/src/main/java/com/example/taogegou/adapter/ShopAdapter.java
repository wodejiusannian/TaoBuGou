package com.example.taogegou.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.bean.JsonBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {
    private List<JsonBean> mData;
    private Context mContext;
    private View.OnClickListener mListener;

    public void setOnItemClickListener(View.OnClickListener listener) {
        mListener = listener;
    }


    public ShopAdapter(Context context, List<JsonBean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_shop, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JsonBean bean = mData.get(position);
        String pic = bean.getPic();
        String title = bean.getTitle();
        String isTmall = bean.getIsTmall();
        String org_price = bean.getOrg_Price();
        org_price = org_price.substring(0, org_price.length() - 1);
        String price = bean.getPrice();
        String sales_num = bean.getSales_num();
        /*String quan_price = bean.getQuan_price();*/
        /*quan_price = quan_price.substring(0,quan_price.length()-3);*/
        if (!TextUtils.isEmpty(pic)) {
            Uri parse = Uri.parse(pic);
            holder.mImageViewPic.setImageURI(parse);
        }
        holder.mTextViewOrg_Price.setText(org_price);
        holder.mTextViewPrice.setText(price);
        /*holder.mTextViewQuan_price.setText(quan_price);*/
        holder.mTextViewSales_num.setText(sales_num);
        holder.mTextViewTitle.setText(title);
        if (TextUtils.equals("1", isTmall)) {
            holder.mImageViewIsTmall.setImageResource(R.mipmap.tall);
            holder.mIsTmall.setText("天猫");
        } else {
            holder.mImageViewIsTmall.setImageResource(R.mipmap.taobao);
        }

        if (mListener != null) {
            holder.mLinearLayoutContent.setTag(position);
            holder.mLinearLayoutContent.setOnClickListener(mListener);
        }

    }

    @Override
    public int getItemCount() {

        return mData.size() == 0 ? 0 : mData.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLinearLayoutContent;
        private ImageView mImageViewIsTmall;
        private TextView mTextViewTitle, mTextViewOrg_Price, mTextViewPrice,
                mTextViewSales_num, mIsTmall/*, mTextViewQuan_price*/;
        private SimpleDraweeView mImageViewPic;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.item_fragment_shop_Title);
            mTextViewOrg_Price = (TextView) itemView.findViewById(R.id.item_fragment_shop_Org_Price);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.item_fragment_shop_price);
            mImageViewIsTmall = (ImageView) itemView.findViewById(R.id.item_fragment_shop_IsTmall);
            mTextViewSales_num = (TextView) itemView.findViewById(R.id.item_fragment_shop_Sales_num);
            /*mTextViewQuan_price = (TextView) itemView.findViewById(R.id.item_fragment_shop_Quan_price);*/
            mImageViewPic = (SimpleDraweeView) itemView.findViewById(R.id.item_fragment_shop_pic);
            mLinearLayoutContent = (LinearLayout) itemView.findViewById(R.id.item_fragment_shop_Content);
            mIsTmall = (TextView) itemView.findViewById(R.id.item_shop_tv_is_tmall);
        }

    }
}
