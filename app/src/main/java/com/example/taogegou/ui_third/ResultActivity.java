package com.example.taogegou.ui_third;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.taogegou.R;
import com.example.taogegou.adapter.ShopAdapter;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.bean.JsonBean;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.decoration.ItemDecoration;
import com.example.taogegou.ui_second.BuyActivity;
import com.example.taogegou.utils.ActivityUtils;
import com.example.taogegou.utils.MyJson;
import com.example.taogegou.utils.UtilsInternet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultActivity extends BaseActivity implements View.OnClickListener, UtilsInternet.XCallBack, SwipeRefreshLayout.OnRefreshListener, RadioGroup.OnCheckedChangeListener {
    private ImageView mBack;
    private String search, encode;
    private RecyclerView mShow;
    private ShopAdapter mAdapter;
    private List<JsonBean> mData;
    private int page = 1, orderBy = 0;
    private GridLayoutManager mManager;
    private SwipeRefreshLayout mRefresh;
    private RadioGroup mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.iv_result_back);
        mShow = (RecyclerView) findViewById(R.id.rv_result_show);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.sf_result_refresh);
        mFilter = (RadioGroup) findViewById(R.id.rv_result_filter);
    }

    @Override
    public void initData() {
        search = getIntent().getStringExtra("search");
        mData = new ArrayList<>();
        try {
            encode = URLEncoder.encode(search, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getData(encode, page);

    }

    private void getData(String encode, int pages) {

        String searchPath = String.format(NetConfig.SEARCH_PATH, encode, orderBy, pages);
        UtilsInternet.getInstance().get(searchPath, null, this);

    }

    @Override
    public void setData() {
        mManager = new GridLayoutManager(this, 2);
        mAdapter = new ShopAdapter(this, mData);
        mShow.setLayoutManager(mManager);
        mShow.setAdapter(mAdapter);
        mShow.addItemDecoration(new ItemDecoration(10));
        RadioButton rb_standard = (RadioButton) mFilter.getChildAt(0);
        rb_standard.setChecked(true);
    }

    @Override
    public void setListener() {
        mBack.setOnClickListener(this);
        mRefresh.setOnRefreshListener(this);
        mFilter.setOnCheckedChangeListener(this);
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
                        getData(encode, page);
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_result_back:
                finish();
                break;
            case R.id.item_fragment_shop_Content:
                Map<String, Object> map = new HashMap<>();
                int tag = (int) v.getTag();
                JsonBean jsonBean = mData.get(tag);
                String url = jsonBean.getQuan_link();
                String isTmall = jsonBean.getIsTmall();
                String goodsID = jsonBean.getGoodsID();
                String quan_price = jsonBean.getQuan_price();
                String quan_id = jsonBean.getQuan_id();
                map.put("url", url);
                map.put("GoodsID", goodsID);
                map.put("IsTmall", isTmall);
                map.put("quan_price", quan_price);
                map.put("quan_id", quan_id);
                ActivityUtils.switchTo(this, BuyActivity.class, map);
                break;
            default:

                break;
        }
    }

    @Override
    public void onResponse(String result) {
        if (!TextUtils.isEmpty(result) && !TextUtils.equals("0", result)) {
            if (page == 1) {
                mData.clear();
            }
            page++;
            List<JsonBean> data = new MyJson().getData(result);
            mData.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        getData(encode, 1);
        mRefresh.setRefreshing(false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_result_filter_standard:
                page = 1;
                orderBy = 0;
                getData(encode, 1);
                break;
            case R.id.rb_result_filter_price:
                page = 1;
                orderBy = 1;
                getData(encode, 1);
                break;
            case R.id.rb_result_filter_many:
                page = 1;
                orderBy = 2;
                getData(encode, 1);
                break;
            default:

                break;
        }
    }
}
