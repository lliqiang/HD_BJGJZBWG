package com.example.lenovo.hd_beijing_meseum.model;

/**
 * Created by lenovo on 2016/11/10.
 */

public class ResponseBody {

    /**
     * status : 1
     * data : AND1000000008
     * msg : 操作成功
     */

    private int status;
    private String data;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "status=" + status +
                ", data='" + data + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
