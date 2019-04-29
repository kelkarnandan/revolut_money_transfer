package com.revolut.hiring.jaxrs.bean;

public class MoneyTransferResponse {

    private Long txnId;
    private String status;
    private String time;
    private MoneyTransferRequest request;

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

    public MoneyTransferRequest getRequest() {
        return request;
    }

    public void setRequest(MoneyTransferRequest request) {
        this.request = request;
    }
}
