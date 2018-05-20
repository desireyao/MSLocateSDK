package com.marslocate.sdk;

import android.content.Context;

/**
 * Created by yaoh on 2018/5/19.
 */

public class MSLocateSDKManager {

    private MSBeaconManager mBeaconManager;

    private static MSLocateSDKManager mInstance;

    private Context mContext;

    private MSLocateSDKManager(Context context) {
        mContext = context.getApplicationContext();
        mBeaconManager = new MSBeaconManager(mContext);
    }

    public static MSLocateSDKManager initSDK(Context context, String key) {

        if (mInstance == null) {
            mInstance = new MSLocateSDKManager(context);
        }

        return mInstance;
    }

    public void start() {
        mBeaconManager.startBeaconService();
    }

    public void stop() {
        mInstance = null;
        mBeaconManager.stop();
    }
}
