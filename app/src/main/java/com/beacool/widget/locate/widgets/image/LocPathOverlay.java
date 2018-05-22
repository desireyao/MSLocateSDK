package com.beacool.widget.locate.widgets.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.widget.FrameLayout;


import com.marslocate.log.SDKLogTool;

import java.util.List;

/**
 * Package com.beacool.widget.locate.widgets.image.
 * Created by JoshuaYin on 2017/11/13.
 * <p>
 * Description:
 */

public class LocPathOverlay extends FrameLayout {
    private static final String TAG = "LocPathOverlay";

    private Context mContext;

    private List<PathPoint> mListPoint;
    private int pathColor;

    LocPathOverlay(Context context, List<PathPoint> pathList, int pathColor) {
        super(context);
        setBackgroundColor(0x00ffffff);
        setWillNotDraw(false);
        setWillNotCacheDrawing(false);
        SDKLogTool.LogV(TAG, "LocPathOverlay--->");
        this.mListPoint = pathList;
        this.pathColor = pathColor;
        setId(View.generateViewId());
        init(context);
    }

    private int mPosSize = 36;

    private void init(Context context) {
        this.mContext = context;

        if (mListPoint == null || mListPoint.isEmpty())
            return;

//        for (PositionView point : mListPoint) {
//            SDKLogTool.LogV(TAG, "init--->addView");
//            addView(point, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mPosSize));
//        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        SDKLogTool.LogV(TAG, "onLayout--->[left]:" + left + " [top]:" + top
                + " [right]:" + right + " [bottom]:" + bottom);
        super.onLayout(changed, left, top, right, bottom);
        int len = getChildCount();
        SDKLogTool.LogV(TAG, "onLayout--->[childView]:" + len
                + " [left]:" + left + " [top]:" + top
                + " [right]:" + right + " [bottom]:" + bottom);
//        if (len == 0) {
//            for (PositionView point : mListPoint) {
//                SDKLogTool.LogV(TAG, "init--->addView");
//                addView(point, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mPosSize));
//            }
//            return;
//        }

        for (int i = 0; i < len; i++) {
            View child = getChildAt(i);

            if (null == child)
                continue;

            if (child instanceof PositionView) {
                PositionView position = (PositionView) child;
                SDKLogTool.LogV(TAG, "onLayout--->[point name]:" + position.getmUserID()
                        + " [point MeasuredWidth]:" + position.getMeasuredWidth()
                        + " [point Height]:" + position.getHeight()
                        + " [point left]:" + position.getmPosition().getmLeft()
                        + " [point top]:" + position.getmPosition().getmTop()
                        + " [point right]:" + position.getmPosition().getmRight()
                        + " [point bottom]:" + position.getmPosition().getmBottom());
                int l = (int) position.getmPosition().getmLeft() - position.getMeasuredWidth() / 2;
                int t = (int) (position.getmPosition().getmTop()
                        - (position.getMeasuredHeight() - position.getPosAnchorMarginBottom()));
                int r = l + position.getMeasuredWidth();
                int b = t + position.getMeasuredHeight();
                position.layout(l, t, r, b);
            }
        }
    }

    private Paint mPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        SDKLogTool.LogV(TAG, "onDraw--->");
        super.onDraw(canvas);
        if (canvas == null)
            return;

        if (mPaint == null)
            mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5.f);
        mPaint.setColor(pathColor);

        Path path = new Path();
        for (int i = 0, len = mListPoint.size(); i < len; i++) {
            PathPoint pos = mListPoint.get(i);
            if (i == 0) {
                path.moveTo(new Float(pos.posX), new Float(pos.posY));
                continue;
            }

            path.lineTo(new Float(pos.posX), new Float(pos.posY));
        }

        canvas.drawPath(path, mPaint);
    }
}
