package com.beacool.widget.locate.widgets.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.beacool.widget.locate.listener.BeaconLocSurfaceViewListener;
import com.marslocate.log.SDKLogTool;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by JoshuaYin on 16/1/11.
 */
public class BeaconLocSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "BeaconLocSurfaceView";

    public static final int LOCATE_SURFACE_TYPE_LOCATE = 1;
    public static final int LOCATE_SURFACE_TYPE_FOLLOW = 2;

    private Context mContext;

    private SurfaceHolder mSurHolder;
    private BeaconLocSurfaceViewListener mListener;
    private LocViewRefreshThread mThreadDraw;

    private boolean isOnlyTouchMapMove = true;

    private boolean isSurfaceAlive = false;
    private ReentrantLock mLockSurfaceStatus;

    private int mLocateType;

    // Tool
    private Paint mPaint;
    private LocationTypePainter mLocPainter = null;
    private FollowTypePainter mFollowPainter = null;

    public BeaconLocSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mSurHolder = this.getHolder();
        mSurHolder.addCallback(this);

        setZOrderOnTop(false);
        mSurHolder.setFormat(PixelFormat.TRANSLUCENT);
        mLocateType = LOCATE_SURFACE_TYPE_LOCATE;

        setKeepScreenOn(true);

        mLockSurfaceStatus = new ReentrantLock();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        SDKLogTool.LogE(TAG, "surfaceCreated--->");
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        if (mLocPainter == null)
            mLocPainter = new LocationTypePainter(mContext, getWidth() / 2.f, getHeight() / 2.f);

        if (mFollowPainter == null)
            mFollowPainter = new FollowTypePainter(mContext, getWidth() / 2.f, getHeight() / 2.f);

        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (mListener != null) {
                        mListener.onSurfaceCreate();
                    }
                }
            }
        }.start();

        isSurfaceAlive(1);
        startDrawSurface();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        SDKLogTool.LogE(TAG, "surfaceDestroyed--->");
        mThreadDraw.pause(1);
        pauseDrawSurface();
        isSurfaceAlive(-1);
        if (mListener != null) {
            mListener.onSurfaceDestory();
        }
    }

    public boolean initSurfaceView(BeaconLocSurfaceViewListener listener) {
        if (listener == null) {
            return false;
        }

        mListener = listener;

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (null == canvas || !isSurfaceAlive(0)) {
            return;
        }
        super.draw(canvas);

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.WHITE);

        switch (mLocateType) {
            case LOCATE_SURFACE_TYPE_LOCATE: {
                if (mLocPainter != null) {
                    mLocPainter.doDraw(canvas, mPaint);
                }
                break;
            }
            case LOCATE_SURFACE_TYPE_FOLLOW: {
                if (mFollowPainter != null) {
                    mFollowPainter.doDraw(canvas, mPaint);
                }
                break;
            }

        }
    }

    public void refreshAngle(float angle) {
        if (mLocPainter != null)
            mLocPainter.refreshAngle(angle);

        if (mFollowPainter != null)
            mFollowPainter.refreshAngle(angle);

    }

    public boolean refreshData(Bitmap bmpMap, boolean isFirstLoc,
                               boolean isChangeMap, float angle,
                               float width, float height, float scale,
                               boolean isShowMyself, float x, float y) {
        boolean flagLoc = false, flagFollow = false;
        if (mLocPainter != null)
            flagLoc = mLocPainter.refreshData(bmpMap, isFirstLoc,
                    isChangeMap, angle, width, height, scale, isShowMyself, x, y);
        if (mFollowPainter != null)
            flagFollow = mFollowPainter.refreshSelf(bmpMap,
                    isChangeMap, angle, width, height, scale, x, y);

//        switch (mLocateType) {
//            case LOCATE_SURFACE_TYPE_LOCATE: {
//                if (mLocPainter != null)
//                    flag = mLocPainter.refreshData(bmpMap, isFirstLoc,
//                            isChangeMap, angle, width, height, scale, isShowMyself, x, y);
//                break;
//            }
//            case LOCATE_SURFACE_TYPE_FOLLOW: {
//                if (mFollowPainter != null)
//                    flag = mFollowPainter.refreshSelf(bmpMap,
//                            isChangeMap, angle, width, height, scale, x, y);
//                break;
//            }
//        }

        return flagLoc && flagFollow;
    }


    private boolean hasMove = false, isCanMove = false;
    private float mPointX, mPointY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                hasMove = false;
                mPointX = event.getX();
                mPointY = event.getY();
                if (LOCATE_SURFACE_TYPE_LOCATE == mLocateType) {
                    if (mLocPainter != null) {
                        isCanMove = (!isOnlyTouchMapMove || mLocPainter.isPointInMap(mPointX, mPointY));
                        SDKLogTool.LogE(TAG, "isCanMove=" + isCanMove);
                    }
                } else if (LOCATE_SURFACE_TYPE_FOLLOW == mLocateType) {
                    isCanMove = true;
                }

                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                if (isCanMove) {
                    if (LOCATE_SURFACE_TYPE_FOLLOW == mLocateType) {
                        mLocateType = LOCATE_SURFACE_TYPE_LOCATE;
                        mListener.onForceChangeLocType();
                        break;
                    }
                    float dx = event.getX() - mPointX;
                    float dy = event.getY() - mPointY;
                    if (Math.abs(dx) < 5 && Math.abs(dy) < 5) {
                        hasMove = true;
                    }

                    if (mLocPainter != null) {
                        mLocPainter.moveMap(dx, dy);
                    }

                    mPointX = event.getX();
                    mPointY = event.getY();
                }

                return true;
            }
            case MotionEvent.ACTION_UP: {

                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    public int getmLocateType() {
        return mLocateType;
    }

    public void setmLocateType(int locateType) {
        if (locateType == LOCATE_SURFACE_TYPE_LOCATE && mLocateType != LOCATE_SURFACE_TYPE_LOCATE) {
            if (mLocPainter != null) {
                mLocPainter.resetLocationType();
            }
        }
        this.mLocateType = locateType;
    }

    private void startDrawSurface() {
        if (mThreadDraw != null) {
            if (mThreadDraw.isAlive()) {
                mThreadDraw.interrupt();
            }
        }
        mThreadDraw = new LocViewRefreshThread(this);
        mThreadDraw.setRun(true);
        mThreadDraw.start();
    }

    private void pauseDrawSurface() {
        if (mThreadDraw != null) {
            mThreadDraw.setRun(false);
            if (mThreadDraw.isAlive()) {
                mThreadDraw.interrupt();
            }
        }
    }

    private boolean isSurfaceAlive(int status) {
        mLockSurfaceStatus.lock();
        if (status > 0) {
            isSurfaceAlive = true;
        } else if (status < 0) {
            isSurfaceAlive = false;
        }

        boolean flag = isSurfaceAlive;
        mLockSurfaceStatus.unlock();

        return flag;
    }

    public void setImageSelf(Bitmap bmpSelf) {
        if (mLocPainter != null) {
            mLocPainter.setImgMyself(bmpSelf);
        }

        if (mFollowPainter != null) {
            mFollowPainter.setImgMyself(bmpSelf);
        }
    }

    public void setImageMap(Bitmap bmpMap) {
        if (mLocPainter != null) {
            mLocPainter.setImgMap(bmpMap);
        }

        if (mFollowPainter != null) {
            mFollowPainter.setImgMap(bmpMap);
        }
    }

    public void initImageMap(Bitmap bmpMap) {
        if (mLocPainter != null) {
            mLocPainter.initImgMap(bmpMap);
        }

        if (mFollowPainter != null) {
            mFollowPainter.setImgMap(bmpMap);
        }
    }

    public boolean isOnlyTouchMapMove() {
        return isOnlyTouchMapMove;
    }

    public void setOnlyTouchMapMove(boolean isOnlyTouchMapMove) {
        this.isOnlyTouchMapMove = isOnlyTouchMapMove;
    }
}
