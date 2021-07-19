package com.lib_common.net;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

public class HttpRequestManage {
    public static void getGsonParse(String links, Class mClass, Map<String, String> headers, Response.Listener mlistener)
    {
        GsonRequest  request=new GsonRequest(Request.Method.GET,links, mClass,headers,mlistener,null);
        VolleyRequestQueueManager.mRequestQueue.add(request);
    }
    public static void stringRequest(String links, Map<String, String> headers, Response.Listener<String> mlistener,Response.ErrorListener errorListener)
    {
        StringRequest stringRequest =new StringRequest(Request.Method.GET,links,headers,mlistener,null);
        VolleyRequestQueueManager.mRequestQueue.add(stringRequest);
    }
    public static void jsonRequest(String links, Map<String, String> headers, Response.Listener<JSONObject> mlistener, Response.ErrorListener errorListener)
    {
        JsonRequset requset=new JsonRequset(Request.Method.GET,links,headers,null,mlistener,errorListener);
        VolleyRequestQueueManager.mRequestQueue.add(requset);
    }
    public static void gzipRequest(String links, Map<String, String> headers, Response.Listener<String> mlistener, Response.ErrorListener errorListener)
    {
        GZipRequest request=new GZipRequest(Request.Method.GET,links,headers,mlistener,errorListener);
        VolleyRequestQueueManager.mRequestQueue.add(request);
    }
}
