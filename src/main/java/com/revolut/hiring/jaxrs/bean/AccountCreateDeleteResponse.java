package com.revolut.hiring.jaxrs.bean;

import java.util.Date;

public class AccountCreateDeleteResponse {

    private String msg;
    private String time;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
