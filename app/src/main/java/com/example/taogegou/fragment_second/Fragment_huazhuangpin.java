package com.example.taogegou.fragment_second;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.taogegou.R;
import com.example.taogegou.adapter.ShopAdapter;
import com.example.taogegou.base.BaseFragment;
import com.example.taogegou.bean.JsonBean;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.decoration.ItemDecoration;
import com.example.taogegou.ui_second.BuyActivity;
import com.example.taogegou.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_huazhuangpin extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mRefreshLayout;
    private List<JsonBean> mData;
    private ShopAdapter mAdapter;
    private Callback.Cancelable cancelable;
    private int page = 1;
    private GridLayoutManager mManager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    String load = load();
                    String substring = load.substring(load.length() - 1, load.length());
                    if (substring.equals("}")){
                        List<JsonBean> data = new MyJson().getJsonResult(load, "4", "6");
                        mData.addAll(data);
                        mHandler.sendEmptyMessage(1);
                    }else{
                        loadMore(1);
                    }
                }
            }).start();*/
            loadMore(1);
        }
    }

    @Override
    protected View initView() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_food, null);
        getChildView(inflate);
        return inflate;
    }

    private void getChildView(View inflate) {
        mRecycleView = (RecyclerView) inflate.findViewById(R.id.rv_fragment_food);
        mRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.sf_fragment_food_refresh);
    }

    @Override
    protected void initData() {
        super.initData();
        mData = new ArrayList<>();
        mAdapter = new ShopAdapter(getActivity(), mData);
    }


    @Override
    protected void setData() {
        super.setData();
        mManager = new GridLayoutManager(getActivity(), 2);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(mManager);
        mRecycleView.addItemDecoration(new ItemDecoration(10));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    /*刷新数据*/
    @Override
    public void onRefresh() {
        mData.clear();
        loadMore(1);
        mRefreshLayout.setRefreshing(false);
    }

    /*请求网络获取数据的方法*/
    private void loadMore(final int pages) {
        String path = String.format(NetConfig.HOME_DATA, pages, 3);
        RequestParams params = new RequestParams(path);
        cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    if (pages == 1) {
                        mData.clear();
                    }
                    page++;
                    JSONArray array = new JSONArray(result);
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

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "网络错误，请刷新重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /*在页面销毁的时候取消网络访问*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cancelable != null && cancelable.isCancelled()) {
            cancelable.cancel();
        }
    }

    public String load() {
        try {
            FileInputStream inStream = getActivity().openFileInput("taogegou.txt");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            stream.close();
            inStream.close();
            return stream.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
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
        ActivityUtils.switchTo(getActivity(), BuyActivity.class, map);
    }
}
