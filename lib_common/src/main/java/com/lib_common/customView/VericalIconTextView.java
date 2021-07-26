package com.lib_common.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lib_common.R;

/**
 * @author ZQZESS
 * @date 2021/5/13.
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 * desc:修改于2021/7/26 14:30
 * version: 2.0
 * project: TDkankan->JMusic
 */
public class VericalIconTextView extends LinearLayout {
    private Context context;
    private Drawable viewImage;
    private String viewText;
    private int viewTextColor;
    private float viewTextSize;
    private String viewText2;
    private int viewTextColor2;
    private float viewTextSize2;
    TextView tv2;
    TextView tv;
    ImageView iv;

    public VericalIconTextView(Context context) {
        super(context,null);
    }

    public VericalIconTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VericalIconTextView);
        viewImage = typedArray.getDrawable(R.styleable.VericalIconTextView_view_image);
        viewText = typedArray.getString(R.styleable.VericalIconTextView_view_text).toString();
        viewTextColor = typedArray.getColor(R.styleable.VericalIconTextView_view_text_color, 0);
        viewTextSize = typedArray.getDimension(R.styleable.VericalIconTextView_view_text_size, 12);
        viewTextColor2 = typedArray.getColor(R.styleable.VericalIconTextView_view_text_color2, 0);
        viewTextSize2 = typedArray.getDimension(R.styleable.VericalIconTextView_view_text_size2, 8);
        initView();
        typedArray.recycle();
    }
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.verical_icontextview, this, true);
        iv = (ImageView) view.findViewById(R.id.imageview_view_image_with_text);
        tv = (TextView) view.findViewById(R.id.textview_view_image_with_text);
        tv2 = (TextView) view.findViewById(R.id.textview_view_image_with_text2);
        iv.setImageDrawable(viewImage);
        tv.setText(viewText);
        tv.setTextColor(viewTextColor);
        tv.setTextSize(viewTextSize);
        tv2.setTextColor(viewTextColor2);
        tv2.setTextSize(viewTextSize2);
    }

    public String getViewText2() {
        return viewText2;
    }

    public void setViewText2(String viewText2) {
        this.viewText2 = viewText2;
        tv2.setText(viewText2);
    }
}
