package com.revolut.hiring.service;

import com.revolut.hiring.bean.BankAccountInfo;
import com.revolut.hiring.dao.AccountInfoDataService;
import com.revolut.hiring.bean.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BankAccountService {

    private final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    private final AccountInfoDataService accountInfoDataService = AccountInfoDataService.getInstance();

    private static final AtomicLong accountNumCounter = new AtomicLong(1);

    public BankAccountInfo createAccount(String currency) {
        final Currency cur = Currency.valueByName(currency);
        if (cur == null) throw new UnsupportedOperationException(currency + " accounts not supported");

        final BankAccountInfo.Builder newAccountBuilder = new BankAccountInfo.Builder().setActive(true).setBalance(0).setCreationDate(new Date())
                                        .setAccountNumber(accountNumCounter.incrementAndGet()).setCurrency(cur);
        boolean isAccountCreated = false;
        BankAccountInfo newAccount = newAccountBuilder.build();
        do {
            try {
                accountInfoDataService.addAccount(newAccount);
                isAccountCreated = true;
            } catch (UnsupportedOperationException e) {
                newAccount = newAccountBuilder.setAccountNumber(accountNumCounter.incrementAndGet()).build();
            }
        } while(!isAccountCreated);

        logger.info("Created account {} with currency {}", newAccount.getAccountNumber(), currency);
        return newAccount;
    }

    public void deleteAccount(long id) {
        final BankAccountInfo accountInfo = accountInfoDataService.getAccountInfo(id);
        if (accountInfo != null && accountInfo.isActive()) {
            accountInfoDataService.deleteAccount(id);
            logger.info("Deleted account {} ", accountInfo.getAccountNumber());
        }
    }

    public BankAccountInfo getAccountInfo(long id) {
        final BankAccountInfo accountInfo = accountInfoDataService.getAccountInfo(id);
        if (accountInfo != null && accountInfo.isActive()) {
            return accountInfo;
        }
        return null;
    }

    public List<BankAccountInfo> getAccountsInfo() {
        return accountInfoDataService.listActiveAccountInfo();
    }
}
