package com.revolut.hiring.bean;

import java.util.Date;

public class BankAccountTransactionInfo {

    private long id;
    private long accountId;
    private TransactionType txnType;
    private double amount;
    private Date txnDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public TransactionType getTxnType() {
        return txnType;
    }

    public void setTxnType(TransactionType txnType) {
        this.txnType = txnType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public static class Builder {

        private final BankAccountTransactionInfo txn = new BankAccountTransactionInfo();

        public Builder() {}

        public BankAccountTransactionInfo.Builder txnId(long id) {
            txn.setId(id);
            return this;
        }
        public BankAccountTransactionInfo.Builder amount(double amount) {
            txn.setAmount(amount);
            return this;
        }

        public BankAccountTransactionInfo.Builder time(Date txnTime) {
            txn.setTxnDate(txnTime);
            return this;
        }

        public BankAccountTransactionInfo.Builder accountId(long accountId) {
            txn.setAccountId(accountId);
            return this;
        }

        public BankAccountTransactionInfo.Builder txnType(TransactionType type) {
            txn.setTxnType(type);
            return this;
        }

        public BankAccountTransactionInfo build() {
            return txn;
        }
    }
}
