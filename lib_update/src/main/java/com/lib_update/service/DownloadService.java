package com.lib_update.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.bean.MessageEvent;
import com.lib_common.util.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import static com.lib_common.bean.C.EventCode.BROADCAST_ACTION;
import static com.lib_common.bean.C.EventCode.EXTENDED_DATA_STATUS;

/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/6/3.
 * desc   : 原来使用的广播，改为eventbus,并使用ARouter框架
 * version: 2.0
 * project: TDkankan->JMusic
 */

@Route(path = Constance.SERVICE_URL_DOWNLOADSERVICE)
public class DownloadService extends IntentService {
    private String TAG = "DownloadService";

//    private LocalBroadcastManager mLocalBroadcastManager;

    public DownloadService() {
        super("下载更新服务");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //获取下载地址
        String url = intent.getDataString();
        Log.i(TAG,url);
        //获取DownloadManager对象
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //指定APK缓存路径和应用名称，可在SD卡/Android/data/包名/file/Download文件夹中查看
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "JMusic.apk");
        if(file.exists()&& file.isFile())
        {
            file.delete();
        }
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "JMusic.apk");
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        //设置显示通知栏，下载完成后通知栏自动消失
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
//            //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }else
        {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
        }
        // 显示下载界面
//        request.setVisibleInDownloadsUi(true);
        //设置通知栏标题
        request.setTitle("土豆音乐下载");
        request.setDescription("应用正在下载");
        request.setAllowedOverRoaming(false);
        //获得唯一下载id
        long requestId = downloadManager.enqueue(request);
        //将id放进Intent
        Intent localIntent = new Intent(BROADCAST_ACTION);
        localIntent.putExtra(EXTENDED_DATA_STATUS,requestId);
        //查询下载信息
        DownloadManager.Query query=new DownloadManager.Query();
        query.setFilterById(requestId);
        try{
            boolean isGoging=true;
            while(isGoging){
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch(status){
                        //如果下载状态为成功
                        case DownloadManager.STATUS_SUCCESSFUL:
                            isGoging=false;
                            //使用eventbus发送id
                            EventBusUtil.sendStickyEvent(new MessageEvent(EXTENDED_DATA_STATUS,localIntent));
                            break;
                    }
                }

                if(cursor!=null){
                    cursor.close();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
