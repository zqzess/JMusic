package com.jmusic.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.os.Build;
import android.os.Bundle;
import android.widget.TabHost;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.jmusic.R;
import com.jmusic.util.FragmentUtils;
import com.lib_common.bean.Constance;
import com.lib_common.config.SysGlobalConfig;
import com.lib_common.util.TabItem;

import java.util.ArrayList;
import java.util.List;

@Route(path = Constance.ACTIVITY_URL_MAINFRAGMENT)
public class FragmentInitActivity extends AppCompatActivity {

    private List<TabItem> mFragmentList;

    private FragmentTabHost mFragmentTabHost;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysGlobalConfig.SystemBarColor(this,true);
        setContentView(R.layout.activity_fragment_init);
        getSupportActionBar().hide();
        initTabItemData();
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
}