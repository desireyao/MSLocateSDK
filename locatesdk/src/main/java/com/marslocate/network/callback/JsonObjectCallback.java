package com.marslocate.network.callback;

import com.google.gson.Gson;
import com.marslocate.log.SDKLogTool;
import com.marslocate.network.enums.EnumStatus;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

/**
 * Created by yaoh on 2018/5/18.
 */

public abstract class JsonObjectCallback<T> implements BaseHttpCallback<T> {

    private static final String TAG = "JsonObjectCallback";

    @Override
    public T parseNetworkResponse(String response) throws Exception {

        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

//        // 反射创建对象，修改字段值
//        T t = null;
//        try {
//            t = new Gson().fromJson(response, clazz);
//        } catch (Exception e) {
//            e.printStackTrace();
//            SDKLogTool.LogE_DEBUG("JsonObjectCallback111--->", e.toString());
//        }
//        SDKLogTool.LogE_DEBUG("JsonObjectCallback222--->", t.toString());

        if (clazz.getName().equals(JSONObject.class.getName())) {
//            SDKLogTool.LogE_DEBUG(TAG, "JsonObjectCallback111--->");
            try {
                Constructor c = clazz.getConstructor(String.class);
                T t = (T) c.newInstance(response);
                return t;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Gson gson = new Gson();
            T t = gson.fromJson(response, clazz);
//            SDKLogTool.LogE_DEBUG(TAG, "JsonObjectCallback222--->" + t.toString() + " clazz.getName() = " + clazz.getName());
            return t;
        }

//        return t;
    }

    @Override
    public void onResponse(T response, int id) {
        onResponse(response);
    }

    @Override
    public void onError(EnumStatus errorMsg, int id) {
        onError(errorMsg);
    }


    public abstract void onError(EnumStatus errorMsg);

    public abstract void onResponse(T response);

}
