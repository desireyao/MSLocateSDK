package com.beacool.widget.locate.widgets.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by JoshuaYin on 16/1/11.
 */
public class FollowTypePainter extends LocPainter {
    private static final String TAG = "FollowTypePainter";


    private final float TIME_STEP = 20.f;

    private ReentrantLock mLockData;
    private Context mContext;

    private Bitmap mBmpSelf;
    private float mSx = -1.f, mSy = -1.f, mEx, mEy;
    private float mTx, mTy;
    private float mDx, mDy;
    private float mMapX, mMapY;

    protected FollowTypePainter(Context context, float centerX, float centerY) {
        super(centerX, centerY);
        mContext = context;
        mLockData = new ReentrantLock();
    }

    boolean setImgMap(Bitmap bmpMap) {
        if (bmpMap == null)
            return false;

        mLockData.lock();
        mBmpMap = bmpMap;
        mLockData.unlock();
        return true;
    }

    boolean setImgMyself(Bitmap bmpSelf) {
        if (bmpSelf == null)
            return false;

        mLockData.lock();
        mBmpSelf = bmpSelf;
        mLockData.unlock();
        return true;
    }

    private float mDltAng = 0;

    private boolean hasAngCache = false;

    void refreshAngle(float angle) {
        mLockData.lock();
//        LogTool.LogE(TAG, String.format("refreshAngle---> " +
//                "ang=%.04f\t\ttarget=%.04f\t\tabs=%.04f", mAngle, angle, Math.abs(mAngle - angle)));
        float tempDltAng = mAngle - angle;
        if (Math.abs(tempDltAng) > 15) {
//            if (tempDltAng >= 0 && mDltAng >= 0) {
//                hasAngCache = true;
//                mAngle = angle;
//            } else if (tempDltAng < 0 && mDltAng < 0) {
//                hasAngCache = true;
//                mAngle = angle;
//            } else {
//                if (hasAngCache) {
//                    hasAngCache = false;
//                }
//            }
            mAngle = angle;
        }
        mDltAng = tempDltAng;
        mLockData.unlock();
    }

    boolean refreshSelf(Bitmap bmpMap,
                        boolean isChangeMap, float angle,
                        float width, float height, float scale, float x, float y) {
        mLockData.lock();

        boolean flag = super.refreshFollowSelf(bmpMap, isChangeMap, angle, width, height, scale);

        if (flag) {
            if (mSx == -1.f || mSy == -1.f || isChangeMap) {
                mSx = mCenterX - x * mScale;
                mSy = mCenterY - ((float) bmpMap.getHeight() - y * mScale);
            }

            this.isChangeMap = isChangeMap;

            mEx = mCenterX - x * mScale;
            mEy = mCenterY - ((float) bmpMap.getHeight() - y * mScale);
            mDx = mEx - mSx;
            mDy = mEy - mSy;
        }
        mLockData.unlock();

        return true;
    }

    void doDraw(Canvas canvas, Paint paint) {
        if (mBmpMap == null || mBmpMap.isRecycled()) {
            return;
        }

        mLockData.lock();
        if (isChangeMap) {
            goSelf(15000);
        } else {
            goSelf(600);
        }

        Matrix mat = new Matrix();
        mat.postScale(mMapWidth / mBmpMapWidth, mMapHeight / mBmpMapHeight);
        mat.postTranslate(mMapX, mMapY);
        mat.postRotate(-mAngle, mCenterX, mCenterY);

        canvas.drawBitmap(mBmpMap, mat, paint);

        if (mBmpSelf != null) {
            canvas.drawBitmap(mBmpSelf, mCenterX - mBmpSelf.getWidth() / 2.f,
                    mCenterY - mBmpSelf.getHeight() / 2.f, paint);
        }

        mSx = mTx;
        mSy = mTy;

        mLockData.unlock();
    }

    private void goSelf(float time) {
        float step = time / TIME_STEP;
        mTx = mSx + mDx / step;
        mTy = mSy + mDy / step;
        mMapX = mSx - mDx / step;
        mMapY = mSy - mDy / step;
    }

}
