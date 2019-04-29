package com.revolut.hiring.service;

import com.revolut.hiring.bean.BankAccountInfo;
import com.revolut.hiring.bean.Currency;
import com.revolut.hiring.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AccountTransactionServiceTest {

    private static final AccountTransactionService testClass = new AccountTransactionService();
    private static BankAccountInfo account1;
    private static BankAccountInfo account2;

    @BeforeAll
    public static void setup() throws NoSuchFieldException, IllegalAccessException {
        final Field field = testClass.getClass().getDeclaredField("accountService");
        field.setAccessible(true);
        final BankAccountService accountService = (BankAccountService) field.get(testClass);
        account1 = accountService.createAccount(Currency.USD.name());
        account2 = accountService.createAccount(Currency.EUR.name());
    }

    @Test
    public void testGetSinkAmount() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final Double inputAmount = new Double(100);
        final Currency sourceCurrency = Currency.USD;
        final Currency sinkCurrency = Currency.EUR;

        final Double expectedValue = Double.valueOf(89);

        Method method = testClass.getClass().getDeclaredMethod("getSinkAmount", Double.class, Currency.class, Currency.class);
        method.setAccessible(true);

        final Double actualValue = (Double) method.invoke(testClass, inputAmount, sourceCurrency, sinkCurrency);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testCredit() {

        account1.setBalance(0);
        final double balance = account1.getBalance();
        double creditAmount = 100;
        final long txnId = testClass.credit(account1.getAccountNumber(), creditAmount);
        assertEquals(balance+creditAmount, account1.getBalance());

        long invalidAccount = 3243;
        try {
            testClass.credit(invalidAccount, 0);
            fail("Credited amount into non-existing account");
        } catch (IllegalArgumentException e) {}

    }

    @Test
    public void testDebit() {

        account1.setBalance(100);

        final double balance = account1.getBalance();
        double debitAmount = 100;
        testClass.debit(account1.getAccountNumber(), debitAmount);
        assertEquals(balance-debitAmount, account1.getBalance());

        long invalidAccount = 3243;
        try {
            testClass.debit(invalidAccount, 0);
            fail("Debited amount into non-existing account");
        } catch (IllegalArgumentException e) {}

        try {
            debitAmount = account1.getBalance()+1;
            testClass.debit(account1.getAccountNumber(), debitAmount);
            fail("Debited amount greater than existing balance");
        } catch (InsufficientFundsException e) {}

    }

    @Test
    public void testTransfer() {
        account1.setBalance(100);
        account2.setBalance(0);

        double sourceBalance = account1.getBalance();
        double destinationBalance = account2.getBalance();
        try {
            testClass.transfer(account1.getAccountNumber(), account2.getAccountNumber(), 1000);
            fail("Transfered amount greater than balance");
        } catch (InsufficientFundsException e) {}

        double amountTransfered = 100;

        double expectedValueCredited = (100 * Currency.EUR.getFxRate())/Currency.USD.getFxRate();
        testClass.transfer(account1.getAccountNumber(), account2.getAccountNumber(), 100);

        assertEquals(sourceBalance-amountTransfered, account1.getBalance());
        assertEquals(destinationBalance+expectedValueCredited, account2.getBalance());
    }

}
