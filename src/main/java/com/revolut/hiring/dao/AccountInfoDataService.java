package com.revolut.hiring.dao;

import com.revolut.hiring.bean.BankAccountInfo;

import java.util.*;
import java.util.stream.Collectors;

public class AccountInfoDataService {

    private final List<BankAccountInfo> accounts = new LinkedList<>();

    private static final AccountInfoDataService INSTANCE = new AccountInfoDataService();

    public static AccountInfoDataService getInstance() {
        return INSTANCE;
    }

    private AccountInfoDataService() {
    }

    public void addAccount(BankAccountInfo accountInfo) {
        if (this.getAccountInfo(accountInfo.getAccountNumber()) != null) {
            throw new UnsupportedOperationException("Account already exist #"+accountInfo.getAccountNumber());
        }
        accounts.add(accountInfo);
    }

    public void deleteAccount(long id) {
        final Optional<BankAccountInfo> accnt = accounts.stream().filter(a -> id == a.getAccountNumber()).findAny();
        if (accnt.isPresent() && accnt.get().isActive()) {
            accnt.get().setActive(false);
        }
    }

    public final List<BankAccountInfo> listActiveAccountInfo() {
        return accounts.stream().filter(BankAccountInfo::isActive)
                .sorted(Comparator.comparing(BankAccountInfo::getCreationDate))
                .collect(Collectors.toList());
    }

    public final List<BankAccountInfo> listInactiveAccountInfo() {
        return accounts.stream().filter(a -> !a.isActive())
                .sorted(Comparator.comparing(BankAccountInfo::getCreationDate))
                .collect(Collectors.toList());
    }

    public final BankAccountInfo getAccountInfo(long id) {
        final Optional<BankAccountInfo> bankAccountInfo = accounts.stream().filter(a -> id == a.getAccountNumber()).findAny();
        return bankAccountInfo.isPresent() ? bankAccountInfo.get() : null;
    }

}
