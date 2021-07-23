package com.jmusic.util;

import android.app.Application;

import com.alex.voice.SPlayer;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lib_common.net.VolleyRequestQueueManager;
import com.lib_common.util.HeadersUtil;

public class MyApplication extends Application {
    private boolean isDebugARouter =true;
    public static String TAG;
    @Override
    public void onCreate() {
        super.onCreate();
        if(isDebugARouter)
        {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
        VolleyRequestQueueManager.getInstance(this);
        TAG = this.getClass().getSimpleName();
        HeadersUtil.headersInit();

        SPlayer.init(this);
        //以下不是必须设置
        //corePoolSize 设置核心下载最大线程,默认2 最大6. 传入小于0或大于6不生效
        //maximumPoolSize 设置最大下载线程数量,默认8,小于corePoolSize则等于corePoolSize,最大数值64,根据机器性能自己选择适当的线程数
        SPlayer.instance().setCorePoolSize(2).setMaximumPoolSize(8);
        //设置缓存文件夹目录和缓存文件夹名称
        //cacheDirPath 默认为 mContext.getExternalCacheDir()
        //cachePath 默认为 "/VoiceCache"
        SPlayer.instance().setCacheDirPath(this.getExternalCacheDir().toString()).setCachePath("/VoiceCache");

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
