package com.marslocate.listener;


import com.marslocate.model.MSNetworkInfo;
import com.marslocate.network.enums.EnumStatus;

import java.util.List;

/**
 * Created by yaoh on 2018/5/18.
 */

public interface MSQueryAllNetworkListner {

    public void onQueryAllNetworkList(EnumStatus status, List<MSNetworkInfo> networkInfos);

}
