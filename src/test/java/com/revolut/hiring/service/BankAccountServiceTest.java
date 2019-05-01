package com.revolut.hiring.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.revolut.hiring.bean.BankAccountInfo;

public class BankAccountServiceTest {

    private final BankAccountService bankAccountService = new BankAccountService();

    @Test
    public void testAccountServices() {

        List<BankAccountInfo> accounts = bankAccountService.getAccountsInfo();
        int accountsCount = accounts.size();

        try {
            bankAccountService.createAccount("AUD");
            Assertions.fail("Created account for unsupported currency");
        }
        catch (UnsupportedOperationException e) {
        }

        final BankAccountInfo usdAccount = bankAccountService.createAccount("USD");
        Assertions.assertEquals(accountsCount + 1, bankAccountService.getAccountsInfo().size());
        Assertions.assertEquals(usdAccount,
                bankAccountService.getAccountInfo(usdAccount.getAccountNumber()));
        Assertions.assertNull(bankAccountService.getAccountInfo(32432));

        bankAccountService.deleteAccount(usdAccount.getAccountNumber());
        Assertions.assertEquals(accountsCount, bankAccountService.getAccountsInfo().size());
        Assertions.assertNull(bankAccountService.getAccountInfo(usdAccount.getAccountNumber()));
    }
}
