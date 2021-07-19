package com.lib_common.net;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;

import com.android.volley.Response;

import java.util.Map;

public class StringRequest extends com.android.volley.toolbox.StringRequest {
    private final Map<String, String> headers;

    public StringRequest(int method, String url, Map<String, String> headers, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.headers=headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

}
