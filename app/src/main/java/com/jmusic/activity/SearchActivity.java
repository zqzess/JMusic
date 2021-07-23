package com.jmusic.activity;

import android.content.Context;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jmusic.R;
import com.jmusic.bean.MusicInfo;
import com.jmusic.config.PlayConfig;
import com.lib_common.bean.C;
import com.lib_common.bean.Constance;
import com.lib_common.base.BaseActivity;
import com.lib_common.bean.MessageEvent;
import com.lib_searchview.bean.MusicPageInfo;

import butterknife.BindView;

/**
 * @author: zqzess
 * 
 * @time: 2021/7/20 8:28
 *
 * @github: https://github.com/zqzess
**/
@Route(path = Constance.ACTIVITY_URL_SEARCH)
public class SearchActivity extends BaseActivity {

    @BindView(R.id.edit_search)
    EditText edit_search;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;

    }

    @Override
    protected void setContentView() {
        overridePendingTransition(R.anim.fading_in, 0);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initViews() {
        edit_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                }
                return false;
            }
        });
    }
    void searchEvent()
    {
        
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.fading_out);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public void onStickyEventBusCome(MessageEvent event) {
    }
}