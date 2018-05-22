package com.beacool.widget.locate.widgets.image;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.beacool.widget.exception.AddViewException;
import com.beacool.widget.models.MapAnimData;
import com.marslocate.log.SDKLogTool;

import java.util.List;

/**
 * Package com.beacool.widget.install.widgets.
 * Created by JoshuaYin on 16/2/14.
 * Company Beacool IT Ltd.
 * <p/>
 * Description:
 */
public class LocViewLayout extends FrameLayout {
    private static final String TAG = "LocViewLayout";

    private Context mContext;
    private LocMapLayout mMapLayout;

    private float mCenterX = 0.f, mCenterY = 0.f;

    public LocViewLayout(Context context) {
        super(context);
        mMapLayout = new LocMapLayout(context);
        init(context);
    }

    public LocViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMapLayout = new LocMapLayout(context, attrs);
        init(context);
    }

    public LocViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMapLayout = new LocMapLayout(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setBackgroundColor(0xffffffff);
        mScaleGestureListener = new BeacoolScalGestureListener();
        mScaleGestureDetector = new ScaleGestureDetector(context, new BeacoolScalGestureListener());

        mMapLayout.setmOnLayoutCallback(mMapOnLayoutCallback);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMapLayout.setId(View.generateViewId());
        addView(mMapLayout, params);
    }

    private LocMapLayout.OnMapLayoutListener mMapOnLayoutCallback = new LocMapLayout.OnMapLayoutListener() {
        @Override
        public void onMapLayout() {
            mFitScreenScale = mMapLayout.updateMapAnimData(scaleValue, getAbsoluteX(), getAbsoluteY());
            SCALE_MIN = Math.min(1.f, Math.max(mFitScreenScale, 0.5f));
            SCALE_MAX = 3.f;
            SDKLogTool.LogE_DEBUG(TAG, "[mFitScreenScale]:" + mFitScreenScale
                    + " [SCALE_MIN]:" + SCALE_MIN + " [SCALE_MAX]:" + SCALE_MAX);
        }
    };

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mCenterX = getWidth() / 2.f;
        mCenterY = getHeight() / 2.f;

        final int len = getChildCount();

        for (int i = 0; i < len; i++) {
            View child = getChildAt(i);
            if (null == child)
                continue;

            child.layout(left, top, right, bottom);
        }
    }

    public boolean setMap(Bitmap bmpMap, int mapWidth, int mapHeight, float mapScale) {
//        removeAllViews();
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        boolean flag = mMapLayout.setMap(bmpMap, mapWidth, mapHeight, mapScale);
//        if(!flag)
//            return flag;
//        mMapLayout.setId(View.generateViewId());
//        addView(mMapLayout,params);
        return flag;
    }

    public void deletePosition(int viewID) {
        if (mMapLayout != null)
            mMapLayout.deletePosition(viewID);
    }

    public void deletePosition(String userID) {
        if (mMapLayout != null)
            mMapLayout.deletePosition(userID);
    }

    public void changeImage(int viewID, int flag) {
        if (mMapLayout != null)
            mMapLayout.changeImage(viewID, flag);
    }

    public void clearAllPositions() {
        if (mMapLayout != null)
            mMapLayout.clearAllPositions();
    }

    public void addPositions(List<PositionView> beacons) throws AddViewException {
        if (null == mMapLayout || null == beacons)
            return;

        mMapLayout.addPositions(beacons);
    }

    public void addPosition(PositionView beacon) throws AddViewException {
        if (null == mMapLayout || null == beacon)
            return;

        mMapLayout.addPosition(beacon);
    }

    private float oldX = -1, oldY = -1;
    private float scaleValue = 1.f;
    private float SCALE_MAX = 2.f, SCALE_MIN = 0.5f;
    private boolean isStartScale = false;

    private float scaleFactor = 1.f;
    private float mFitScreenScale = 1.f;
    private ScaleGestureDetector mScaleGestureDetector;
    private BeacoolScalGestureListener mScaleGestureListener;

    private class BeacoolScalGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float dlt = detector.getScaleFactor();
            scaleFactor *= dlt;
//            SDKLogTool.LogE_DEBUG(TAG, "[dlt]:" + dlt + " [scaleFactor]:" + scaleFactor + " [scaleValue]:" + scaleValue);
            scaleFactor = Math.max(SCALE_MIN, Math.min(scaleFactor, SCALE_MAX));

            zoomView(scaleFactor);

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
//            LogTool.LogD(TAG, "onScaleBegin--->");
            isStartScale = true;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
