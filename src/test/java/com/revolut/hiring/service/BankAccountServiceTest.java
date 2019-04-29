package com.revolut.hiring.service;

import com.revolut.hiring.bean.BankAccountInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class BankAccountServiceTest {

    private final BankAccountService testClass = new BankAccountService();

    @Test
    public void testAccountServices() {

        List<BankAccountInfo> accounts = testClass.getAccountsInfo();
        int accountsCount = accounts.size();

        try {
            testClass.createAccount("GBP");
            fail("Created account for unsupported currency");
        } catch (UnsupportedOperationException e) {}

        final BankAccountInfo usdAccount = testClass.createAccount("USD");
        assertEquals(accountsCount+1 , testClass.getAccountsInfo().size());
        assertEquals(usdAccount , testClass.getAccountInfo(usdAccount.getAccountNumber()));
        assertNull(testClass.getAccountInfo(32432));

        testClass.deleteAccount(usdAccount.getAccountNumber());
        assertEquals(accountsCount , testClass.getAccountsInfo().size());
        assertNull(testClass.getAccountInfo(usdAccount.getAccountNumber()));
    }
}
