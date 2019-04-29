package com.revolut.hiring.jaxrs.bean;

import static com.revolut.hiring.util.DateUtil.getDateString;

import com.revolut.hiring.bean.BankAccountTransactionInfo;

public class AccountTransaction {

    private long txnId;
    private long accountId;
    private String txnType;
    private double amount;
    private String txnDate;

    public AccountTransaction(long txnId, long accountId, String txnType, double amount,
            String txnDate) {
        this.txnId = txnId;
        this.accountId = accountId;
        this.txnType = txnType;
        this.amount = amount;
        this.txnDate = txnDate;
    }

    public AccountTransaction(BankAccountTransactionInfo txnInfo) {
        txnId = txnInfo.getId();
        accountId = txnInfo.getAccountId();
        txnType = txnInfo.getTxnType().name();
        amount = txnInfo.getAmount();
        txnDate = getDateString(txnInfo.getTxnDate());
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
