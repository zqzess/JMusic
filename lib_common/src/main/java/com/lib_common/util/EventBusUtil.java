package com.lib_common.util;

import com.lib_common.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtil {
    public static void register(Object subscriber) {//注册
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {//注销
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(MessageEvent event) {//发送事件
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(MessageEvent event) {//发送粘性事件
        EventBus.getDefault().postSticky(event);
    }
}
