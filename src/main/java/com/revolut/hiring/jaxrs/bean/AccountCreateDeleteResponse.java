package com.revolut.hiring.jaxrs.bean;

public class AccountCreateDeleteResponse {

    private String message;
    private String time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
