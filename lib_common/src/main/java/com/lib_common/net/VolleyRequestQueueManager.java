package com.lib_common.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.RequestTask;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueueManager {
//单例模式
    private static VolleyRequestQueueManager instance;
    private static Context mContext;
    public static RequestQueue mRequestQueue;

    public VolleyRequestQueueManager(Context context) {
        mContext=context;
        mRequestQueue=getRequestQueue();
    }

    public static synchronized VolleyRequestQueueManager getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyRequestQueueManager(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    //取消任务
    public static void cancelRequest(Object tag){
        mRequestQueue.cancelAll(tag);
    }
}
