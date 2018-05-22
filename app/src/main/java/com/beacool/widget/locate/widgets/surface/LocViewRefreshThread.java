package com.beacool.widget.locate.widgets.surface;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by JoshuaYin on 16/1/11.
 */
public class LocViewRefreshThread extends Thread {
    private static final String TAG = "LocViewRefreshThread";

    private SurfaceView mSurface;
    private SurfaceHolder mSurHold;
    private boolean isRun = true;
    private boolean isPause = false;
    private ReentrantLock mLockPause;

    public LocViewRefreshThread(SurfaceView surfaceView) {
        mSurface = surfaceView;
        mSurHold = mSurface.getHolder();
        mLockPause = new ReentrantLock();
    }

    @Override
    public void run() {
        Canvas c;
        while (isRun) {
            c = null;
            c = mSurHold.lockCanvas();
            synchronized (mSurHold) {
                mLockPause.lock();
                if (!isPause) {
                    mSurface.draw(c);
                }
                mLockPause.unlock();
            }

            if(c!=null){
                mSurHold.unlockCanvasAndPost(c);
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    public boolean pause(int status) {
        mLockPause.lock();
        if (status > 0) {
            isPause = true;
        } else if (status < 0) {
            isPause = false;
        }

        boolean flag = isPause;
        mLockPause.unlock();

        return flag;
    }
}
