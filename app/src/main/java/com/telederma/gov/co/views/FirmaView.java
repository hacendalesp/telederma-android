package com.telederma.gov.co.views;

/**
 * Created by Daniel Hernández on 6/21/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * A simple view to capture a path traced onto the screen. Initially intended to be used to captures signatures.
 *
 * @author Andrew Crichton
 * @version 0.1
 */
public class FirmaView extends View {

    private Path mPath;
    private Paint mPaint;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private float curX, curY;

    private static final int TOUCH_TOLERANCE = 4;
    private static final int STROKE_WIDTH = 4;

    public FirmaView(Context context) {
        super(context);
        init();
    }

    public FirmaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FirmaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setFocusable(true);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH);
    }

    public void clearSignature() {
        if (mCanvas == null) {
            mCanvas = new Canvas();
            mBitmap = Bitmap.createBitmap(mCanvas.getWidth(), mCanvas.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas.setBitmap(mBitmap);
        } else {
            mCanvas.drawColor(Color.WHITE);
        }

        mPath.reset();
        invalidate();
    }

    public Bitmap getImage() {
        return this.mBitmap;
    }

    public void setImage(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        int bitmapWidth = mBitmap != null ? mBitmap.getWidth() : 0;
        int bitmapHeight = mBitmap != null ? mBitmap.getHeight() : 0;
        //if (bitmapWidth > width && bitmapHeight > height)
          //  return;
        if (bitmapWidth < width)
            bitmapWidth = width;
        if (bitmapHeight < height)
            bitmapHeight = height;
        Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas(newBitmap);
        newCanvas.drawColor(Color.WHITE);
        if (mBitmap != null)
            newCanvas.drawBitmap(mBitmap, 0, 0, null);
        mBitmap = newBitmap;
        mCanvas = newCanvas;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }
        invalidate();
        return true;
    }

    /**
     * ----------------------------------------------------------
     * Private methods
     * *---------------------------------------------------------
     */

    private void touchDown(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        curX = x;
        curY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - curX);
        float dy = Math.abs(y - curY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(curX, curY, (x + curX) / 2, (y + curY) / 2);
            curX = x;
            curY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(curX, curY);
        if (mCanvas == null) {
            mCanvas = new Canvas();
            mCanvas.setBitmap(mBitmap);
        }
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }
}
