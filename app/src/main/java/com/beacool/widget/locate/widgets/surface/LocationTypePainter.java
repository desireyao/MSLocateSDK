package com.beacool.widget.locate.widgets.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;


import com.marslocate.log.SDKLogTool;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by JoshuaYin on 16/1/11.
 */
public class LocationTypePainter extends LocPainter {
    private static final String TAG = "LocationTypePainter";

    private LocMyself mLocMe = null;
    private ReentrantLock mLockData;
    private Context mContext;

    private Bitmap mBmpSelf;
    private float mSelfLeftBorder, mSelfTopBorder;
    private float mScaleSelfX, mScaleSelfY;
    private float mSelfX, mSelfY;

    private boolean hasRefreshData = false;

    protected LocationTypePainter(Context context, float centerX, float centerY) {
        super(centerX, centerY);
        mContext = context;
        mLockData = new ReentrantLock();
    }

    boolean initImgMap(Bitmap bmpMap) {
        if (bmpMap == null)
            return false;

        mLockData.lock();
        if (mBmpMap == null) {
            mBmpMap = bmpMap;
            mMapLeftBorder = mCenterX - mBmpMap.getWidth() / 2.f;
            mMapTopBorder = mCenterY - mBmpMap.getHeight() / 2.f;
            mMapWidth = mBmpMap.getWidth();
            mMapHeight = mBmpMap.getHeight();
        }
        mLockData.unlock();
        return true;
    }

    boolean setImgMap(Bitmap bmpMap) {
        if (bmpMap == null)
            return false;

        mLockData.lock();
        mBmpMap = bmpMap;
        mMapLeftBorder = mCenterX - mBmpMap.getWidth() / 2.f;
        mMapTopBorder = mCenterY - mBmpMap.getHeight() / 2.f;
        mMapWidth = mBmpMap.getWidth();
        mMapHeight = mBmpMap.getHeight();
        mLockData.unlock();
        return true;
    }

    boolean setImgMyself(Bitmap bmpSelf) {
        if (bmpSelf == null)
            return false;

        mLockData.lock();
        mBmpSelf = bmpSelf;
        mLocMe = new LocMyself(mBmpSelf);
        mLockData.unlock();
        return true;
    }

    void refreshAngle(float angle) {
        mLockData.lock();
        mAngle = angle;
        if (mLocMe != null) {
            mLocMe.refreshAngle(angle);
        }
        mLockData.unlock();
    }

    boolean refreshData(Bitmap bmpMap, boolean isFirstLoc,
                        boolean isChangeMap, float angle,
                        float width, float height, float scale,
                        boolean isShowMyself, float x, float y) {
        mLockData.lock();

        boolean flag = super.refreshData(bmpMap, isChangeMap, angle, width, height, scale, isShowMyself);

        if (flag) {
            mScaleSelfX = x / mMapWidth * mScale;
            mScaleSelfY = 1.f - y / mMapHeight * mScale;

            if (isFirstLoc || isResetLocType) {
                mMapLeftBorder = mCenterX - mScaleSelfX * mMapWidth;
                mMapTopBorder = mCenterY - mScaleSelfY * mMapHeight;
                if (mBmpSelf == null) {
                    mSelfLeftBorder = mCenterX;
                    mSelfTopBorder = mCenterY;
                } else {
                    mSelfLeftBorder = mCenterX - mBmpSelf.getWidth() / 2.f;
                    mSelfTopBorder = mCenterY - mBmpSelf.getHeight() / 2.f;
                }
            } else {
                if (mBmpSelf == null) {
                    mSelfLeftBorder = mMapLeftBorder + mScaleSelfX * mMapWidth;
                    mSelfTopBorder = mMapTopBorder + mScaleSelfY * mMapHeight;
                } else {
                    mSelfLeftBorder = mMapLeftBorder + mScaleSelfX * mMapWidth - mBmpSelf.getWidth() / 2.f;
                    mSelfTopBorder = mMapTopBorder + mScaleSelfY * mMapHeight - mBmpSelf.getHeight() / 2.f;
                }
            }

            mSelfX = x;
            mSelfY = y;

            if (mLocMe != null) {
                mLocMe.refreshSelf(mSelfLeftBorder, mSelfTopBorder, false, isChangeMap);
            }

            hasRefreshData = true;
            isResetLocType = false;
        }
        mLockData.unlock();

        return true;
    }

    void doDraw(Canvas canvas, Paint paint) {
        if (mBmpMap == null || mBmpMap.isRecycled()) {
            return;
        }

        mLockData.lock();

        canvas.drawBitmap(mBmpMap, mMapLeftBorder, mMapTopBorder, paint);

        if (isShowMyself && mLocMe != null && hasRefreshData) {
            mLocMe.drawSelf(canvas, paint);
        }

        mLockData.unlock();
    }

    boolean isPointInMap(float x, float y) {
        SDKLogTool.LogE(TAG, "isPointInMap--->x=" + x + " y=" + y + "\nleft=" + mMapLeftBorder + " top=" + mMapTopBorder);
        if (x >= mMapLeftBorder && x <= (mMapLeftBorder + mMapWidth)
                && y >= mMapTopBorder && y <= (mMapTopBorder + mMapHeight)) {
            return true;
        } else {
            return false;
        }
    }

    void moveMap(float dX, float dY) {
        mLockData.lock();

        mMapLeftBorder += dX;
        mMapTopBorder += dY;

        if (hasRefreshData) {
            mScaleSelfX = mSelfX / mMapWidth * mScale;
            mScaleSelfY = 1.f - mSelfY / mMapHeight * mScale;

            if (mBmpSelf == null) {
                mSelfLeftBorder = mMapLeftBorder + mScaleSelfX * mMapWidth;
                mSelfTopBorder = mMapTopBorder + mScaleSelfY * mMapHeight;

            } else {
                mSelfLeftBorder = mMapLeftBorder + mScaleSelfX * mMapWidth - mBmpSelf.getWidth() / 2.f;
                mSelfTopBorder = mMapTopBorder + mScaleSelfY * mMapHeight - mBmpSelf.getHeight() / 2.f;

            }

            mLocMe.refreshSelf(mSelfLeftBorder, mSelfTopBorder, true, false);
            mLocMe.refreshAngle(mAngle);
        }

        mLockData.unlock();
    }

    private boolean isResetLocType = false;

    void resetLocationType() {
        mLockData.lock();
        isResetLocType = true;

        if (mLocMe != null) {
            mMapLeftBorder = mCenterX - mScaleSelfX * mMapWidth;
            mMapTopBorder = mCenterY - mScaleSelfY * mMapHeight;

            if (mBmpSelf == null) {
                mSelfLeftBorder = mCenterX;
                mSelfTopBorder = mCenterY;
            } else {
                mSelfLeftBorder = mCenterX - mBmpSelf.getWidth() / 2.f;
                mSelfTopBorder = mCenterY - mBmpSelf.getHeight() / 2.f;
            }

            mLocMe.resetLocType();
            mLocMe.refreshSelf(mSelfLeftBorder, mSelfTopBorder, false, false);
        }
        mLockData.unlock();
    }
}
