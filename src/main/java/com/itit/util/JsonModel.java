package com.itit.util;

public class JsonModel {
    private long Count;
    private Object Datas;
    private String Msg;
    private boolean success;

    public long getCount() {
        return Count;
    }

    public void setCount(long count) {
        Count = count;
    }

    public Object getDatas() {
        return Datas;
    }

    public void setDatas(Object datas) {
        Datas = datas;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
