package com.marslocate.listener;

import com.marslocate.model.MSLocationMapInfo;
import com.marslocate.model.MSLocationPosition;
import com.marslocate.sdk.enums.EnumLocationStatus;

/**
 * Created by yaoh on 2018/5/21.
 * 地图定位回调的接口
 */

public interface MSLocationListener {

    /**
     * mapInfo 地图信息
     */
    public void onMapChanged(MSLocationMapInfo mapInfo);

    /**
     * location 定位信息
     */
    public void onLocationChanged(MSLocationPosition location);

    /**
     * location 状态信息
     */
    public void onLocationStatus(EnumLocationStatus status);

}
