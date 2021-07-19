package com.lib_common.bean;

import java.io.Serializable;

public class MessageEvent<T> implements Serializable {
    private String code;
    private T data;

    public MessageEvent(String code) {
        this.code = code;
    }

    public MessageEvent(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
