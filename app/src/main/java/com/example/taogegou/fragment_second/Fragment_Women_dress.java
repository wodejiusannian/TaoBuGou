package com.example.taogegou.fragment_second;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.example.taogegou.R;
import com.example.taogegou.adapter.BannerAdapter;
import com.example.taogegou.adapter.ShopAdapter;
import com.example.taogegou.base.BaseFragment;
import com.example.taogegou.bean.JsonBean;
import com.example.taogegou.config.NetConfig;
import com.example.taogegou.decoration.ItemDecoration;
import com.example.taogegou.ui_second.BuyActivity;
import com.example.taogegou.ui_third.ModuleActivity;
import com.example.taogegou.utils.ActivityUtils;
import com.example.taogegou.utils.UtilsInternet;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_women_dress extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, View.OnClickListener, UtilsInternet.XCallBack {

    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mRefreshLayout;
    private List<JsonBean> mData, mBanner;
    private ShopAdapter mAdapter;
    private Callback.Cancelable cancelable;
    private RollPagerView mRollPagerView;
    private BannerAdapter bAdapter;
    private static final int JUMP_TIME = 4000;
    private int page = 1;
    private GridLayoutManager mManager;
    private RecyclerViewHeader mHeader;
    private LinearLayout mRecommend, mLow, mFood;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LoadMore(1);
        }
    }

    @Override
    protected View initView() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shop, null);
        getChildView(inflate);
        return inflate;
    }

    private void getChildView(View inflate) {
        mRecycleView = (RecyclerView) inflate.findViewById(R.id.rv_fragment_shop);
        mHeader = (RecyclerViewHeader) inflate.findViewById(R.id.header);
        mRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.sf_fragment_shop_refresh);
        mRollPagerView = (RollPagerView) inflate.findViewById(R.id.rv_shop_banner);
        mRecommend = (LinearLayout) inflate.findViewById(R.id.ll_shop_recommend);
        mLow = (LinearLayout) inflate.findViewById(R.id.ll_shop_low);
        mFood = (LinearLayout) inflate.findViewById(R.id.ll_shop_food);
    }

    @Override
    protected void initData() {
        super.initData();
        mData = new ArrayList<>();
        mBanner = new ArrayList<>();
        mAdapter = new ShopAdapter(getActivity(), mData);
        initRollViewPager();
        UtilsInternet.getInstance().get(NetConfig.BANNER_PATH, null, this);
    }

    private void initRollViewPager() {
        bAdapter = new BannerAdapter(mRollPagerView, mBanner, getActivity());
        mRollPagerView.setAdapter(bAdapter);
        mRollPagerView.setHintView(new ColorPointHintView(getActivity(), Color.parseColor("#ac0000"), Color.WHITE));
        mRollPagerView.setPlayDelay(JUMP_TIME);
    }

    @Override
    protected void setData() {
        super.setData();
        mManager = new GridLayoutManager(getActivity(), 2);
        mRecycleView.setLayoutManager(mManager);
        mRecycleView.setAdapter(mAdapter);
        mHeader.attachTo(mRecycleView, true);
        mRecycleView.addItemDecoration(new ItemDecoration(10));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mRefreshLayout.setOnRefreshListener(this);
        mRollPagerView.setOnItemClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mRecommend.setOnClickListener(this);
        mLow.setOnClickListener(this);
        mFood.setOnClickListener(this);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断最后一个条目是否出现了
                    int last = mManager.findLastVisibleItemPosition();
                    if (last == mData.size() - 1) {
                        //如果出现了 加载下一页数据
                        LoadMore(page);
                    }

                }
            }
        });
    }

    /*刷新数据*/
    @Override
    public void onRefresh() {
        LoadMore(1);
        mRefreshLayout.setRefreshing(false);
    }

    /*请求网络获取数据的方法*/
    private void LoadMore(final int pages) {

        String path = String.format(NetConfig.HOME_DATA, pages, 1);
        Log.i("", "LoadMore: " + path);
        RequestParams params = new RequestParams(path);
        cancelable = x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (pages == 1) {
                    mData.clear();
                }
                page++;
                try {
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
                        bean.setQuan_link(jb.getString("Quan_link"));
                        bean.setGoodsID(jb.getString("GoodsID"));
                        bean.setQuan_id(jb.getString("Quan_id"));
                        mData.add(bean);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), R.string.err_internet, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onItemClick(int position) {
        Map<String, Object> map = new HashMap<>();
        JsonBean jsonBean = mBanner.get(position);
        String type = jsonBean.getType();
        switch (type) {
            case "good":
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
                break;

            default:
                break;

        }
    }

    @Override
    public void onClick(View v) {
        Map<String, Object> map = new HashMap<>();
        switch (v.getId()) {
            case R.id.item_fragment_shop_Content:
                int tag = (int) v.getTag();
                JsonBean jsonBean = mData.get(tag);
                String url = jsonBean.getQuan_link();
                String isTmall = jsonBean.getIsTmall();
                String goodsID = jsonBean.getGoodsID();
                String quan_price = jsonBean.getQuan_price();
                String quan_id = jsonBean.getQuan_id();
                String imagePath = jsonBean.getPic();
                String title = jsonBean.getTitle();
                String org_price = jsonBean.getPrice();
                String base_price = jsonBean.getOrg_Price();
                map.put("url", url);
                map.put("GoodsID", goodsID);
                map.put("IsTmall", isTmall);
                map.put("quan_price", quan_price);
                map.put("quan_id", quan_id);
                map.put("imagePath", imagePath);
                map.put("title", title);
                map.put("base_price", base_price);
                map.put("after_price", org_price);
                ActivityUtils.switchTo(getActivity(), BuyActivity.class, map);
                break;
            case R.id.ll_shop_recommend:
                map.put("moduleName", "小编力荐");
                map.put("url", "http://120.77.46.28:8080/test/servlet/getGoodsRecommended?pageIndex=%d");
                ActivityUtils.switchTo(getActivity(), ModuleActivity.class, map);
                break;
            case R.id.ll_shop_low:
                map.put("moduleName", "9.9包邮");
                map.put("url", "http://120.77.46.28:8080/test/servlet/get9_9Goods?pageIndex=%d");
                ActivityUtils.switchTo(getActivity(), ModuleActivity.class, map);
                break;
            case R.id.ll_shop_food:
                map.put("moduleName", "休闲零食");
                map.put("url", "http://120.77.46.28:8080/test/servlet/getSnacks?pageIndex=%d");
                ActivityUtils.switchTo(getActivity(), ModuleActivity.class, map);
                break;
            default:

                break;
        }

    }

    @Override
    public void onResponse(String result) {

        try {
            JSONObject obj = new JSONObject(result);
            JSONArray array = obj.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JsonBean bean = new JsonBean();
                JSONObject jb = array.getJSONObject(i);
                bean.setType(jb.getString("type"));
                JSONObject goodsInfo = jb.getJSONObject("goodsInfo");
                bean.setTitle(goodsInfo.getString("Title"));
                bean.setPic(goodsInfo.getString("Pic"));
                bean.setIsTmall(goodsInfo.getString("IsTmall"));
                bean.setSales_num(goodsInfo.getString("Sales_num"));
                bean.setOrg_Price(goodsInfo.getString("Org_Price"));
                bean.setPrice(goodsInfo.getDouble("Price") + "");
                bean.setQuan_price(goodsInfo.getString("Quan_price"));
                bean.setQuan_time(goodsInfo.getString("Quan_time"));
                bean.setQuan_link(goodsInfo.getString("Quan_link"));
                bean.setGoodsID(goodsInfo.getString("GoodsID"));
                bean.setQuan_id(goodsInfo.getString("Quan_id"));
                mBanner.add(bean);
            }
            bAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* public String load(){
        try {
            FileInputStream inStream= getActivity().openFileInput("taogegou.txt");
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int length=-1;
            while((length=inStream.read(buffer))!=-1)   {
                stream.write(buffer,0,length);
            }
            stream.close();
            inStream.close();
            return stream.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            return null;
        }
        return null;
    }*/
}
