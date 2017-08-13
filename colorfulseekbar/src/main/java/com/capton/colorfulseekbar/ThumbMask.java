package com.capton.colorfulseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by capton on 2017/8/13.
 */

public class ThumbMask extends View {
   private int mWidth;
    private Paint mPaint;
    private Paint mPaint2;
    public ThumbMask(Context context, int width,Paint paint, Paint paint2) {
        super(context);
        this.mWidth=width;
        this.mPaint2=paint2;
        this.mPaint=paint;
        setWillNotDraw(false);
     }

    public ThumbMask(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThumbMask(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth,mWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
         canvas.drawCircle(mWidth/2,mWidth/2,mWidth/2,mPaint);
    }

    public void setThumbWrapColor(int color){
        mPaint.setColor(color);
        postInvalidate();
    }
    public void setThumbWrapColorRes(int colorRes){
        mPaint.setColor(getResources().getColor(colorRes));
        postInvalidate();
    }

}
