package com.revolut.hiring.jaxrs.bean;

import com.revolut.hiring.bean.BankAccountTransactionInfo;

import static com.revolut.hiring.util.DateUtil.getDateString;

public class AccountTransaction {

    private long txnId;
    private long accountId;
    private String txnType;
    private double amount;
    private String txnDate;

    public AccountTransaction(long txnId, long accountId, String txnType, double amount, String txnDate) {
        this.txnId = txnId;
        this.accountId = accountId;
        this.txnType = txnType;
        this.amount = amount;
        this.txnDate = txnDate;
    }

    public AccountTransaction(BankAccountTransactionInfo txnInfo) {
        this.txnId = txnInfo.getId();
        this.accountId = txnInfo.getAccountId();
        this.txnType = txnInfo.getTxnType().name();
        this.amount = txnInfo.getAmount();
        this.txnDate = getDateString(txnInfo.getTxnDate());
    }

    public long getTxnId() {
        return txnId;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getTxnType() {
        return txnType;
    }

    public double getAmount() {
        return amount;
    }

    public String getTxnDate() {
        return txnDate;
    }
}
