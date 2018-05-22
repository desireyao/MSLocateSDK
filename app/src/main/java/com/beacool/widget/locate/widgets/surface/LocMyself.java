package com.beacool.widget.locate.widgets.surface;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by JoshuaYin on 16/1/11.
 */
public class LocMyself {
    private static final String TAG = "LocMyself";

    private final float DEFAULT_ACC = 5;
    private final float TIME_STEP = 20.f;

    private Bitmap mBmpSelf;
    private float mSx = -1.f, mSy = -1.f, mEx, mEy;
    private float mTx, mTy;
    private float mDx, mDy;
    private float mAng = 361.f;
    private float mTime = 0;
    private boolean isMoveMap = false, isChangeMap = false;
    private boolean isResetLocType = false;

    LocMyself(Bitmap bmpSelf) {
        this.mBmpSelf = bmpSelf;
    }

    void refreshAngle(float angle) {
        if (Math.abs(mAng - angle) > 5) {
            mAng = angle;
        }
    }

    void refreshSelf(float showX, float showY, boolean isMoveMap, boolean isChangeMap) {
        if (!isMoveMap) {
            mTime = 0;
        }

        this.isMoveMap = isMoveMap;
        this.isChangeMap = isChangeMap;

//        LogTool.LogE(TAG, String.format("refresh Bef--->sX=%.5f\teX=%.5f\ttX=%.5f\tdX=%.5f", mSx, mEx, mTx, mDx));
        if ((mSx == -1.f && mSy == -1.f) || isResetLocType) {
            mSx = showX;
            mSy = showY;
        }

        mEx = showX;
        mEy = showY;
        mDx = showX - mSx;
        mDy = showY - mSy;
//        LogTool.LogE(TAG, String.format("refresh Aft--->sX=%.5f\teX=%.5f\ttX=%.5f\tdX=%.5f", mSx, mEx, mTx, mDx));
    }

    private void goSelf(float time) {
        if (isChangeMap || isMoveMap) {
            mTx = mEx;
            mTy = mEy;
        } else {
            if (mSx != -1.f && mSy != -1.f) {
//                LogTool.LogE(TAG, String.format("goSelf first--->mLx=%.5f\tmLy=%.5f\tmSx=%.5f\tmSy=%.5f", mLx, mLy, mSx, mSy));

                float step = time / TIME_STEP;
                mTx = mSx + mDx / step;
                mTy = mSy + mDy / step;

                if ((mDx >= 0 && ((mEx - mTx) < 0))
                        || (mDx < 0 && ((mEx - mTx) >= 0))) {
                    mTx = mEx;
                }

                if ((mDy >= 0 && ((mEy - mTy) < 0))
                        || (mDy < 0 && ((mEy - mTy) >= 0))) {
                    mTy = mEy;
                }
            }
        }

//        LogTool.LogE(TAG, String.format("goSelf--->sX=%.5f\teX=%.5f\ttX=%.5f\tdX=%.5f", mSx, mEx, mTx, mDx));
    }

    void drawSelf(Canvas canvas, Paint paint) {
        goSelf(600);

        Matrix mat = new Matrix();
        mat.postRotate(mAng, mBmpSelf.getWidth() / 2.f, mBmpSelf.getHeight() / 2.f);
        mat.postTranslate(mTx, mTy);

        canvas.drawBitmap(mBmpSelf, mat, paint);

        mSx = mTx;
        mSy = mTy;

        if (isResetLocType) {
            mSx = -1.f;
            mSy = -1.f;
            isResetLocType = false;
        }
    }

    void resetLocType() {
        this.isResetLocType = true;
    }
}
