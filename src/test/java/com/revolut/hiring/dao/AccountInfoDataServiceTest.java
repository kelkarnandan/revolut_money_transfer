package com.revolut.hiring.dao;

import com.revolut.hiring.bean.BankAccountInfo;
import com.revolut.hiring.bean.Currency;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AccountInfoDataServiceTest {

    private static AccountInfoDataService testClass = AccountInfoDataService.getInstance();

    private static int accountsCount = 0;

    @BeforeAll
    public static void setup() {
        testClass.addAccount(new BankAccountInfo.Builder().setAccountNumber(1).setActive(true).setBalance(0).setCreationDate(new Date()).setCurrency(Currency.USD).build());
        testClass.addAccount(new BankAccountInfo.Builder().setAccountNumber(2).setActive(true).setBalance(0).setCreationDate(new Date()).setCurrency(Currency.INR).build());
        testClass.addAccount(new BankAccountInfo.Builder().setAccountNumber(3).setActive(true).setBalance(0).setCreationDate(new Date()).setCurrency(Currency.USD).build());
        accountsCount = 3;
    }

    @Test
    public void testCreateAccount() {
        long accountNum = 4;
        accountsCount = testClass.listActiveAccountInfo().size();
        testClass.addAccount(new BankAccountInfo.Builder().setAccountNumber(accountNum).setActive(true).setBalance(0).setCreationDate(new Date()).setCurrency(Currency.USD).build());

        assertEquals(accountsCount+1, testClass.listActiveAccountInfo().size());
        accountsCount = testClass.listActiveAccountInfo().size();

        try {
            testClass.addAccount(new BankAccountInfo.Builder().setAccountNumber(accountNum).setActive(true).setBalance(0).setCreationDate(new Date()).setCurrency(Currency.USD).build());
            fail("Expected exception for Account already existing");
        } catch (UnsupportedOperationException e) { }
    }

    @Test
    public void testDeleteAccount() {
        long accountNum = 4;

        List<BankAccountInfo> activeAccountInfos = testClass.listActiveAccountInfo();
        List<BankAccountInfo> inactiveAccountInfos = testClass.listInactiveAccountInfo();

        testClass.deleteAccount(accountNum);
        assertEquals(activeAccountInfos.size()-1, testClass.listActiveAccountInfo().size());
        assertEquals(inactiveAccountInfos.size()+1, testClass.listInactiveAccountInfo().size());

        activeAccountInfos = testClass.listActiveAccountInfo();
        inactiveAccountInfos = testClass.listInactiveAccountInfo();

        testClass.deleteAccount(accountNum);
        assertEquals(activeAccountInfos.size(), testClass.listActiveAccountInfo().size());
        assertEquals(inactiveAccountInfos.size(), testClass.listInactiveAccountInfo().size());
    }

}
