package com.jmusic.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jmusic.bean.MusicInfo;
import com.jmusic.util.HeadersUtil;
import com.lib_common.cache.ACache;
import com.lib_common.net.HttpRequestManage;
import com.lib_common.util.Base64Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class HttpRequest {

    public static ArrayList netGetMusicInfo(Context context,String url,String errMsg)
    {
        ArrayList<MusicInfo> dataList = new ArrayList<>();

        HttpRequestManage.jsonRequest(url, HeadersUtil.MUSICINFO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg=response.getString("msg");
                    JSONObject data=response.getJSONObject("data");
                    if (data.getJSONArray("resourceList") != null && data.getJSONArray("resourceList").length() > 0) {
                        JSONArray Array = data.getJSONArray("resourceList");
                        for (int i = 0; i < Array.length(); i++) {
                            JSONObject object = Array.getJSONObject(i);
                            long id=object.getLong("id");
                            String name=object.getString("name");
                            long albumId=object.getLong("albumId");
                            String album=object.getString("album");
                            String albumPic=object.getString("albumPic");
                            String albumPic120=object.getString("albumPic120");
                            String artist=object.getString("artist");
                            long artistId=object.getLong("artistId");
                            String artistPic=object.getString("artistPic");
                            int duration=object.getInt("duration");
                            String releaseDate=object.getString("releaseDate");
                            int isMv=object.getInt("isMv");
                            Log.d("debug-data",id+"  "+name+"  "+albumPic);
                            MusicInfo musicInfo=new MusicInfo(id,name,albumId,album,albumPic,albumPic120,artist,artistId,artistPic,duration,releaseDate,isMv);
                            dataList.add(musicInfo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
            }
        });
        return dataList;
    }

    public static ArrayList getMusicInfoData(JSONObject response)
    {
        ArrayList<MusicInfo> dataList = new ArrayList<>();
        try{
            JSONObject data=response.getJSONObject("data");
            if (data.getJSONArray("resourceList") != null && data.getJSONArray("resourceList").length() > 0) {
                JSONArray Array = data.getJSONArray("resourceList");
                for (int i = 0; i < Array.length(); i++) {
                    JSONObject object = Array.getJSONObject(i);
                    long id = object.getLong("id");
                    String name = object.getString("name");
                    long albumId = object.getLong("albumId");
                    String album = object.getString("album");
                    String albumPic = object.getString("albumPic");
                    String albumPic120 = object.getString("albumPic120");
                    String artist = object.getString("artist");
                    long artistId = object.getLong("artistId");
                    String artistPic = object.getString("artistPic");
                    int duration = object.getInt("duration");
                    String releaseDate = object.getString("releaseDate");
                    int isMv = object.getInt("isMv");
                    Log.d("debug-data", id + "  " + name + "  " + albumPic);
                    MusicInfo musicInfo = new MusicInfo(id, name, albumId, album, albumPic, albumPic120, artist, artistId, artistPic, duration, releaseDate, isMv);
                    dataList.add(musicInfo);
                }
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return dataList;
    }

    public static void netGetLyrc(Context context,String url,long musicId,String errMsg)
    {
        HttpRequestManage.jsonRequest(url, HeadersUtil.MUSICINFO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg=response.getString("msg");
                    JSONObject data=response.getJSONObject("data");
                    String lyrcBase64=data.getString("content");
                    String lyrcContent;
                    ACache mCache=ACache.get(context);
                    if(!lyrcBase64.isEmpty())
                    {
                        lyrcContent=Base64Utils.decodeToString(lyrcBase64);
//                        SharedPreferencesUtil.put(context,"id&"+musicId,lyrcContent,"id&"+musicId);
                        mCache.put("lyrc&"+musicId,musicId);
                        saveToFile(lyrcContent,context,musicId);
//                        Log.d("歌词","歌曲id:"+musicId+"\n"+lyrcContent);
                    }else
                    {
                        lyrcContent="纯音乐,暂无歌词";
//                        SharedPreferencesUtil.put(context,"id&"+musicId,lyrcContent,"id&"+musicId);
                        mCache.put("lyrc&"+musicId,musicId);
                        saveToFile(lyrcContent,context,musicId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static  void saveToFile(String inputText,Context context,long id){
        FileOutputStream fos =null;
        BufferedWriter writer = null;

        try {
            fos = context.openFileOutput("id&"+id+".lrc", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(inputText);

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (null != writer){
                    writer.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    public static void netGetAudioUrl(Context context,String url,long musicId,String errMsg)
    {
        HttpRequestManage.jsonRequest(url, HeadersUtil.MUSICURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg=response.getString("msg");
                    JSONObject data=response.getJSONObject("data");
                    String audioUrl=data.getString("audioUrl");
                    ACache mCache=ACache.get(context);
                    if(!audioUrl.isEmpty())
                    {
                        mCache.put("audiourl&"+musicId,audioUrl);
                        Log.d("歌曲链接",audioUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
            }
        });

    }

}
