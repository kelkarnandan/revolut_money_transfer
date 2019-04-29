package com.revolut.hiring.jaxrs.bean;

import com.revolut.hiring.bean.BankAccountInfo;

import static com.revolut.hiring.util.DateUtil.getDateString;

public class AccountInfoResponse {

    private long accountNumber;
    private double balance;
    private String currency;
    private String creationDate;

    public AccountInfoResponse(long accountNumber, double balance, String currency, String creationDate) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.creationDate = creationDate;
    }

    public AccountInfoResponse(BankAccountInfo accountInfo) {
        this.accountNumber = accountInfo.getAccountNumber();
        this.balance = accountInfo.getBalance();
        this.currency = accountInfo.getCurrency().name();
        this.creationDate = getDateString(accountInfo.getCreationDate());
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCreationDate() {
        return creationDate;
    }
}
