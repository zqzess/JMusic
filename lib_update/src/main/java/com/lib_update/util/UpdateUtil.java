package com.lib_update.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.bean.ErrCode;
import com.lib_common.bean.MessageEvent;
import com.lib_common.bean.NetString;
import com.lib_common.net.HttpRequestManage;
import com.lib_common.util.EventBusUtil;
import com.lib_update.service.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/7/23 8:43
 * desc   :
 * version: 1.0
 * project: JMusic
 */
public class UpdateUtil {
    private static AlertDialog.Builder mDialog;
    public static void getNewVersion(Context context,String versionName)
    {

        try {
//            loadingDialog.setMessage("");
//            loadingDialog.show();
            HttpRequestManage.getJSONObject(NetString.lastestVerUrl, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject  jsonObject) {
                    try {
                        String downurl;
                        String verName=jsonObject.getString("tag_name");
                        String updateinfo=jsonObject.getString("body");
                        if (jsonObject.getJSONArray("assets") != null && jsonObject.getJSONArray("assets").length() > 0) {
                            JSONArray Array = jsonObject.getJSONArray("assets");
                            for (int i = 0; i < Array.length(); i++) {
                                JSONObject object = Array.getJSONObject(i);
                                downurl=object.getString("browser_download_url");
//                                            Log.d("downurl",verName+"->"+downurl);
//                                loadingDialog.dismiss();
                                if (verName.equals(versionName))
                                {
                                    Toast.makeText(context,"当前已是最新版本",Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    UpdateTipDialog(context,verName,downurl,updateinfo);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, ErrCode.NEWVERGETFAILED,Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void UpdateTipDialog(final Context context, final String verName, final String downUrl, final String updateinfo)
    {
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("土豆音乐又更新咯！");
        mDialog.setMessage("最新版本: v"+verName+"\n"+updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent serviceIntent = new Intent(context, DownloadService.class);
                serviceIntent.setData(Uri.parse(downUrl));
                context.startService(serviceIntent);
//                EventBusUtil.sendStickyEvent(new MessageEvent(C.EventCode.URLSENDTOSERVICE,serviceIntent));
//                ARouter.getInstance().build(Constance.SERVICE_URL_DOWNLOADSERVICE).navigation();
            }
        });
        //设置反面按钮
        mDialog.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setCancelable(false).create().show();
    }
}
