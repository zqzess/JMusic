package com.jmusic.bean;

import java.util.List;

public class MusicResource {
    int code;
    String msg;
    String reqId;
    MusicResourceData data;
    String profiled;
    long curTime;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public MusicResourceData getData() {
        return data;
    }

    public void setData(MusicResourceData data) {
        this.data = data;
    }

    public String getProfiled() {
        return profiled;
    }

    public void setProfiled(String profiled) {
        this.profiled = profiled;
    }

    public long getCurTime() {
        return curTime;
    }

    public void setCurTime(long curTime) {
        this.curTime = curTime;
    }
}
