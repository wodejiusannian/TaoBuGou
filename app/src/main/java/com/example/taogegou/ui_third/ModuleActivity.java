package com.example.taogegou.ui_third;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taogegou.R;
import com.example.taogegou.adapter.ShopAdapter;
import com.example.taogegou.base.BaseActivity;
import com.example.taogegou.bean.JsonBean;
import com.example.taogegou.decoration.ItemDecoration;
import com.example.taogegou.ui_second.BuyActivity;
import com.example.taogegou.utils.ActivityUtils;
import com.example.taogegou.utils.MyJson;
import com.example.taogegou.utils.UtilsInternet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleActivity extends BaseActivity implements View.OnClickListener, UtilsInternet.XCallBack, SwipeRefreshLayout.OnRefreshListener {
    private ImageView mBack;
    private RecyclerView mShow;
    private ShopAdapter adapter;
    private List<JsonBean> mData;
    private int pageIndex = 1;
    private String url,moduleName;
    private SwipeRefreshLayout mRefresh;
    private TextView mModuleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.iv_module_back);
        mShow = (RecyclerView) findViewById(R.id.rv_module_show);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.sf_module_refresh);
        mModuleName = (TextView) findViewById(R.id.tv_module_title);
    }

    @Override
    public void initData() {
        mData = new ArrayList<>();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        moduleName = intent.getStringExtra("moduleName");
        mModuleName.setText(moduleName);
        adapter = new ShopAdapter(this,mData);
        LoadMore(pageIndex);
    }

    private void LoadMore(int index) {
        String shopUrl = String.format(url, index);
        UtilsInternet.getInstance().get(shopUrl,null,this);
    }

    @Override
    public void setData() {
        final GridLayoutManager manager = new GridLayoutManager(this,2);
        mShow.setLayoutManager(manager);
        mShow.setAdapter(adapter);
        mShow.addItemDecoration(new ItemDecoration(15));
        mShow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState== RecyclerView.SCROLL_STATE_IDLE){
                    // 判断最后一个条目是否出现了
                    int last = manager.findLastVisibleItemPosition();
                    if (last==mData.size()-1){
                        //如果出现了 加载下一页数据
                        LoadMore(pageIndex);
                    }

                }
            }
        });
    }

    @Override
    public void setListener() {
        mBack.setOnClickListener(this);
        adapter.setOnItemClickListener(this);
        mRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_module_back:
                finish();
                break;
            case R.id.item_fragment_shop_Content:
                Map<String,Object> map = new HashMap<>();
                int tag = (int) v.getTag();
                JsonBean jsonBean = mData.get(tag);
                String url = jsonBean.getQuan_link();
                String isTmall = jsonBean.getIsTmall();
                String goodsID = jsonBean.getGoodsID();
                String quan_price = jsonBean.getQuan_price();
                String quan_id = jsonBean.getQuan_id();
                map.put("url",url);
                map.put("GoodsID",goodsID);
                map.put("IsTmall",isTmall);
                map.put("quan_price",quan_price);
                map.put("quan_id",quan_id);
                ActivityUtils.switchTo(ModuleActivity.this,BuyActivity.class,map);
                break;
            default:

            break;
        }
    }

    @Override
    public void onResponse(String result) {
        if (pageIndex==1){
            mData.clear();
        }
        pageIndex++;
        List<JsonBean> data = new MyJson().getData(result);
        mData.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        LoadMore(1);
        mRefresh.setRefreshing(false);
    }
}
