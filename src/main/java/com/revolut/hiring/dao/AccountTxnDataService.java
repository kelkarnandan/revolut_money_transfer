package com.revolut.hiring.dao;

import com.revolut.hiring.bean.BankAccountTransactionInfo;

import java.util.*;
import java.util.stream.Collectors;

public class AccountTxnDataService {

    private List<BankAccountTransactionInfo> transactions = new LinkedList<>();

    private static final AccountTxnDataService INSTANCE = new AccountTxnDataService();

    public static AccountTxnDataService getInstance() { return INSTANCE; }

    private AccountTxnDataService() {}

    public void addTransaction(BankAccountTransactionInfo txnInfo) { transactions.add(txnInfo); }

    public List<BankAccountTransactionInfo> getAllTransactions(long accountId) {
        return transactions.stream().filter(t -> t.getAccountId()== accountId)
                .sorted(Comparator.comparing(BankAccountTransactionInfo::getTxnDate))
                .collect(Collectors.toList());
    }

    public BankAccountTransactionInfo getTransactionById(long txnId) {
        final Optional<BankAccountTransactionInfo> txn = transactions.stream().filter(t -> t.getId() == txnId).findAny();
        return txn.isPresent() ? txn.get() : null;
    }

    public List<BankAccountTransactionInfo> getAllTransactions(long accountId, final Date fromDate) {
        return transactions.stream()
                .filter(t -> accountId == t.getAccountId())
                .filter(t -> t.getTxnDate().after(fromDate) || t.getTxnDate().equals(fromDate))
                .sorted(Comparator.comparing(BankAccountTransactionInfo::getTxnDate))
                .collect(Collectors.toList());
    }

    public List<BankAccountTransactionInfo> getAllTransactions(long accountId, final Date fromDate, final Date endDate) {
        return transactions.stream()
                .filter(t -> accountId == t.getAccountId())
                .filter(t -> (t.getTxnDate().after(fromDate) || t.getTxnDate().equals(fromDate)) &&
                             (t.getTxnDate().before(endDate) || t.getTxnDate().equals(endDate)))
                .sorted(Comparator.comparing(BankAccountTransactionInfo::getTxnDate))
                .collect(Collectors.toList());
    }

}
