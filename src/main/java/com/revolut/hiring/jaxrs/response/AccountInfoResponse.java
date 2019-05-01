package com.revolut.hiring.jaxrs.response;

import static com.revolut.hiring.util.DateUtil.getDateString;

import com.revolut.hiring.bean.BankAccountInfo;

public class AccountInfoResponse {

    private long accountNumber;
    private double balance;
    private String currency;
    private String creationDate;

    public AccountInfoResponse(long accountNumber, double balance, String currency,
            String creationDate) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.creationDate = creationDate;
    }

    public AccountInfoResponse(BankAccountInfo accountInfo) {
        accountNumber = accountInfo.getAccountNumber();
        balance = accountInfo.getBalance();
        currency = accountInfo.getCurrency().name();
        creationDate = getDateString(accountInfo.getCreationDate());
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
