package com.beacool.widget.locate.widgets.surface;

import android.graphics.Bitmap;

/**
 * Created by JoshuaYin on 16/1/11.
 */
public abstract class LocPainter {
    protected Bitmap mBmpMap;

    protected float mMapWidth, mMapHeight;
    protected float mBmpMapWidth, mBmpMapHeight;
    protected float mCenterX, mCenterY;
    protected float mMapLeftBorder = -1, mMapTopBorder = -1;
    protected float mScale = -1;
    protected float mAngle = -1;
    protected boolean isChangeMap, isShowMyself;

    protected LocPainter(float centerX, float centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
    }

    protected boolean refreshData(Bitmap bmpMap, boolean isChangeMap, float angle,
                                  float width, float height, float scale,
                                  boolean isShowMyself) {
        if (bmpMap == null) {
            return false;
        }

        mBmpMap = bmpMap;
        mAngle = angle;

        this.isChangeMap = isChangeMap;
        if (isChangeMap) {
            if (mMapLeftBorder == -1 && mMapTopBorder == -1) {
                mMapLeftBorder = mCenterX - (width / 2.f);
                mMapTopBorder = mCenterY - (height / 2.f);
            } else {
                float justX = mCenterX - mMapLeftBorder;
                mMapLeftBorder = mCenterX - justX / mMapWidth * width;
                float justY = mCenterY - mMapTopBorder;
                mMapTopBorder = mCenterY - justY / mMapHeight * height;
            }
        }

        this.isShowMyself = isShowMyself;
        mScale = scale;
        mMapWidth = width;
        mMapHeight = height;


        return true;
    }

    protected boolean refreshFollowSelf(Bitmap bmpMap, boolean isChangeMap, float angle,
                                        float width, float height, float scale) {
        if (bmpMap == null) {
            return false;
        }

        mBmpMap = bmpMap;
        mMapWidth = width;
        mMapHeight = height;
        mBmpMapWidth=mBmpMap.getWidth();
        mBmpMapHeight=mBmpMap.getHeight();

        mScale=scale;

        return true;
    }
}
