package com.marslocate.network;

import android.content.Context;

import com.marslocate.log.SDKLogTool;
import com.marslocate.network.callback.BaseHttpCallback;
import com.marslocate.network.enums.EnumStatus;
import com.marslocate.network.request.StringRequestWithAuth;
import com.marslocate.volley.AuthFailureError;
import com.marslocate.volley.RequestQueue;
import com.marslocate.volley.Response;
import com.marslocate.volley.VolleyError;
import com.marslocate.volley.toolbox.Volley;

/**
 * Created by yaoh on 2018/5/18.
 */

public class NetworkManager {

    private static final String TAG = "NetworkManager";

    public volatile static NetworkManager mInstance;
    private RequestQueue mQueue;

    private Context mContext;
    private String mKey;

    private NetworkManager() {
    }

    public static NetworkManager get() {
        if (mInstance == null) {
            synchronized (NetworkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetworkManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, String key) {
        mContext = context.getApplicationContext();
        mKey = key;

        mQueue = Volley.newRequestQueue(mContext);
    }

    private void doGetStringRequest(String requestParams, final BaseHttpCallback callback) {
        String url = NetworkConfigs.HOST + requestParams;
        SDKLogTool.LogE_DEBUG(TAG, "doGetStringRequest---> url = " + url);

        StringRequestWithAuth stringRequest = new StringRequestWithAuth(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SDKLogTool.json(TAG, response);
                        try {
                            Object obj = callback.parseNetworkResponse(response);
                            callback.onResponse(obj, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SDKLogTool.LogE_DEBUG(TAG, error.toString());
                        if (error instanceof AuthFailureError) {
                            callback.onError(EnumStatus.STATUS_ERROR_AUTH, 0);
                        } else {
                            callback.onError(EnumStatus.STATUS_ERROR_NET, 0);
                        }
                    }
                });
        stringRequest.setThird_key(mKey);

        mQueue.add(stringRequest);
    }

    /**
     * 查询用户全部网络
     *
     * @param callback
     */
    public void getAllNetworkList(BaseHttpCallback callback) {
        String params = "/v1/network/";
        doGetStringRequest(params, callback);
    }

    /**
     * 查询当前网络的所有 beacon 信息
     */
    public void getNetworkAllBeacons(String networkId, BaseHttpCallback callback) {
        String params = "/v1/network/" + networkId + "/map/ibeacon";
        doGetStringRequest(params, callback);
    }

    /**
     * 获取地图信息
     */
    public void getMapInfo(String networkId, int mapId, BaseHttpCallback callback) {
        String params = "/v1/network/" + networkId + "/map/" + mapId;
        doGetStringRequest(params, callback);
    }

}
