package com.capton.colorfulseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by capton on 2017/8/13.
 */

public class Thumb extends ViewGroup {
    private int mWidth;
    private  Paint mPaint;
  private   Paint mPaint2;
   public ThumbMask maskView;
    public Thumb(Context context,int width,Paint paint,Paint paint2) {
        super(context);
        this.mWidth=width;
        this.mPaint=paint;
        this.mPaint2=paint2;
        setWillNotDraw(false);
        color=mPaint2.getColor();
        mPaint2.setColor(mPaint.getColor());
        this.setScaleX(0.5f);
        this.setScaleY(0.5f);
     }

    public Thumb(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Thumb(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth,mWidth);
    }


     boolean once;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
       if(!once) {
           maskView=new ThumbMask(getContext(),(int) (getMeasuredWidth()*0.9),mPaint,mPaint2);
           maskView.measure(0, 0);
           addView(maskView);

           TextView textView=new TextView(getContext());
           textView.setWidth(mWidth);
           textView.setHeight(mWidth);
           textView.setTextSize((float) (DisplayUtil.px2sp(getContext(),mWidth)*0.5));
           textView.setTextColor(Color.WHITE);
           textView.setShadowLayer(2,1,2,Color.DKGRAY);
           textView.setGravity(Gravity.CENTER);
           addView(textView);

           getChildAt(0).layout((int) (getMeasuredWidth()*0.05),(int) (getMeasuredWidth()*0.05),(int) (getMeasuredWidth()*0.95),(int) (getMeasuredWidth()*0.95));
           getChildAt(1).layout(0,mWidth/8,mWidth,mWidth);

           once=true;
       }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
         canvas.drawCircle(mWidth/2,mWidth/2,mWidth/2,mPaint2);
        canvas.restore();
    }

    int color;

    public void startTrackingTouch(){
        this.setScaleX(1f);
        this.setScaleY(1f);
        setThumbColor(color);
    }

    public void stopTrackingTouche(){

        this.setScaleX(0.5f);
        this.setScaleY(0.5f);
        setThumbColor(mPaint.getColor());
    }

    public void setThumbColor(int color){
        mPaint2.setColor(color);
        postInvalidate();
    }
    public void setThumbColorRes(int colorRes){
        mPaint2.setColor(getResources().getColor(colorRes));
        postInvalidate();
    }
    public void setThumbWrapColor(int color){
        if(maskView!=null) {
            maskView.setThumbWrapColor(color);
            postInvalidate();
        }
    }
    public void setThumbWrapColorRes(int colorRes){
        if(maskView!=null) {
            maskView.setThumbWrapColorRes(getResources().getColor(colorRes));
            postInvalidate();
        }
    }
}
