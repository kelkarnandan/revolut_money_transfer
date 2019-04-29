package com.revolut.hiring.jaxrs.bean;

public class AccountCreditResponse {

    private Long txnId;
    private String status;
    private String time;
    private AccountCreditRequest request;

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

    public AccountCreditRequest getRequest() {
        return request;
    }

    public void setRequest(AccountCreditRequest request) {
        this.request = request;
    }
}
