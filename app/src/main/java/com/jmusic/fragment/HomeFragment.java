package com.jmusic.fragment;

import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.alibaba.android.arouter.facade.annotation.Route;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jmusic.R;
import com.jmusic.adapter.RecyclerViewAdapter;
import com.jmusic.bean.MusicInfo;


import com.jmusic.util.HeadersUtil;
import com.jmusic.net.HttpRequest;
import com.lib_common.bean.Constance;
import com.jmusic.bean.NetString;
import com.lib_common.net.HttpRequestManage;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Route(path = Constance.FRAGMENT_URL_HOME)
public class HomeFragment extends Fragment {
    RecyclerViewAdapter adapter;

    RecyclerView recyclerView;

    Map<String, String> headers=new HashMap<String,String>();
    Activity activity;
    private ArrayList<MusicInfo> dataList = new ArrayList<>();
    private ExecutorService executorService;
    SmartRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity=this.getActivity();
        executorService = Executors.newCachedThreadPool();
        refreshLayout=(SmartRefreshLayout)view.findViewById(R.id.recyclerview_refresh);
        initView(view);
        loadMoreEvent();
        return view;
    }
    void initView(View view)
    {
        recyclerView= (RecyclerView)view.findViewById(R.id.pullLoadMoreRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL));//列数，方向
//        adapter = new RecyclerViewAdapter(activity,dataList);
        adapter = new RecyclerViewAdapter(activity);
        if(dataList.isEmpty()) {
            doAsyncCode();
        }else
        {
            adapter.replaceAll(dataList);
        }
        recyclerView.setAdapter(adapter);
    }

    void loadMoreEvent()
    {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new ClassicsFooter(activity).setSpinnerStyle(SpinnerStyle.FixedBehind));
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                HttpRequestManage.jsonRequest(NetString.musicRecommend, GlobalConfig.headers, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        dataList=HttpRequest.getData(response);
//                        adapter.addData(adapter.getItemCount(), dataList);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        SuperToast.create(activity, ErrCode.NETERRCODE, Style.DURATION_SHORT).show();
//                    }
//                });
                doAsyncCode();
                refreshLayout.finishLoadMore(2000/*,true*/);//传入false表示加载失败
            }
        });

    }

    private void doAsyncCode() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // 执行你的耗时操作代码
                HttpRequestManage.gzipRequest(NetString.musicRecommend, HeadersUtil.MUSICGZIP, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(dataList.isEmpty())
                        {
                            dataList=HttpRequest.getMusicInfoData(jsonObject);
                            adapter.replaceAll(dataList);
                        }else
                        {
                            dataList.addAll(HttpRequest.getMusicInfoData(jsonObject));
                            adapter.addData(adapter.getItemCount(),HttpRequest.getMusicInfoData(jsonObject));
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity,"网络连接错误",Toast.LENGTH_SHORT).show();
                    }
                });
                doOnUiCode();
            }
        });
    }

    private void doOnUiCode() {
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {
                // 更新你的UI
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (!executorService.isShutdown()) {
//            executorService.shutdownNow();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
