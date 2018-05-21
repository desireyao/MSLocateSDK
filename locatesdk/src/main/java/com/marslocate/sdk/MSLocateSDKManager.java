package com.marslocate.sdk;

import android.content.Context;

import com.marslocate.listener.MSLocationListener;
import com.marslocate.listener.MSQueryAllNetworkListner;
import com.marslocate.log.SDKLogTool;
import com.marslocate.model.MSNetworkInfo;
import com.marslocate.network.NetworkManager;
import com.marslocate.network.bean.GetAllNetworkList;
import com.marslocate.network.bean.GetMapInfo;
import com.marslocate.network.callback.JsonObjectCallback;
import com.marslocate.network.enums.EnumStatus;

/**
 * Created by yaoh on 2018/5/19.
 */

public class MSLocateSDKManager {

    private static final String TAG = "MSLocateSDKManager";

    private MSBeaconManager mBeaconManager;

    private static MSLocateSDKManager mInstance;

    private Context mContext;
    private NetworkManager networkManager;
    private String mKey;

    private MSLocateSDKManager(Context context, String key) {
        mContext = context.getApplicationContext();
        mKey = key;

        mBeaconManager = new MSBeaconManager(mContext);
        networkManager = NetworkManager.get();
        networkManager.init(mContext, mKey);
    }

    public static MSLocateSDKManager initSDK(Context context, String key) {

        if (key.isEmpty()) {
            throw new IllegalArgumentException(" key cannot be empty!");
        }

        if (mInstance == null) {
            mInstance = new MSLocateSDKManager(context, key);
        }

        return mInstance;
    }

    /**
     * 获取用户所有网络
     *
     * @param listner
     */
    public void queryAllNetworkList(final MSQueryAllNetworkListner listner) {
        networkManager.getAllNetworkList(new JsonObjectCallback<GetAllNetworkList>() {
            @Override
            public void onResponse(GetAllNetworkList response) {
//                SDKLogTool.LogE_DEBUG(TAG, response.toString());
                if (response.getResult() == 0) {
                    listner.onQueryAllNetworkList(EnumStatus.STATUS_SUCCESS, MSNetworkInfo.parseData(response));
                } else {
                    listner.onQueryAllNetworkList(EnumStatus.STATUS_INTERNAL_ERROR, null);
                }
            }

            @Override
            public void onError(EnumStatus error) {
                // 有可能 鉴权错误
                listner.onQueryAllNetworkList(error, null);
            }
        });
    }

    /**
     * 开始定位
     */
    public void startLocation(String networkId, MSLocationListener listener) {

        if (networkId.isEmpty()) {
            throw new IllegalArgumentException(" networkId cannot be empty!");
        }

        mBeaconManager.startLocation(networkId, listener);

    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        mInstance = null;
        mBeaconManager.unBindConsumer();
    }
}
