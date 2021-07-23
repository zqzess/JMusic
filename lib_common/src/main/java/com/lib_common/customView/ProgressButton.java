package com.lib_common.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.lib_common.R;


/**
 * author : zqzess
 * github : https://github.com/zqzess
 * date   : 2021/7/20 9:56
 * desc   : 改自 https://github.com/galibujianbusana/progressBtn1 项目
 * version: 2.0
 * project: JMusic
 */
public class ProgressButton extends View {


    private static final String TAG = "ProgressButton";
    /*** view 环形的底色*/
    private int bgColor;
    /**** view 进度颜色 */
    private int progressColor;
    /*** view 的宽高，(宽高应当相等)*/
    private float viewHeight, viewWidth;
    /*** view 圆环的宽度*/
    private float viewRoundWidth;

    /***中心部分的颜色*/
    private int centerColor;

    /*** view 的进度值，初始为0*/
    private int progressValue=0;
    /*** view 的进度最大值,默认为100 */
    private int progressMax=100;

    /**** 画笔*/
    private Paint paint;

    /**** 标识播放状态： 1是暂停  ; 2是播放*/
    private  int pauseOrPlay=1;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs, int deStyleAttr) {
        super(context, attrs, deStyleAttr);
        paint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);
        bgColor = array.getColor(R.styleable.ProgressButton_bgColor, Color.GRAY);
        progressColor = array.getColor(R.styleable.ProgressButton_progressColor, Color.RED);
        viewHeight = array.getDimension(R.styleable.ProgressButton_viewHeight, 20);
        viewWidth = array.getDimension(R.styleable.ProgressButton_viewWidth, 20);
        viewRoundWidth = array.getDimension(R.styleable.ProgressButton_viewRoundWidth, 2);
        centerColor=array.getColor(R.styleable.ProgressButton_centerColor, Color.RED);
        array.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*** 画外层的圆环*/
        canvas.save();
        int centre = getWidth() / 2;
        int radius = (int) (centre - viewRoundWidth/2);
        paint.setColor(bgColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(viewRoundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环
        canvas.save();
        /***画进度层颜色*/
        paint.setColor(progressColor);
        paint.setStrokeWidth(viewRoundWidth);
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);
        try {
            canvas.drawArc(oval, -90, 360 * progressValue / progressMax, false, paint);
        }catch (Exception e)
        {
            canvas.drawArc(oval, -90, 360 * progressValue / 0, false, paint);
        }


        /*****绘制中心的暂停，播放标识*/
        int x=centre*2;
        if(pauseOrPlay==1){
            Path path = new Path();
            path.moveTo(3*x/8,3*x/8);
            path.lineTo(3*x/8,5*x/8);
            path.lineTo(5*x/8,4*x/8);
            paint.setColor(centerColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(1);
            canvas.drawPath(path,paint);
        }else if(pauseOrPlay==2){
            RectF rectF=new RectF(3*x/8,3*x/8,5*x/8,5*x/8);
            paint.setColor(centerColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(1);
            canvas.drawRect(rectF,paint);
        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        if(progressValue<0){
            throw new IllegalArgumentException("progress not less than 0");
        }else if(progressValue>progressMax){
            this.progressValue=progressMax;
        }else{
            this.progressValue=progressValue;
        }

        postInvalidate();
    }

    public int getProgressMax() {
        return progressMax;
    }

    public void setProgressMax(int progressMax) {
        this.progressMax = progressMax;
    }

    public int getPauseOrPlay() {
        return pauseOrPlay;
    }

    public void setPauseOrPlay(int pauseOrPlay) {
        this.pauseOrPlay = pauseOrPlay;
        postInvalidate();
    }

    public int getCenterColor() {
        return centerColor;
    }

    public void setCenterColor(int centerColor) {
        this.centerColor = centerColor;
        postInvalidate();
    }
}
