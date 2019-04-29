package com.revolut.hiring.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revolut.hiring.bean.BankAccountInfo;
import com.revolut.hiring.bean.Currency;

public class AccountInfoDataServiceTest {

    private static AccountInfoDataService accountInfoDataService = AccountInfoDataService.getInstance();

    private static int accountsCount = 0;

    @BeforeAll
    public static void setup() {
        accountInfoDataService.addAccount(new BankAccountInfo.Builder().setAccountNumber(1).setActive(true)
                .setBalance(0).setCreationDate(new Date()).setCurrency(Currency.USD).build());
        accountInfoDataService.addAccount(new BankAccountInfo.Builder().setAccountNumber(2).setActive(true)
                .setBalance(0).setCreationDate(new Date()).setCurrency(Currency.INR).build());
        accountInfoDataService.addAccount(new BankAccountInfo.Builder().setAccountNumber(3).setActive(true)
                .setBalance(0).setCreationDate(new Date()).setCurrency(Currency.USD).build());
        accountsCount = 3;
    }

    @Test
    public void testCreateAccountThrowsException() {
        long accountNum = 4;
        accountsCount = accountInfoDataService.listActiveAccountInfo().size();
        accountInfoDataService.addAccount(new BankAccountInfo.Builder().setAccountNumber(accountNum)
                .setActive(true).setBalance(0).setCreationDate(new Date()).setCurrency(Currency.USD)
                .build());

        assertEquals(accountsCount + 1, accountInfoDataService.listActiveAccountInfo().size());
        accountsCount = accountInfoDataService.listActiveAccountInfo().size();

        try {
            accountInfoDataService.addAccount(new BankAccountInfo.Builder().setAccountNumber(accountNum)
                    .setActive(true).setBalance(0).setCreationDate(new Date())
                    .setCurrency(Currency.USD).build());
            fail("Expected exception for Account already existing");
        }
        catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
            assertTrue(e.getMessage().contains("Account already exist #4"));
        }
    }

    @Test
    public void testDeleteAccount() {
        long accountNum = 4;

        List<BankAccountInfo> activeAccountInfos = accountInfoDataService.listActiveAccountInfo();
        List<BankAccountInfo> inactiveAccountInfos = accountInfoDataService.listInactiveAccountInfo();

        accountInfoDataService.deleteAccount(accountNum);
        assertEquals(activeAccountInfos.size() - 1, accountInfoDataService.listActiveAccountInfo().size());
        assertEquals(inactiveAccountInfos.size() + 1, accountInfoDataService.listInactiveAccountInfo().size());

        activeAccountInfos = accountInfoDataService.listActiveAccountInfo();
        inactiveAccountInfos = accountInfoDataService.listInactiveAccountInfo();

        accountInfoDataService.deleteAccount(accountNum);
        assertEquals(activeAccountInfos.size(), accountInfoDataService.listActiveAccountInfo().size());
        assertEquals(inactiveAccountInfos.size(), accountInfoDataService.listInactiveAccountInfo().size());
    }

}
