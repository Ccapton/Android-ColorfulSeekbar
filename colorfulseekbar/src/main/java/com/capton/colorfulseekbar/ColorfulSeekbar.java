package com.capton.colorfulseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by capton on 2017/8/12.
 */



public class ColorfulSeekbar extends ViewGroup{

    public static final String STYLE_NORMAL="normal";  //正常单色样式
    public static final String STYLE_COLORFUL="colorful"; //双色样式
    public   String style="colorful";

    private ColorfulView colofulView;     //双色View
    private TextView progressView;     // 第二进度条
    private TextView backgroundMaskView;     // 背景罩
    private TextView maskView;         // 进度条白色渐变图层
    private TextView percentView;       //文字显示进度层
    private Thumb thumbView;            //  拖动圆球
    private Paint progressPaint=new Paint();  //颜色一画笔
    private Paint progressPaint2=new Paint();  //颜色二画笔
    private Paint backgroundPaint=new Paint();  //背景画笔
    private Paint thumbPaint=new Paint();  //拖动圆球画笔
    private Paint thumbWrapPaint=new Paint();  //拖动圆球外环画笔

    private  int mHeight;     // 高度
    private  int mWidth;      // 宽度

    private  long progress;     //进度值
    private  long secondProgress;   //第二进度值
    private  long maxProgress=100;  //默认最大进度100
    private  int thumbHeight;       //  拖动圆球高度
    private  int progressBarHeight;  //  进度条高度
    private  int backgroundColor=getResources().getColor(R.color.progressBg);    //背景颜色
    private  int secondProgressColor=getResources().getColor(R.color.secondProgressColor);  //第二进度条颜色
    private  int progressColor=getResources().getColor(R.color.colorAccent);    //进度条颜色一
    private  int progressColor2=getResources().getColor(R.color.ltcolorAccent);  //进度条颜色二
    private  int thumbColor=getResources().getColor(R.color.colorAccent);  //拖动圆球颜色
    private  int thumbWrapColor=getResources().getColor(R.color.white);  //拖动圆球外环颜色
    private  int percentColor= Color.YELLOW;          //进度文字的颜色，默认黄色
    private  int percentShaderColor=Color.DKGRAY;   //进度文字的阴影颜色，默认暗灰色
    private boolean animationOn=true;      //动画开启的标志位

    private ColorfulProgressbar progressBar;

    public ColorfulSeekbar(Context context) {
        this(context,null);
    }

