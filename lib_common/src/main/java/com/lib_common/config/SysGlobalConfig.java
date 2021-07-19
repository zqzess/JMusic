package com.lib_common.config;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.lib_common.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SysGlobalConfig {

    public static void SystemBarColor(Activity activity, boolean blackStatusBarText)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                //在6.0增加了View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR，这个字段就是把状态栏标记为浅色，然后状态栏的字体颜色自动转换为深色
                if (blackStatusBarText){

                    option=option | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }else
                {
                    option=option | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//            window.setNavigationBarColor(Color.TRANSPARENT);
            }
        } else {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
    }
}
