package com.revolut.hiring.service;

import static org.junit.Assert.*;

import java.util.List;

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
            fail("Created account for unsupported currency");
        }
        catch (UnsupportedOperationException e) {
        }

        final BankAccountInfo usdAccount = bankAccountService.createAccount("USD");
        assertEquals(accountsCount + 1, bankAccountService.getAccountsInfo().size());
        assertEquals(usdAccount, bankAccountService.getAccountInfo(usdAccount.getAccountNumber()));
        assertNull(bankAccountService.getAccountInfo(32432));

        bankAccountService.deleteAccount(usdAccount.getAccountNumber());
        assertEquals(accountsCount, bankAccountService.getAccountsInfo().size());
        assertNull(bankAccountService.getAccountInfo(usdAccount.getAccountNumber()));
    }
}
