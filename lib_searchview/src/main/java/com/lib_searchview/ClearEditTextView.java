package com.searchview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/7/20 13:58
 * desc   : 改自： https://blog.csdn.net/u014078990/article/details/79360975
 * version: 1.0
 * project: SearchView
 */
@SuppressLint("AppCompatCustomView")
public class ClearEditTextView extends EditText implements View.OnFocusChangeListener, TextWatcher {
    public Drawable mClearDrawable;
    public ClearEditTextView(Context context) {
        this(context, null);
    }
    public ClearEditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }
    public ClearEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();//初始化
    }
    private void init() {
        mClearDrawable= getResources().getDrawable(R.drawable.ic_action_clear);
        //设置删除按钮的边界
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        //默认隐藏删除按钮
        setClearIcon(false);
        //监听EditText焦点变化，以根据text长度控制删除按钮的显示和隐藏
        setOnFocusChangeListener(this);
        //监听文本内容变化
        addTextChangedListener(this);
    }
    /**
     * 控制EditText右边的文本清除按钮的隐藏和显示
     */
    private void setClearIcon(boolean isShow) {
        //当isShow为true时取mClearDrawable的值，为false时取null
        Drawable drawable= isShow? mClearDrawable: null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], drawable, getCompoundDrawables()[3]);
    }
    /**
     * 获取焦点并且文本长度大于0时候显示清除按钮(右边的叉号)
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            setClearIcon(getText().length()> 0);
        }else{
            setClearIcon(false);
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    /**
     * 文本内容变化时候回调
     * 当文本长度大于0时显示删除按钮，否则隐藏
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        setClearIcon(s.length()> 0);
    }
    @Override
    public void afterTextChanged(Editable s) {
    }
}
