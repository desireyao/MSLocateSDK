package com.beacool.widget.locate.widgets.image;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.beacool.widget.exception.AddViewException;
import com.beacool.widget.models.MapAnimData;
import com.beacool.widget.models.MapPositionData;
import com.marslocate.log.SDKLogTool;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Package com.beacool.widget.install.widgets.
 * Created by JoshuaYin on 16/2/14.
 * Company Beacool IT Ltd.
 * <p/>
 * Description:
 */
public class LocMapLayout extends FrameLayout {
    private static final String TAG = "LocMapLayout";

    private Context mContext;
    private ImageView mImgMap;

    private int mMapWidth = 0, mMapHeight = 0;
    private float mMapScale = 0.f;
    private SparseArray<PositionView> mListUserView;
    private ReentrantLock mLockUserView;

    LocMapLayout(Context context) {
        super(context);
        init(context);
    }

    LocMapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    LocMapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mListUserView = new SparseArray<>();
        mLockUserView = new ReentrantLock();
        setWillNotDraw(false);
        setBackgroundColor(0xffffffff);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        SDKLogTool.LogD(TAG, "onLayout--->left=" + left + " top=" + top + " right=" + right + " bottom=" + bottom);
        int len = getChildCount();
        for (int i = 0; i < len; i++) {
            View child = getChildAt(i);

            if (null == child)
                continue;

            if (child instanceof PositionView) {
                PositionView beacon = (PositionView) child;
                calculateShowPos(beacon);
                int l = (int) beacon.getmPosition().getmLeft();
                int t = (int) beacon.getmPosition().getmTop();
                int r = (int) beacon.getmPosition().getmRight();
                int b = (int) beacon.getmPosition().getmBottom();
                beacon.layout(l, t, r, b);
//                SDKLogTool.LogD(TAG, "onLayout--->l=" + l + " t=" + t + " r=" + r + " b=" + b);
            } else if (child instanceof LocPathOverlay) {
                child.layout(left, top, right, bottom);
            } else {
                if (mImgMap != null && child.getId() == mImgMap.getId()) {
                    SDKLogTool.LogD(TAG, "onLayout--->l=" + left + " right=" + right + " top=" + getY() + " bottom=" + getBottom());
                    child.layout(0, 0, getWidth(), getHeight());

                    if (mOnLayoutCallback != null)
                        mOnLayoutCallback.onMapLayout();
                }
            }
        }

    }

    interface OnMapLayoutListener {
        void onMapLayout();
    }

    private OnMapLayoutListener mOnLayoutCallback;

    public void setmOnLayoutCallback(OnMapLayoutListener mOnLayoutCallback) {
        this.mOnLayoutCallback = mOnLayoutCallback;
    }

    boolean setMap(Bitmap bmpMap, int mapWidth, int mapHeight, float mapScale) {
        if (bmpMap == null
                || mapWidth <= 0 || mapHeight <= 0 || mapScale <= 0)
            return false;

        removeAllViews();

        mMapWidth = mapWidth;
        mMapHeight = mapHeight;
        mMapScale = mapScale;

        mPosSize = (int) mMapScale;
        mPosSize = Math.min(100, Math.max(50, mPosSize));
        mPosSize *= Math.min(getWidth(), getHeight()) / 1080.f;

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                mapWidth, mapHeight);

        if (mImgMap == null) {
            mImgMap = new ImageView(mContext);
            mImgMap.setId(View.generateViewId());
            mImgMap.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        mImgMap.setImageBitmap(bmpMap);
        addView(mImgMap, params);

        return true;
    }

    boolean hasAddMapView() {
        if (mImgMap == null)
            return false;

        int len = getChildCount();
        boolean hasAddMapView = false;
        for (int i = 0; i < len; i++) {
            if (getChildAt(i).getId() == mImgMap.getId()) {
                hasAddMapView = true;
                break;
            }
        }

        return hasAddMapView;
    }

    private int mPosSize = 24;

    void addPositions(List<PositionView> positions) throws AddViewException {
        if (!hasAddMapView())
            throw new AddViewException("You must setMap(Bitmap) before add UserView");

        if (null == positions || mPosSize <= 0)
            return;

        for (PositionView b : positions) {
            if (null == b)
                continue;

            addView(b, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mPosSize));
        }
    }

    void addPosition(PositionView position) throws AddViewException {
        if (!hasAddMapView())
            throw new AddViewException("You must setMap(Bitmap) before add UserView");

        if (null == position || mPosSize <= 0)
            return;

        addView(position, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mPosSize));
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof PositionView) {
            mLockUserView.lock();
            SDKLogTool.LogD(TAG, "onViewAdded--->" + ((PositionView) child).getmPosName());
            ((PositionView) child).setmTime(SystemClock.uptimeMillis());
            mListUserView.put(child.getId(), (PositionView) child);
            mLockUserView.unlock();
        }


        SDKLogTool.LogV(TAG, "onViewAdded--->");
    }

    void deletePosition(int viewID) {
        mLockUserView.lock();
        int len = mListUserView.size();
        for (int i = 0; i < len; i++) {
            PositionView preBeacon = mListUserView.valueAt(i);
            if (preBeacon == null)
                continue;

            if (preBeacon.getId() == viewID) {
                removeView(preBeacon);
                mListUserView.remove(viewID);
                break;
            }
        }
        mLockUserView.unlock();
    }

    void deletePosition(String userID) {
        mLockUserView.lock();
        int len = mListUserView.size();
        for (int i = 0; i < len; i++) {
            PositionView preBeacon = mListUserView.valueAt(i);
            if (preBeacon == null)
                continue;

            if (preBeacon.getmUserID().equals(userID)) {
                removeView(preBeacon);
                mListUserView.remove(preBeacon.getId());
                break;
            }
        }
        mLockUserView.unlock();
    }

    void changeImage(int viewID, int flag) {
        mLockUserView.lock();
        int len = mListUserView.size();
        for (int i = 0; i < len; i++) {
            PositionView preBeacon = mListUserView.valueAt(i);
            if (preBeacon == null)
                continue;

            if (preBeacon.getId() == viewID) {
                preBeacon.changeImage(flag);
                break;
            }
        }
        mLockUserView.unlock();
    }

    void clearAllPositions() {
//        int len = getChildCount();
//        int count = 0;
//        for (int i = 0; i < len; i++) {
//            View child = getChildAt(i);
//            if (child == null || !(child instanceof UserView))
//                continue;
//
//            UserView preBeacon = (UserView) child;
//            removeView(preBeacon);
//            count += 1;
//        }

        int count = 0;
        mLockUserView.lock();
        int len = mListUserView.size();
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            PositionView b = mListUserView.valueAt(i);
            if (b == null)
                continue;

            removeView(b);
            arr.add(b.getId());
            count += 1;
        }

        for (int i : arr)
            mListUserView.remove(i);

        mLockUserView.unlock();
        SDKLogTool.LogD(TAG, "clearAllPositions--->" + count);
    }

    private float mAbsoluteScale = 1.f;
    private float mAbsLayoutX = 0.f, mAbsLayoutY = 0.f;
    private float mFixScale = 1.f;
    private boolean isFixY = false;
    private float absMapX = 0.f, absMapY = 0.f;
    private float absFirstMapX = 0.f, absFirstMapY = 0.f;
    private float absMapWidth = 0.f, absMapHeight = 0.f;

    float updateMapAnimData(float absScale, float absLayoutX, float absLayoutY) {
        mAbsoluteScale = absScale;
        mAbsLayoutX = absLayoutX;
        mAbsLayoutY = absLayoutY;

        if (mAbsoluteScale > 0) {
            float mapViewWidth = mImgMap.getWidth();
            float mapViewHeight = mImgMap.getHeight();

            float mapCentX = mapViewWidth / 2;
            float mapCentY = mapViewHeight / 2;

            mFixScale = 1.f;
            float fixX = mapViewWidth / (float) mMapWidth;
            float fixY = mapViewHeight / (float) mMapHeight;

            if (fixX > 1.f && fixY > 1.f) {   // 放大:比率和1.0差得少的
                isFixY = fixX >= fixY;
                mFixScale = Math.min(fixX, fixY);
            } else if (fixX > 1.f && fixY <= 1.f) {
                isFixY = true;
                mFixScale = fixY;
            } else if (fixX <= 1.f && fixY > 1.f) {
                isFixY = false;
                mFixScale = fixX;
            } else if (fixX <= 1.f && fixY <= 1.f) {    // 缩小:比率和1.0差得多的
                isFixY = fixX >= fixY;
                mFixScale = Math.min(fixX, fixY);
            }

            float scale = mFixScale * mAbsoluteScale;

            absMapWidth = (int) (mMapWidth * scale);
            absMapHeight = (int) (mMapHeight * scale);
            absMapX = (mapCentX - absMapWidth / 2 + getX());
            absMapY = (mapCentY - absMapHeight / 2 + getY());
            if (mAbsoluteScale == 1.f) {
                absFirstMapX = (mapCentX - absMapWidth / 2);
                absFirstMapY = (mapCentY - absMapHeight / 2);
            }

//            SDKLogTool.LogD(TAG, "updateMapAnimData--->mFixScale=" + mFixScale + " absMapX=" + absMapX + " absMapY=" + absMapY
//                    + " absMapWidth=" + absMapWidth
//                    + " absMapHeight=" + absMapHeight + " mapViewWidth=" + mapViewWidth
//                    + " mapViewHeight=" + mapViewHeight + " scale=" + scale
//                    + " mAbsoluteScale=" + mAbsoluteScale + " absFirstMapX:" + absFirstMapX + " absFirstMapY:" + absFirstMapY
//                    + " getX=" + getX() + " getY=" + getY() + " layoutX=" + mAbsLayoutX + " layoutY=" + mAbsLayoutY
//                    + " fixX=" + fixX + " fixY=" + fixY);
        }

        return mFixScale;
    }

    MapAnimData getCanMoveArea(float absScale, float absLayoutX, float absLayoutY) {
        updateMapAnimData(absScale, absLayoutX, absLayoutY);
        MapAnimData data = new MapAnimData();
        data.absMapX = absMapX;
        data.absMapY = absMapY;
        data.absMapWidth = absMapWidth;
        data.absMapHeight = absMapHeight;


        return data;
    }

    private static final DecimalFormat posFormat = new DecimalFormat("#.###");

    void calculateShowPos(PositionView user) {
        MapPositionData data = user.getmPosition();
        if (data.hasCalculateView())
            return;

        double posX = data.getmCoordinateX();
        double posY = data.getmCoordinateY();

        posX = posX * mMapScale;
        posY = mMapHeight - posY * mMapScale;

        posX *= mFixScale;
        posY *= mFixScale;

//        float absX = (absMapX - mAbsLayoutX) + posX;
//        float absY = (absMapY - mAbsLayoutY) + posY;
        double absX = absFirstMapX + posX;
        double absY = absFirstMapY + posY;

        data.setmPosX(absX);
        data.setmPosY(absY);

        float absUserPosW = (user.getMeasuredWidth());
        float absUserPosH = (user.getMeasuredHeight());

        data.setmCurWidth(absUserPosW);
        data.setmCurHeight(absUserPosH);
        data.setmLeft(absX - absUserPosW / 2);
        data.setmTop(absY - (absUserPosH - user.getPosAnchorMarginBottom()));
        data.setmRight(absX + absUserPosW / 2);
        data.setmBottom(absY + user.getPosAnchorMarginBottom());

        SDKLogTool.LogD(TAG, "calculateShowPos--->[name]:" + user.getmPosName()
                + " [coordX]:" + data.getmCoordinateX()
                + " [coordY]:" + data.getmCoordinateY()
                + " [posX]:" + posX
                + " [posY]:" + posY
                + " [absX]:" + absX
                + " [absY]:" + absY
                + " [absUserPosW]:" + absUserPosW
                + " [absUserPosH]:" + absUserPosH
                + " absFirstMapX=" + absFirstMapX + " absFirstMapY=" + absFirstMapY
                + " absMapX=" + absMapX + " absMapY=" + absMapY + " getX=" + getX() + " getY=" + getY());

        data.setHasCalculateView(true);
    }

    void updatePos(String userID, double x, double y) {
        PositionView user = null;
        mLockUserView.lock();
        int len = mListUserView.size();
        for (int i = 0; i < len; i++) {
            PositionView u = mListUserView.valueAt(i);
            if (u == null)
                continue;

            if (u.getmUserID().equals(userID)) {
                user = u;
                break;
            }
        }

        mLockUserView.unlock();
        updatePos(user, x, y, 200);
    }

    void updatePos(PositionView user, double x, double y, int duration) {
        if (user == null)
            return;

        mLockUserView.lock();
        int len = mListUserView.size();
        boolean exist = false;
        for (int i = 0; i < len; i++) {
            PositionView u = mListUserView.valueAt(i);
            if (u == null)
                continue;

            if (u.getId() == user.getId()) {
                user = u;
                break;
            }
        }

        mLockUserView.unlock();

        if (!user.getmPosition().hasCalculateView())
            return;

        user.setmTime(SystemClock.uptimeMillis());
        AnimatorSet set = new AnimatorSet();

        float oldX = user.getX();
        float oldY = user.getY();

        double posX = x;
        double posY = y;

        posX *= mMapScale;
        posY = mMapHeight - posY * mMapScale;

        posX *= mFixScale;
        posY *= mFixScale;

        posX += absFirstMapX;
        posY += absFirstMapY;

        user.getmPosition().setmPosX(posX);
        user.getmPosition().setmPosY(posY);

        float absUserPosW = user.getMeasuredWidth();
        float absUserPosH = user.getMeasuredHeight();

        user.getmPosition().setmCurWidth(absUserPosW);
        user.getmPosition().setmCurHeight(absUserPosH);
        user.getmPosition().setmLeft(posX - absUserPosW / 2.f);
        user.getmPosition().setmTop(posY - (absUserPosH - user.getPosAnchorMarginBottom()));
        user.getmPosition().setmRight(posX + absUserPosW / 2.f);
        user.getmPosition().setmBottom(posY + user.getPosAnchorMarginBottom());

        SDKLogTool.LogD(TAG, "updatePos--->" + " oldX=" + (oldX) + " oldY=" + (oldY)
                + " X=" + posX + " Y=" + posY + " mFixScale=" + mFixScale);
        user.getmPosition().setmCoordinateX(x);
        user.getmPosition().setmCoordinateY(y);
        ObjectAnimator aniX = ObjectAnimator.ofFloat(user, "x", oldX, new Float(user.getmPosition().getmLeft()));
        ObjectAnimator aniY = ObjectAnimator.ofFloat(user, "y", oldY, new Float(user.getmPosition().getmTop()));

        set.playTogether(aniX, aniY);
        set.setDuration(duration);
        set.start();
    }


    void jumpToPos(PositionView user, double x, double y) {
        if (user == null)
            return;

        mLockUserView.lock();
        int len = mListUserView.size();
        boolean exist = false;
        for (int i = 0; i < len; i++) {
            PositionView u = mListUserView.valueAt(i);
            if (u == null)
                continue;

            if (u.getId() == user.getId()) {
                user = u;
                break;
            }
        }

        mLockUserView.unlock();

        if (!user.getmPosition().hasCalculateView())
            return;

        user.setmTime(SystemClock.uptimeMillis());
        AnimatorSet set = new AnimatorSet();

        float oldX = user.getX();
        float oldY = user.getY();

        double posX = x;
        double posY = y;

        posX *= mMapScale;
        posY = mMapHeight - posY * mMapScale;

        posX *= mFixScale;
        posY *= mFixScale;

        posX += absFirstMapX;
        posY += absFirstMapY;

        user.getmPosition().setmPosX(posX);
        user.getmPosition().setmPosY(posY);

        float absUserPosW = user.getMeasuredWidth();
        float absUserPosH = user.getMeasuredHeight();

        user.getmPosition().setmCurWidth(absUserPosW);
        user.getmPosition().setmCurHeight(absUserPosH);
        user.getmPosition().setmLeft(posX - absUserPosW / 2.f);
        user.getmPosition().setmTop(posY - (absUserPosH - user.getPosAnchorMarginBottom()));
        user.getmPosition().setmRight(posX + absUserPosW / 2.f);
        user.getmPosition().setmBottom(posY + user.getPosAnchorMarginBottom());

        SDKLogTool.LogD(TAG, "updatePos--->" + " oldX=" + (oldX) + " oldY=" + (oldY)
                + " X=" + posX + " Y=" + posY + " mFixScale=" + mFixScale);
        user.getmPosition().setmCoordinateX(x);
        user.getmPosition().setmCoordinateY(y);

        user.setX(new Float(user.getmPosition().getmLeft()));
        user.setY(new Float(user.getmPosition().getmTop()));
    }

    private LocPathOverlay pathOverlay;

    void refreshPath(List<PathPoint> pathPoint, int pathColor) {
        if (pathPoint == null || pathPoint.isEmpty())
            return;

        if (pathOverlay != null)
            for (int i = 0, len = getChildCount(); i < len; i++) {
                if (getChildAt(i).getId() == pathOverlay.getId()) {
                    removeView(pathOverlay);
                    break;
                }
            }

        for (PathPoint point : pathPoint)
            calculatePathPos(point);

        pathOverlay = new LocPathOverlay(mContext, pathPoint, pathColor);
        addView(pathOverlay, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    void removePath() {
        if (pathOverlay != null)
            for (int i = 0, len = getChildCount(); i < len; i++) {
                if (getChildAt(i).getId() == pathOverlay.getId()) {
                    removeView(pathOverlay);
                    break;
                }
            }
    }

    void calculatePathPos(PathPoint point) {
        double posX = point.getCoordinateX();
        double posY = point.getCoordinateY();

        posX = posX * mMapScale;
        posY = mMapHeight - posY * mMapScale;

        posX *= mFixScale;
        posY *= mFixScale;

        double absX = absFirstMapX + posX;
        double absY = absFirstMapY + posY;

        point.posX = absX;
        point.posY = absY;

        SDKLogTool.LogD(TAG, "calculatePathPos--->[name]:" + point.getName()
                + " [coordX]:" + point.getCoordinateX()
                + " [coordY]:" + point.getCoordinateY()
                + " [posX]:" + posX
                + " [posY]:" + posY);
    }
}
