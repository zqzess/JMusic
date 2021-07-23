package com.lib_splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lib_common.bean.Constance;

@Route(path = "/lib_splashscreen/SplashScreenActivity")
public class SplashScreenActivity extends AppCompatActivity {

    private static final int DELAY_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        // 隐藏android系统的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 利用消息处理器实现延迟跳转到主窗口
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 启动登录窗口
                ARouter.getInstance().build(Constance.ACTIVITY_URL_MAINFRAGMENT).withTransition(R.anim.slide_in_from_top, R.anim.slide_out_from_top).navigation(SplashScreenActivity.this);
                // 关闭启动画面
                finish();//启动后销毁自身
            }
        }, DELAY_TIME);
    }
}