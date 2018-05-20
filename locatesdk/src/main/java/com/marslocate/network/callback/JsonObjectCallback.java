package com.marslocate.network.callback;

import com.google.gson.Gson;
import com.marslocate.network.enums.EnumStatus;

import java.lang.reflect.ParameterizedType;

/**
 * Created by yaoh on 2018/5/18.
 */

public abstract class JsonObjectCallback<T> implements BaseHttpCallback<T> {

    @Override
    public T parseNetworkResponse(String response) throws Exception {

        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        // 反射创建对象，修改字段值
        T t = null;
        try {
            t = new Gson().fromJson(response, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }


    @Override
    public void onError(EnumStatus errorMsg, int id) {
        onError(errorMsg);
    }

    @Override
    public void onResponse(T response, int id) {
        onResponse(response);
    }

    public abstract void onError(EnumStatus errorMsg);

    public abstract void onResponse(T response);

}
