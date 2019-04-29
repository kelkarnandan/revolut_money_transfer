package com.revolut.hiring.dao;

import com.revolut.hiring.bean.BankAccountTransactionInfo;
import com.revolut.hiring.bean.TransactionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTxnDataServiceTest {

    private static AccountTxnDataService testClass = AccountTxnDataService.getInstance();

    @BeforeAll
    public static void setup() {
        testClass.addTransaction(new BankAccountTransactionInfo.Builder().txnId(1).txnType(TransactionType.CREDIT).accountId(1).amount(10).time(new Date()).build());
        testClass.addTransaction(new BankAccountTransactionInfo.Builder().txnId(2).txnType(TransactionType.CREDIT).accountId(1).amount(10).time(new Date()).build());
        testClass.addTransaction(new BankAccountTransactionInfo.Builder().txnId(3).txnType(TransactionType.CREDIT).accountId(1).amount(10).time(new Date()).build());
        testClass.addTransaction(new BankAccountTransactionInfo.Builder().txnId(4).txnType(TransactionType.CREDIT).accountId(2).amount(10).time(new Date()).build());
        testClass.addTransaction(new BankAccountTransactionInfo.Builder().txnId(5).txnType(TransactionType.CREDIT).accountId(2).amount(10).time(new Date()).build());
        testClass.addTransaction(new BankAccountTransactionInfo.Builder().txnId(6).txnType(TransactionType.CREDIT).accountId(2).amount(10).time(new Date()).build());
    }

    @Test
    public void testGetAllTransactions() {
        final int expectedCount = 3;
        List<BankAccountTransactionInfo> actualValue = testClass.getAllTransactions(1);
        assertNotNull(actualValue);
        assertEquals(expectedCount, actualValue.size());

        actualValue = testClass.getAllTransactions(3);
        assertNotNull(actualValue);
        assertTrue(actualValue.isEmpty());
    }

    @Test
    public void testGetTransactionById() {
        long txnId = 3;

        BankAccountTransactionInfo actualValue = testClass.getTransactionById(txnId);
        assertNotNull(actualValue);
        assertEquals(txnId, actualValue.getId());

        actualValue = testClass.getTransactionById(10);
        assertNull(actualValue);
    }

    @Test
    public void testGetAllTransactionsByFromDate() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        final Date fromTime = cal.getTime();
        long account = 1;

        List<BankAccountTransactionInfo> actualValue = testClass.getAllTransactions(account, fromTime);
        assertNotNull(actualValue);
        assertEquals(3, actualValue.size());

        actualValue = testClass.getAllTransactions(account, new Date());
        assertNotNull(actualValue);
        assertEquals(0, actualValue.size());
    }

    @Test
    public void testGetAllTransactionsByFromDateAndEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        final Date time1 = cal.getTime();
        long account = 1;

        List<BankAccountTransactionInfo> actualValue = testClass.getAllTransactions(account, time1, new Date());
        assertNotNull(actualValue);
        assertEquals(3, actualValue.size());

        cal.add(Calendar.MINUTE, -5);
        final Date time2 = cal.getTime();

        actualValue = testClass.getAllTransactions(account, time2, time1);
        assertNotNull(actualValue);
        assertEquals(0, actualValue.size());
    }
}
