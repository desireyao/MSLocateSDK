package com.beacool.widget.locate.widgets.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.beacool.widget.exception.AddViewException;

import java.util.List;

/**
 * Package com.beacool.widget.install.manager.
 * Created by JoshuaYin on 16/2/14.
 * Company Beacool IT Ltd.
 * <p/>
 * Description:
 */
public class LocViewManager {
    private static final String TAG = "LocViewManager";

    private static LocViewManager mManager;
    private Context mContext;

    private LocViewLayout mMainLayout;
    private int mCurWidth = 0, mCurHeight = 0;
    private float mCurScale = 0.f;

    private LocViewManager(Context context) {
        mContext = context;
    }

    public static LocViewManager getManager(Context context) {
        if (mManager == null)
            mManager = new LocViewManager(context);

        return mManager;
    }

    public boolean init(LocViewLayout layout) {
        if (layout == null)
            return false;

        mMainLayout = layout;
        return true;
    }

    public boolean setMap(Bitmap bmpMap, int width, int height, float scale, boolean isNeedResetState) {
        if (mMainLayout == null || width <= 0
                || height <= 0 || scale <= 0)
            return false;

        mCurWidth = width;
        mCurHeight = height;
        mCurScale = scale;

        boolean flag = mMainLayout.setMap(bmpMap, width, height, scale);

        if (isNeedResetState)
            mMainLayout.reset();

        return flag;
    }

    public void clearAllPositions() {
        if (null != mMainLayout)
            mMainLayout.clearAllPositions();
    }

    public void deletePosition(int viewID) {
        if (mMainLayout != null)
            mMainLayout.deletePosition(viewID);
    }

    public void deletePosition(String userID) {
        if (mMainLayout == null)
            return;

        mMainLayout.deletePosition(userID);
    }

    public void changeImage(int viewID, int flag) {
        if (mMainLayout != null)
            mMainLayout.changeImage(viewID, flag);
    }

    public void addPositions(List<PositionView> users) throws AddViewException {
        if (null == mMainLayout || null == users)
            return;

//        userViewSize = Math.min(200, Math.max(20, userViewSize));

        mMainLayout.addPositions(users);
    }

    public void addPosition(PositionView user) throws AddViewException {
        if (null == mMainLayout || null == user)
            return;

//        userViewSize = Math.min(200, Math.max(20, userViewSize));

        mMainLayout.addPosition(user);
    }

    public void updatePos(String userID, double x, double y) {
        if (mMainLayout == null)
            return;

        mMainLayout.updatePos(userID, x, y);
    }

    public void updatePos(PositionView user, double x, double y, int duration) {
        if (mMainLayout == null)
            return;

        mMainLayout.updatePos(user, x, y, duration);
    }

    public void jumpToPos(PositionView user, double x, double y) {
        if (mMainLayout == null)
            return;

        mMainLayout.jumpToPos(user, x, y);
    }

    public void refreshPath(List<PathPoint> pathPoint, int pathColor) {
        if (null == mMainLayout || null == pathPoint)
            return;

        mMainLayout.refreshPath(pathPoint, pathColor);
    }

    public void removePath() {
        if (null == mMainLayout)
            return;

        mMainLayout.removePath();
    }
}
