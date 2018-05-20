package com.marslocate.network.request;

import com.marslocate.volley.AuthFailureError;
import com.marslocate.volley.Response;
import com.marslocate.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaoh on 2018/5/18.
 */

public class StringRequestWithAuth extends StringRequest {

    private String mThird_key;

    public StringRequestWithAuth(int method,
                                 String url,
                                 Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {

        super(method, url, listener, errorListener);
    }

    public StringRequestWithAuth(String url,
                                 Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("third_key", mThird_key);
        return params;
    }


    public void setThird_key(String third_key) {
        this.mThird_key = third_key;
    }
}
