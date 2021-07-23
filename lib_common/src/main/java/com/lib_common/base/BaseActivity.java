package com.lib_common.base;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Explode;

import androidx.annotation.Nullable;

import com.lib_common.bean.MessageEvent;
import com.lib_common.config.SysGlobalConfig;
import com.lib_common.util.ActivityUtils;
import com.lib_common.util.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
        initViews();
        SysGlobalConfig.SystemBarColor(this,true);
        ActivityUtils.addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


    // 设置界面视图
    protected abstract void setContentView();
    //控件初始化
    protected abstract void initViews();

    //eventbus
    //默认不订阅eventbus事件
    protected boolean isRegisterEventBus() {
        return false;
    }

    // POSTING：默认，表示事件处理函数的线程跟发布事件的线程在同一个线程。
    // MAIN：表示事件处理函数的线程在主线程(UI)线程，因此在这里不能进行耗时操作。
    // BACKGROUND：表示事件处理函数的线程在后台线程，因此不能进行UI操作。如果发布事件的线程是主线程(UI线程)，那么事件处理函数将会开启一个后台线程，如果果发布事件的线程是在后台线程，那么事件处理函数就使用该线程。
    // ASYNC：表示无论事件发布的线程是哪一个，事件处理函数始终会新建一个子线程运行，同样不能进行UI操作。

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(MessageEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(MessageEvent event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(MessageEvent event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(MessageEvent event) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
        ActivityUtils.removeActivity(this);
    }
}
