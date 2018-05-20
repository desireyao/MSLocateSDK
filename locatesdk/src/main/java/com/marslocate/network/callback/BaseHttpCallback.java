package com.marslocate.network.callback;

import com.marslocate.network.enums.EnumStatus;

/**
 * Created by yaoh on 2018/5/18.
 */

public interface BaseHttpCallback<T> {

    public T parseNetworkResponse(String response) throws Exception;

    public void onError(EnumStatus error, int id);

    public void onResponse(T response, int id);

}
