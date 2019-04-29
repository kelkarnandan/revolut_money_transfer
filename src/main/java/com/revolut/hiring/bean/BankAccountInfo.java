package com.revolut.hiring.bean;

import java.util.Date;

public class BankAccountInfo {

    private long accountNumber;
    private double balance;
    private Currency currency;
    private Date creationDate;
    private boolean isActive;

    public BankAccountInfo(long accountNumber, double balance, Currency currency, Date creationDate, boolean isActive) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.creationDate = creationDate;
        this.isActive = isActive;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Currency getCurrency() {
        return currency;
    }

    public  static class Builder {

        private long accountNumber;
        private double balance;
        private Currency currency;
        private Date creationDate;
        private boolean isActive;

        public Builder setAccountNumber(long accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder setBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public Builder setActive(boolean active) {
            isActive = active;
            return this;
        }

        public Builder setCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public BankAccountInfo build() {
            return new BankAccountInfo(accountNumber, balance, currency, creationDate, isActive);
        }
    }
}
