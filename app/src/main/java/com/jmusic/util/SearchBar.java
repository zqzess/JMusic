package com.jmusic.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jmusic.R;
/**
 * @author: zqzess
 * 
 * @time: 2021/7/19 20:57
 *
 * @github: https://github.com/zqzess
**/
public class SearchBar extends LinearLayout {
    private TextView tv_icon;
    private TextView tv_hint;
    private LinearLayout linearLayout;
    public SearchBar(Context context) {
        super(context);
    }
    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.searbar,this);
        linearLayout = (LinearLayout) findViewById(R.id.searc_linearlayout);
        tv_icon=(TextView)findViewById(R.id.searchbar_tv_icon);
        tv_hint=(TextView)findViewById(R.id.search_bar_tv_hint);
    }
}
