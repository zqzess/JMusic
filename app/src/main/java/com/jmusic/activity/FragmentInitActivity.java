package com.jmusic.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alex.voice.SPlayer;
import com.alibaba.android.arouter.facade.annotation.Route;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guogali.progressbuttonlibrary.ProgressButton;
import com.jmusic.R;
import com.jmusic.config.PlayConfig;
import com.jmusic.util.FragmentUtils;
import com.lib_common.bean.Constance;
import com.lib_common.config.SysGlobalConfig;
import com.lib_common.util.TabItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = Constance.ACTIVITY_URL_MAINFRAGMENT)
public class FragmentInitActivity extends AppCompatActivity {

    private List<TabItem> mFragmentList;

    private FragmentTabHost mFragmentTabHost;

    @BindView(R.id.main_iv_pic)
    ImageView iv_pic;

    @BindView(R.id.main_btn_play)
    ProgressButton button;

    @BindView(R.id.main_tv_btn_next)
    TextView btn_next;

    @BindView(R.id.main_click)
    TextView btn_click;

    Handler mHandler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysGlobalConfig.SystemBarColor(this,true);
        setContentView(R.layout.activity_fragment_init);
        getSupportActionBar().hide();
        initTabItemData();
        ButterKnife.bind(this);
    }

    private void initTabItemData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new TabItem(
                R.drawable.ic_action_explorer,
                R.drawable.ic_action_explorer_select,
                "探索",
                FragmentUtils.getHomeFragment().getClass(), R.color.courseTable3
        ));

        mFragmentList.add(new TabItem(
                R.drawable.ic_action_play,
                R.drawable.ic_action_play_select,
                "乐库",
                FragmentUtils.getPlayFragment().getClass(), R.color.courseTable3
        ));

        mFragmentList.add(new TabItem(
                R.drawable.ic_action_mine,
                R.drawable.ic_action_mine_select,
                "我的",
                FragmentUtils.getMeFragment().getClass(), R.color.courseTable3
        ));

        mFragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        // 绑定 FragmentManager
        mFragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        // 删除分割线
//        mFragmentTabHost.getTabWidget().setDividerDrawable(null);

        for (int i = 0; i < mFragmentList.size(); i++) {
            TabItem tabItem = mFragmentList.get(i);
            // 创建 tab
            TabHost.TabSpec tabSpec = mFragmentTabHost.newTabSpec(
                    tabItem.getTabText()).
                    setIndicator(tabItem.getTabView(FragmentInitActivity.this));
            // 将创建的 tab 添加到底部 tab 栏中（ @android:id/tabs ）
            // 将 Fragment 添加到页面中（ @android:id/tabcontent ）
            mFragmentTabHost.addTab(tabSpec, tabItem.getFragmentClass(), null);
            // 底部 tab 栏设置背景图片
            //mFragmentTabHost.getTabWidget().setBackgroundResource(R.drawable.bottom_bar);
            mFragmentTabHost.getTabWidget().setBackgroundResource(R.color.courseTable6);

            // 默认选中第一个 tab
            if (i == 0) {
                tabItem.setChecked(true);
            } else {
                tabItem.setChecked(false);
            }
        }

        // 切换 tab 时，回调此方法
        mFragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < mFragmentList.size(); i++) {
                    TabItem tabItem = mFragmentList.get(i);
                    // 通过 tag 检查用户点击的是哪个 tab
                    if (tabId.equals(tabItem.getTabText())) {
                        tabItem.setChecked(true);
                    } else {
                        tabItem.setChecked(false);
                    }
                }
            }
        });
    }

    void initView()
    {
        if(PlayConfig.playFlag!=0)
        {
            String author="";
            try {
                author=PlayConfig.playNow.getArtist();
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            if(author.length()>20)
            {
                author="群星";
            }
            btn_click.setText(PlayConfig.playNow.getName()+" - "+author);
            Glide.with(iv_pic.getContext()).load(PlayConfig.playNow.getAlbumPic()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).crossFade().into(iv_pic);
            if(SPlayer.instance().isPlaying())
            {
                button.setPauseOrPlay(2);/**** 标识播放状态： 1是暂停  ; 2是播放*/
            }else
            {
                button.setPauseOrPlay(1);
            }

            if(PlayConfig.mediaPlayer.getDuration()==0)
            {
                button.setProgressMax(100);
            }
            try {
                button.setProgressValue(PlayConfig.mediaPlayer.getCurrentPosition());
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            mHandler.post(mRunnable);
        }

        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PlayConfig.isPlaying)
                {
                    PlayConfig.playFlag=3;
                }
                ARouter.getInstance().build(Constance.ACTIVITY_URL_PLAYMUSIC).withTransition(R.anim.slide_in_right,R.anim.slide_out_right).navigation();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SPlayer.instance().isPlaying())
                {
                    PlayConfig.isPlaying=false;
                    SPlayer.instance().pause(); //暂停
                    PlayConfig.currentPosition=PlayConfig.mediaPlayer.getCurrentPosition();
                    button.setPauseOrPlay(1);
                }else
                {
                    PlayConfig.isPlaying=true;
                    SPlayer.instance().start(); //继续
                    button.setPauseOrPlay(2);
                    PlayConfig.currentPosition=0;

                }
            }
        });
    }

    //TODO 子线程更新ui
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(PlayConfig.playFlag!=0)
            {
                String author=PlayConfig.playNow.getArtist();
                if(author.length()>20)
                {
                    author="群星";
                }
                btn_click.setText(PlayConfig.playNow.getName()+" - "+author);
                Glide.with(iv_pic.getContext()).load(PlayConfig.playNow.getAlbumPic()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).crossFade().into(iv_pic);
            }
            if(SPlayer.instance().isPlaying())
            {
                button.setPauseOrPlay(2);
            }
            try {
                button.setProgressMax(PlayConfig.mediaPlayer.getDuration());
                button.setProgressValue(PlayConfig.mediaPlayer.getCurrentPosition());
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(mRunnable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }
}