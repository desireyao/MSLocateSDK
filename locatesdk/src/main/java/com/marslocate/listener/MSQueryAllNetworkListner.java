package com.marslocate.listener;


import com.marslocate.model.MSNetworkList;
import com.marslocate.network.enums.EnumStatus;

/**
 * Created by yaoh on 2018/5/18.
 */

public interface MSQueryAllNetworkListner {

    public void onQueryAllNetworkList(EnumStatus status, MSNetworkList networks);

}