    public ColorfulSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorfulSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        getParameter(context,attrs);
        setClickable(true);
    }

    /**
     * 从xml中获取各个属性
     * @param context
     * @param attrs
     */
    private void getParameter(Context context, AttributeSet attrs){
        if(attrs!=null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorfulSeekbar);
            style = ta.getString(R.styleable.ColorfulSeekbar_style);
            if (!STYLE_NORMAL.equals(style) && !STYLE_COLORFUL.equals(style)) {
                style = STYLE_COLORFUL;  //如果没有在xml中显示设置style，默认使用双色进度条
            }
            progress = ta.getInteger(R.styleable.ColorfulSeekbar_progress, (int)progress);
            secondProgress = ta.getInteger(R.styleable.ColorfulSeekbar_secondProgress,(int)secondProgress);
            maxProgress = ta.getInteger(R.styleable.ColorfulSeekbar_max, (int) maxProgress);
            backgroundColor = ta.getColor(R.styleable.ColorfulSeekbar_backgroundColor, backgroundColor);
            progressColor = ta.getColor(R.styleable.ColorfulSeekbar_progressColor1, progressColor);
            progressColor2 = ta.getColor(R.styleable.ColorfulSeekbar_progressColor2, progressColor2);
            thumbColor = ta.getColor(R.styleable.ColorfulSeekbar_thumbColor, thumbColor);
            thumbWrapColor = ta.getColor(R.styleable.ColorfulSeekbar_thumbWrapColor, thumbWrapColor);
            thumbHeight=ta.getDimensionPixelSize(R.styleable.ColorfulSeekbar_thumbRadius,DisplayUtil.dip2px(context,8))*2;
            progressBarHeight=ta.getDimensionPixelSize(R.styleable.ColorfulSeekbar_progressBarHeight,DisplayUtil.dip2px(context,4));
            animationOn = ta.getBoolean(R.styleable.ColorfulSeekbar_animation,animationOn);
            ta.recycle();
        }
    }

    private void addProgressBar(int width,int height) {
        progressBar=new ColorfulProgressbar(getContext());
        progressBar.setStyle(style);
        progressBar.setWidth(width);
        progressBar.setHeight(height);
        progressBar.setProgress(progress);
        progressBar.setSecondProgress(secondProgress);
        progressBar.setMaxProgress(maxProgress);
        progressBar.setBackgroundColor(backgroundColor);
        progressBar.setProgressColor(progressColor);
        progressBar.setProgressColor2(progressColor2);
        progressBar.setAnimation(animationOn);
        progressBar.showPercentText(false);
        progressBar.measure(0,0);
        addView(progressBar);
    }

    private void addThumb(int measuredHeight, Paint thumbPaint) {
             thumbPaint.setAntiAlias(true);
           thumbWrapPaint.setAntiAlias(true);
             thumbPaint.setColor(thumbColor);
           thumbWrapPaint.setColor(thumbWrapColor);
              thumbView=new Thumb(getContext(),measuredHeight,thumbPaint,thumbWrapPaint);
               thumbView.measure(0,0);
             //  thumbView.setBackgroundResource(R.drawable.thumb_mask);
             thumbView.setThumbColor(thumbColor);
             thumbView.setThumbWrapColor(thumbWrapColor);
               addView(thumbView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        widthSize=widthMode==MeasureSpec.EXACTLY?widthSize:DisplayUtil.dip2px(getContext(),212);
        heightSize=heightMode==MeasureSpec.EXACTLY?heightSize:DisplayUtil.dip2px(getContext(),16);

         /*
        * 当你设置高度大于40dp时，强制高度变为40dp,太高了不美观。
        * */
        int maxHeight=DisplayUtil.dip2px(getContext(),40);
        int minHeight=DisplayUtil.dip2px(getContext(),8);
        if(mHeight>maxHeight) {
            mHeight = maxHeight;
        }
        if(mHeight<minHeight){
            if(mHeight!=0)
            mHeight= minHeight;
        }
        /*
        * 设置高度
        * */
        if(mHeight>0){
            thumbHeight=mHeight;
            heightSize=mHeight;
        } else {
            thumbHeight=thumbHeight>maxHeight?maxHeight:thumbHeight;
            thumbHeight=thumbHeight<minHeight?minHeight:thumbHeight;
            heightSize=heightSize>maxHeight?maxHeight:heightSize;
            heightSize=heightSize<minHeight?minHeight:heightSize;
        }
        if(thumbHeight!=heightSize)
        heightSize=thumbHeight;

        if(progressBarHeight>heightSize) {
            progressBarHeight = heightSize;
        }

        /*
        * 设置宽度
        * */
        if(mWidth>0){
            widthSize=mWidth;
        }
        setMeasuredDimension(widthSize,heightSize);
    }

    boolean once=true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(once) {

            addProgressBar(getMeasuredWidth()-thumbHeight,progressBarHeight);
            addThumb(thumbHeight,thumbPaint);


            getChildAt(0).layout(thumbHeight/2, thumbHeight/2-progressBarHeight/2,
                    getMeasuredWidth() - thumbHeight/2, getMeasuredHeight()/2+progressBarHeight/2);

            getChildAt(1).layout(0, 0,
                    thumbHeight, thumbHeight);
            once=false;
        }
    }

    private float partition;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long progress=progressBar.getProgress();
        long maxProgress=progressBar.getMaxProgress();

        partition=(float) progress/maxProgress;

        thumbView.setTranslationX(partition*progressBar.getMeasuredWidth());


    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener){
        mOnSeekBarChangeListener=onSeekBarChangeListener;
    }

    public interface OnSeekBarChangeListener{

        public void onProgressChanged(ColorfulSeekbar seekBar, int progress, boolean fromUser);


        public void onStartTrackingTouch(ColorfulSeekbar seekBar);


        public void onStopTrackingTouch(ColorfulSeekbar seekBar);
    }


    public int getSecondProgressColor() {
        return progressBar.getSecondProgressColor();
    }

    public void setSecondProgressColor(int secondProgressColor) {
        progressBar.setSecondProgress(secondProgressColor);
    }
    public void setSecondProgressColorRes(int secondProgressColorRes) {
        progressBar.setSecondProgressColorRes(secondProgressColorRes);
    }

    public int getPercentColor() {
        return progressBar.getPercentColor();
    }

    public void setPercentColor(int percentColor) {
        progressBar.setProgressColor(percentColor);
    }

    public void setPercentColorRes(int percentColorRes) {
        progressBar.setPercentColorRes(percentColorRes);
    }

    public int getPercentShadeColor() {
        return progressBar.getPercentShadeColor();
    }

    public void setPercentShaderColor(int percentShadeColor) {
        progressBar.setPercentShaderColor(percentShadeColor);
    }
    public void setPercentShaderColorRes(int percentShaderColorRes) {
        progressBar.setPercentColorRes(percentShaderColorRes);
    }

    public String getStyle() {
        return progressBar.getStyle();
    }

    public void setStyle(String style) {
        progressBar.setStyle(style);
    }

    public int getThumbColor() {
        return thumbColor;
    }

    public void setThumbColor(int thumbColor) {
        this.thumbColor = thumbColor;
        thumbView.setThumbColor(thumbColor);
    }

    public void setThumbColorRes(int thumbColorRes) {
        this.thumbColor = getResources().getColor(thumbColorRes);
        thumbView.setThumbColorRes(thumbColor);
    }

    public int getThumbWrapColor() {
        return thumbWrapColor;
    }

    public void setThumbWrapColor(int thumbWrapColor) {
        this.thumbWrapColor = thumbWrapColor;
        thumbView.setThumbWrapColor(thumbWrapColor);
    }
    public void setThumbWrapColorRes(int thumbWrapColorRes) {
        this.thumbWrapColor = getResources().getColor(thumbWrapColorRes);
        thumbView.setThumbWrapColorRes(thumbWrapColor);
    }

    public int getProgressColor() {
        return progressBar.getProgressColor();
    }

    public void setProgressColor(int progressColor) {
        progressBar.setProgressColor(progressColor);
    }
    public void setProgressColorRes(int progressColorRes) {
        progressBar.setProgressColorRes(progressColorRes);
    }

    public int getProgressColor2() {
        return progressBar.getProgressColor2();
    }

    public void setProgressColor2(int progressColor2) {
        progressBar.setProgressColor2(progressColor2);
    }
    public void setProgressColor2Res(int progressColor2Res) {
        progressBar.setProgressColor2Res(progressColor2Res);
    }

    public void setAnimation(boolean animationOn){
        progressBar.setAnimation(animationOn);
    }

    public long getSecondProgress() {
        return progressBar.getSecondProgress();
    }

    private long tempProgress2;
    public void setSecondProgress(long secondProgress) {
        if(tempProgress2!=progress) {
            tempProgress2 = progress;
            if(mOnSeekBarChangeListener!=null){
                mOnSeekBarChangeListener.onProgressChanged(this, (int) tempProgress2,false);
            }
        }
        progressBar.setSecondProgress(secondProgress);
         invalidate();
    }

    public int getBackgroundColor() {
        return progressBar.getBackgroundColor();
    }

    public void setBackgroundColor(int backgroundColor) {
        progressBar.setBackgroundColor(backgroundColor);
    }

    public void setBackgroundColorRes(int backgroundColorRes) {
        progressBar.setBackgroundColorRes(backgroundColorRes);
    }

    public void setHeight(int height){
        mHeight=height;
    }
    public void setWidth(int width){
        mWidth=width;
    }

    public void setMaxProgress(long progress){
        progressBar.setMaxProgress(progress);
    }

    public long getMaxProgress(){
        return progressBar.getMaxProgress();
    }

   private long tempProgress;
    public void setProgress(long progress){
        if(tempProgress!=progress) {
            tempProgress = progress;
            if(mOnSeekBarChangeListener!=null){
                mOnSeekBarChangeListener.onProgressChanged(this, (int) tempProgress,false);
            }
        }
        progressBar.setProgress(progress);
        postInvalidate();
    }

    public long getProgress(){
        return progressBar.getProgress();
    }

    public void setThumbText(String text){
        ((TextView)thumbView.getChildAt(1)).setText(text);
    }

    float pressX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pressX=event.getX();
                if(pressX>getMeasuredHeight()/2&&pressX<getMeasuredWidth()-getMeasuredHeight()/2){
                    partition=(event.getX()-getMeasuredHeight()/2)/progressBar.getMeasuredWidth();
                }else if(event.getX()<=getMeasuredHeight()/2){
                    partition = 0;
                }else if(event.getX()>=getMeasuredWidth()-getMeasuredHeight()/2){
                    partition = 1;
                }
                progressBar.setProgress((long) (partition * progressBar.getMaxProgress()));
                postInvalidate();
                if(mOnSeekBarChangeListener!=null) {
                    mOnSeekBarChangeListener.onStartTrackingTouch(this);
                    mOnSeekBarChangeListener.onProgressChanged(this, (int) progressBar.getProgress(), true);
                }
                thumbView.startTrackingTouch();
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX()>=getMeasuredHeight()/2&&event.getX()<=getMeasuredWidth()-getMeasuredHeight()/2) {
                    if (event.getX() >getMeasuredHeight() / 2 && event.getX() < getMeasuredWidth() - getMeasuredHeight() / 2) {
                        partition = (event.getX() - getMeasuredHeight() / 2) / progressBar.getMeasuredWidth();
                    }
                }else if(event.getX()>=getMeasuredWidth()-getMeasuredHeight()/2){
                    partition = 1;
                }
                progressBar.setProgress((long) (partition * progressBar.getMaxProgress()));
                postInvalidate();
                if(mOnSeekBarChangeListener!=null) {
                    mOnSeekBarChangeListener.onProgressChanged(this, (int) progressBar.getProgress(), true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(event.getX()>=getMeasuredHeight()/2&&event.getX()<=getMeasuredWidth()-getMeasuredHeight()/2) {
                    if (pressX >getMeasuredHeight() / 2 && pressX < getMeasuredWidth() - getMeasuredHeight() / 2) {
                        partition = (event.getX() - getMeasuredHeight() / 2) / progressBar.getMeasuredWidth();
                    }
                }else if(event.getX()>=getMeasuredWidth()-getMeasuredHeight()/2){
                    partition = 1;
                }
                progressBar.setProgress((long) (partition * progressBar.getMaxProgress()));
                postInvalidate();
                if(mOnSeekBarChangeListener!=null) {
                    mOnSeekBarChangeListener.onStopTrackingTouch(this);
                    mOnSeekBarChangeListener.onProgressChanged(this, (int) progressBar.getProgress(), true);
                }
                thumbView.stopTrackingTouche();
                setThumbText("");
                break;
        }
        return true;
    }


}
