package com.a.mycamera.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.a.mycamera.Utils;

public class GridLineView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint=new Paint();


    public GridLineView(Context context) {
        this(context, null);
    }

    public GridLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCanvas = new Canvas();
    }

    public void clear() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.TRANSPARENT);
        mCanvas.setBitmap(mBitmap);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        clear();

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(Utils.isGrid){
            int screenWidth = Utils.curW;
            int screenHeight = Utils.curH;

            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(3);
            mPaint.setColor(Color.argb(255, 255, 255, 255));

            canvas.drawLine((screenWidth/3)*2,0,(screenWidth/3)*2,screenHeight,mPaint);
            canvas.drawLine((screenWidth/3),0,(screenWidth/3),screenHeight,mPaint);
            canvas.drawLine(0,(screenHeight/3)*2,screenWidth,(screenHeight/3)*2,mPaint);
            canvas.drawLine(0,(screenHeight/3),screenWidth,(screenHeight/3),mPaint);
        }
    }


}
