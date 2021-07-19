package com.lib_common.net;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class GZipRequest extends StringRequest {
    private final Response.Listener<String> mListener;
    private final Map<String, String> headers;

    public GZipRequest(int method, String url, Map<String, String> headers, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, headers, listener, errorListener);
        this.mListener=listener;
        this.headers=headers;
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String str1 = "";
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(
                    new ByteArrayInputStream(response.data));
            InputStreamReader inputStreamReader = new InputStreamReader(
                    gzipInputStream);
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            while (true) {
                String str2 = bufferedReader.readLine();
                if (str2 == null)
                    break;
                str1 = str1 + str2;
            }
            inputStreamReader.close();
            bufferedReader.close();
            gzipInputStream.close();
            return Response.success(str1, HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }
}
