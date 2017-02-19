package com.example.taogegou.fragment_second;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.example.taogegou.R;
import com.example.taogegou.adapter.ShopAdapter;
import com.example.taogegou.base.BaseFragment;
import com.example.taogegou.bean.JsonBean;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.decoration.ItemDecoration;
import com.example.taogegou.ui_second.BuyActivity;
import com.example.taogegou.utils.ActivityUtils;
import com.example.taogegou.utils.Record;
import com.example.taogegou.utils.UtilsInternet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/18.
 */

public class Fragment_Default extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, UtilsInternet.XCallBack {
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mShow;
    private ShopAdapter mAdapter;
    private List<JsonBean> mData = new ArrayList<>();
    private GridLayoutManager mManager;
    private UtilsInternet instance = UtilsInternet.getInstance();
    private Map<String, Object> maps = new HashMap<>();
    private int page = 1;

    @Override
    protected View initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_result, null);
        getChildView(view);
        return view;
    }

    private void getChildView(View view) {
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sf_result_refresh);
        mShow = (RecyclerView) view.findViewById(R.id.rv_result_show);
    }

    @Override
    protected void initData() {
        super.initData();
        mManager = new GridLayoutManager(getContext(), 2);
        mAdapter = new ShopAdapter(getContext(), mData);
        loadMore(1);
    }

    @Override
    protected void setData() {
        super.setData();
        mShow.setLayoutManager(mManager);
        mShow.setAdapter(mAdapter);
        mShow.addItemDecoration(new ItemDecoration(10));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mRefresh.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
        mShow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断最后一个条目是否出现了
                    int last = mManager.findLastVisibleItemPosition();
                    if (last == mData.size() - 1) {
                        //如果出现了 加载下一页数据
                        loadMore(page);
                    }

                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadMore(1);
        mRefresh.setRefreshing(false);
    }

    private void loadMore(int page) {
        try {
            String encode = URLEncoder.encode(Record.search_Record, "utf-8");
            String searchPath = String.format(NetConfig.SEARCH_PATH, encode, 0, page);
            instance.get(searchPath, null, this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        JsonBean jsonBean = mData.get(tag);
        String url = jsonBean.getQuan_link();
        String isTmall = jsonBean.getIsTmall();
        String goodsID = jsonBean.getGoodsID();
        String quan_price = jsonBean.getQuan_price();
        String quan_id = jsonBean.getQuan_id();
        maps.put("url", url);
        maps.put("GoodsID", goodsID);
        maps.put("IsTmall", isTmall);
        maps.put("quan_price", quan_price);
        maps.put("quan_id", quan_id);
        ActivityUtils.switchTo(getActivity(), BuyActivity.class, maps);
    }

    @Override
    public void onResponse(String result) {
        if (!TextUtils.isEmpty(result) && !TextUtils.equals("0", result)) {
            try {
                if (page == 1) {
                    mData.clear();
                }
                page++;
                JSONObject object = new JSONObject(result);
                JSONArray array = object.getJSONArray("result");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jb = array.getJSONObject(i);
                    JsonBean bean = new JsonBean();
                    bean.setTitle(jb.getString("Title"));
                    bean.setPic(jb.getString("Pic"));
                    bean.setIsTmall(jb.getString("IsTmall"));
                    bean.setSales_num(jb.getString("Sales_num"));
                    bean.setOrg_Price(jb.getString("Org_Price"));
                    bean.setPrice(jb.getDouble("Price") + "");
                    bean.setQuan_price(jb.getString("Quan_price"));
                    bean.setQuan_time(jb.getString("Quan_time"));
                    bean.setQuan_id(jb.getString("Quan_id"));
                    bean.setGoodsID(jb.getString("GoodsID"));
                    mData.add(bean);
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
