package com.revolut.hiring.service;

import com.revolut.hiring.bean.BankAccountInfo;
import com.revolut.hiring.bean.BankAccountTransactionInfo;
import com.revolut.hiring.bean.TransactionType;
import com.revolut.hiring.dao.AccountTxnDataService;
import com.revolut.hiring.exceptions.InsufficientFundsException;
import com.revolut.hiring.bean.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class AccountTransactionService {

    private final Logger logger = LoggerFactory.getLogger(AccountTransactionService.class);

    private final BankAccountService accountService = new BankAccountService();
    private final AccountTxnDataService txnDataService = AccountTxnDataService.getInstance();

    private static final AtomicLong txnIdCounter = new AtomicLong(1);

    public long credit(long account, double amount) {
        synchronized (AccountTransactionService.class) {

            final BankAccountInfo accountInfo = accountService.getAccountInfo(account);
            if (accountInfo == null) throw new IllegalArgumentException(String.format("Account #%d don't exit", account));

            final double balance = accountInfo.getBalance();

            final Date txnTime = new Date();
            try {
                final BankAccountTransactionInfo creditTxn = new BankAccountTransactionInfo.Builder()
                        .accountId(accountInfo.getAccountNumber()).amount(amount).time(txnTime).txnId(generateTxnId())
                        .txnType(TransactionType.CREDIT).build();

                txnDataService.addTransaction(creditTxn);
                accountInfo.setBalance(balance + amount);
                return creditTxn.getId();
            } catch (Throwable e) {
                accountInfo.setBalance(balance);
                throw e;
            }
        }
    }

    public long debit(long account, double amount) {
        synchronized (AccountTransactionService.class) {

            final BankAccountInfo accountInfo = accountService.getAccountInfo(account);
            if (accountInfo == null) throw new IllegalArgumentException(String.format("Account #%d don't exit", account));

            if (accountInfo.getBalance()==0 || accountInfo.getBalance() < amount)
                throw new InsufficientFundsException("Insufficient Funds");

            final double balance = accountInfo.getBalance();

            final Date txnTime = new Date();
            try {
                final BankAccountTransactionInfo debitTxn = new BankAccountTransactionInfo.Builder()
                        .accountId(accountInfo.getAccountNumber()).amount(amount).time(txnTime).txnId(generateTxnId())
                        .txnType(TransactionType.DEBIT).build();

                txnDataService.addTransaction(debitTxn);
                accountInfo.setBalance(balance - amount);
                return debitTxn.getId();
            } catch (Throwable e) {
                accountInfo.setBalance(balance);
                throw e;
            }
        }
    }

    public long transfer(long sourceAcnt, long sinkAcnt, double amount) {
        synchronized (AccountTransactionService.class) {

            final BankAccountInfo source = accountService.getAccountInfo(sourceAcnt);
            if (source == null) throw new IllegalArgumentException(String.format("Source Account #%d don't exit", sourceAcnt));

            final BankAccountInfo sink = accountService.getAccountInfo(sinkAcnt);
            if (sink == null) throw new IllegalArgumentException(String.format("Destination Account #%d don't exit", sinkAcnt));

            final double sourceBalance = source.getBalance();
            if (sourceBalance==0 || sourceBalance < amount) {
                throw new InsufficientFundsException("Insufficient Funds in Account #"+sourceAcnt);
            }
            final double sinkBalance = sink.getBalance();

            final double sourceDebit = amount;
            final double sinkCredit = getSinkAmount(amount, source.getCurrency(), sink.getCurrency());

            final Date txnTime = new Date();
            try {
                final BankAccountTransactionInfo sourceDebitTxn = new BankAccountTransactionInfo.Builder().txnId(generateTxnId())
                        .accountId(sourceAcnt).amount(sourceDebit).time(txnTime).txnType(TransactionType.DEBIT)
                        .build();
                final BankAccountTransactionInfo sinkCreditTxn = new BankAccountTransactionInfo.Builder().txnId(generateTxnId())
                        .accountId(sinkAcnt).amount(sinkCredit).time(txnTime).txnType(TransactionType.CREDIT)
                        .build();

                txnDataService.addTransaction(sourceDebitTxn);
                txnDataService.addTransaction(sinkCreditTxn);

                source.setBalance(sourceBalance - sourceDebit);
                sink.setBalance(sinkBalance + sinkCredit);
                return sourceDebitTxn.getId();
            } catch (Throwable e) {
                source.setBalance(sourceBalance);
                sink.setBalance(sinkBalance);
                throw e;
            }
        }
    }

    private Double getSinkAmount(Double amount, Currency fromCurrency, Currency toCurrency) {
        if (!fromCurrency.equals(toCurrency)) {
            float sourceFxRate = fromCurrency.getFxRate();
            float sinkFxRate = toCurrency.getFxRate();
            final double value = (amount / sourceFxRate) * sinkFxRate;
            final Double roundedValue = Double.valueOf(new DecimalFormat("#.##").format(value));
            logger.info("Conversion of {} {} to {} :: {} at exchange rate {}/{}",
                    amount, fromCurrency.name(), toCurrency.name(), roundedValue, sourceFxRate, sinkFxRate);
            return roundedValue;
        }

        return amount;
    }

    private long generateTxnId() {
        return txnIdCounter.getAndIncrement();
    }
}
