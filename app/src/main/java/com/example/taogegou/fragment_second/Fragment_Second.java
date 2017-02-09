package com.example.taogegou.fragment_second;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.example.taogegou.R;
import com.example.taogegou.adapter.MallAdapter;
import com.example.taogegou.base.BaseFragment;
import com.example.taogegou.bean.JsonBean;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.decoration.ItemDecoration;
import com.example.taogegou.utils.UtilsInternet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Second extends BaseFragment implements UtilsInternet.XCallBack {
    private RecyclerView mContent;
    private MallAdapter mAdapter;
    private List<JsonBean> mData;
    private int pages = 1;
    private GridLayoutManager mManager;

    @Override
    protected View initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.fragment_prize, null);
        getChildView(inflate);
        return inflate;
    }

    private void getChildView(View inflate) {
        mContent = (RecyclerView) inflate.findViewById(R.id.rv_prize_content);
    }

    @Override
    protected void initData() {
        super.initData();
        mData = new ArrayList<>();
        mAdapter = new MallAdapter(getContext(), mData);
        mManager = new GridLayoutManager(getContext(), 2);
        mContent.setLayoutManager(mManager);
        mContent.setAdapter(mAdapter);
        mContent.addItemDecoration(new ItemDecoration(15));
    }

    @Override
    protected void setData() {
        super.setData();
        loadMore(1);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断最后一个条目是否出现了
                    int last = mManager.findLastVisibleItemPosition();
                    if (last == mData.size() - 1) {
                        //如果出现了 加载下一页数据
                        loadMore(pages);
                    }

                }
            }
        });
    }

    private void loadMore(int page) {
        String url = String.format(NetConfig.MALL_SECOND, page);
        UtilsInternet.getInstance().get(url, null, this);
    }

    @Override
    public void onResponse(String result) {
        if (pages == 1) {
            mData.clear();
        }
        pages++;
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray array = obj.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jb = array.getJSONObject(i);
                String cid = jb.getString("Cid");
                if (TextUtils.equals("1", cid) || TextUtils.equals("5", cid)) {
                    JsonBean bean = new JsonBean();
                    bean.setTitle(jb.getString("Title"));
                    bean.setPic(jb.getString("Pic"));
                    bean.setIsTmall(jb.getString("IsTmall"));
                    bean.setSales_num(jb.getString("Sales_num"));
                    bean.setOrg_Price(jb.getString("Org_Price"));
                    bean.setPrice(jb.getDouble("Price") + "");
                    bean.setQuan_price(jb.getString("Quan_price"));
                    bean.setQuan_time(jb.getString("Quan_time"));
                    bean.setQuan_link(jb.getString("Quan_link"));
                    bean.setGoodsID(jb.getString("GoodsID"));
                    bean.setQuan_id(jb.getString("Quan_id"));
                    mData.add(bean);
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
