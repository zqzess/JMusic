package com.jmusic.util;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lib_common.bean.Constance;

public class FragmentUtils {
    public static Fragment getHomeFragment() {
        Fragment fragment = (Fragment) ARouter.getInstance().build(Constance.FRAGMENT_URL_HOME).navigation();
        return fragment;
    }

    public static Fragment getMusicResourceFragment() {
        Fragment fragment = (Fragment) ARouter.getInstance().build(Constance.FRAGMENT_URL_PLAY).navigation();
        return fragment;
    }


    public static Fragment getMeFragment() {
        Fragment fragment = (Fragment) ARouter.getInstance().build(Constance.FRAGMENT_URL_ME).navigation();
        return fragment;
    }

    public static Activity getFragmentInitActivity()
    {
        Activity activity = (Activity) ARouter.getInstance().build(Constance.ACTIVITY_URL_MAINFRAGMENT).navigation();
        return activity;
    }
}