//            LogTool.LogD(TAG, "onScaleEnd--->");
        }
    }


    public void reset() {
        oldX = -1;
        oldY = -1;
        scaleFactor = 1.f;
        isStartScale = false;
        zoomView(1.f);

        mMapLayout.clearAllPositions();
        moveToCenter();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int fingerCnt = event.getPointerCount();

        if (!mMapLayout.hasAddMapView())
            return false;

        if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN && 1 == fingerCnt && !isStartScale) {
            SDKLogTool.LogD(TAG, "1 Finger down");
            oldX = event.getRawX();
            oldY = event.getRawY();

            return true;
        } else if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE && 1 == fingerCnt && !isStartScale) {
            float dx = event.getRawX() - oldX;
            float dy = event.getRawY() - oldY;

            float mapX = mMapLayout.getX();
            float mapY = mMapLayout.getY();

            float nx = getAbsoluteX();
            float ny = getAbsoluteY();

            float nextX = mapX + dx;
            float nextY = mapY + dy;

            MapAnimData data = mMapLayout.getCanMoveArea(scaleValue, nx, ny);
            float absMapX = data.absMapX;
            float absMapY = data.absMapY;
            float absMapR = absMapX + data.absMapWidth;
            float absMapB = absMapY + data.absMapHeight;

            float dw = absMapX - mapX;
            float dh = absMapY - mapY;

            boolean isXborder = false;
            boolean isYborder = false;


            if (dx > 0 && absMapX >= mCenterX) {
                mMapLayout.setX(mCenterX - dw);
                isXborder = true;
            } else if (dx <= 0 && absMapR <= mCenterX) {
                mMapLayout.setX(mCenterX - mMapLayout.getWidth() + dw);
                isXborder = true;
            }
//            else if (dx > 0 && absMapR <= mCenterX) {
//                mapX = mCenterX - mMapLayout.getWidth() + dw;
//                nextX += mapX;
//            } else if (dx <= 0 && absMapX >= mCenterX) {
//                mapX = mCenterX - dw;
//                nextX += mapX;
//            }

            if (dy > 0 && absMapY >= mCenterY) {
                mMapLayout.setY(mCenterY - dh);
                isYborder = true;
            } else if (dy <= 0 && absMapB <= mCenterY) {
                mMapLayout.setY(mCenterY - mMapLayout.getHeight() + dh);
                isYborder = true;
            }
//            else if (dy > 0 && absMapB <= mCenterY) {
//                mapY = mCenterY - mMapLayout.getHeight() + dh;
//                nextY += mapY;
//            } else if (dy <= 0 && absMapY >= mCenterY) {
//                mapY = mCenterY - dh;
//                nextY += mapY;
//            }

            AnimatorSet set = new AnimatorSet();
            set.addListener(mAnimatorListener);
            if (!isXborder && !isYborder) {
                ObjectAnimator x = ObjectAnimator.ofFloat(mMapLayout, "x", mapX, nextX);
                ObjectAnimator y = ObjectAnimator.ofFloat(mMapLayout, "y", mapY, nextY);
                set.playTogether(x, y);
                set.setDuration(0);
                set.start();
            } else if (isXborder && !isYborder) {
                ObjectAnimator y = ObjectAnimator.ofFloat(mMapLayout, "y", mapY, nextY);

                set.playTogether(y);
                set.setDuration(0);
                set.start();
            } else if (!isXborder && isYborder) {
                ObjectAnimator x = ObjectAnimator.ofFloat(mMapLayout, "x", mapX, nextX);
                set.playTogether(x);
                set.setDuration(0);
                set.start();
            } else {
                mMapLayout.updateMapAnimData(scaleValue, getAbsoluteX(), getAbsoluteY());
            }

            oldX = event.getRawX();
            oldY = event.getRawY();

            return true;
        } else {
            mScaleGestureDetector.onTouchEvent(event);
            if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP && 1 == fingerCnt) {
                SDKLogTool.LogD(TAG, "1 Finger up");
                isStartScale = false;
            }
            return true;
        }
    }

    private void zoomView(float end) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mMapLayout, "scaleX", scaleValue, end);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mMapLayout, "scaleY", scaleValue, end);

        AnimatorSet set = new AnimatorSet();
        set.addListener(mAnimatorListener);
        set.playTogether(scaleX, scaleY);
        set.setDuration(0);
        set.start();

        scaleValue = end;
    }

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mMapLayout.updateMapAnimData(scaleValue, getAbsoluteX(), getAbsoluteY());
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public boolean zoomUp() {
        if (scaleValue >= SCALE_MAX) {
            return false;
        }

        zoomView(scaleValue + 0.1f);
        scaleValue += 0.1f;
        return true;
    }

    public boolean zoomDown() {
        if (scaleValue <= SCALE_MIN) {
            return false;
        }

        zoomView(scaleValue - 0.1f);
        scaleValue -= 0.1f;

        return true;
    }

    public void moveToCenter() {
        if (mMapLayout == null)
            return;

        ObjectAnimator x = ObjectAnimator.ofFloat(mMapLayout, "x", mMapLayout.getX(), 0);
        ObjectAnimator y = ObjectAnimator.ofFloat(mMapLayout, "y", mMapLayout.getY(), 0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(x, y);
        set.setDuration(300);
        set.start();
    }


    private float getAbsoluteX() {
        float mapX = mMapLayout.getX();
        float mapCentX = mapX + mMapLayout.getPivotX();
        float absMapX = scaleValue * (mMapLayout.getPivotX() - mMapLayout.getLeft());
        absMapX = mapCentX - absMapX;

        return absMapX;
    }

    private float getAbsoluteY() {
        float mapY = mMapLayout.getY();
        float mapCentY = mapY + mMapLayout.getPivotY();
        float absMapY = scaleValue * (mMapLayout.getPivotY() - mMapLayout.getTop());
        absMapY = mapCentY - absMapY;

        return absMapY;
    }

    public void updatePos(String userID, double x, double y) {
        if (mMapLayout == null)
            return;

        mMapLayout.updatePos(userID, x, y);
    }

    public void updatePos(PositionView user, double x, double y, int duration) {
        if (mMapLayout == null)
            return;

        mMapLayout.updatePos(user, x, y, duration);
    }

    public  void jumpToPos(PositionView user, double x, double y){
        if (mMapLayout == null)
            return;

        mMapLayout.jumpToPos(user, x, y);
    }

    public void refreshPath(List<PathPoint> pathPoint, int pathColor) {
        if (null == mMapLayout)
            return;

        mMapLayout.refreshPath(pathPoint,pathColor);
    }

    public void removePath() {
        if (null == mMapLayout)
            return;

        mMapLayout.removePath();
    }
}
