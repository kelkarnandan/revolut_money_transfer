package com.revolut.hiring.jaxrs.bean;

public class AccountDebitResponse {

    private Long txnId;
    private String status;
    private String time;
    private AccountDebitRequest request;

    public Long getTxnId() {
        return txnId;
    }

    public void setTxnId(Long txnId) {
        this.txnId = txnId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public AccountDebitRequest getRequest() {
        return request;
    }

    public void setRequest(AccountDebitRequest request) {
        this.request = request;
    }
}
