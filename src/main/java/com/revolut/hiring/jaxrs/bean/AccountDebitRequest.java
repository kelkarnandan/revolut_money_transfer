package com.revolut.hiring.jaxrs.bean;

public class AccountDebitRequest {

    private Long account;
    private Double amount;

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
