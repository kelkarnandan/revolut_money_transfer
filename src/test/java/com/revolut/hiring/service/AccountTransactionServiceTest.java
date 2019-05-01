package com.revolut.hiring.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revolut.hiring.bean.BankAccountInfo;
import com.revolut.hiring.bean.Currency;
import com.revolut.hiring.exceptions.InsufficientFundsException;

public class AccountTransactionServiceTest {

    private static final AccountTransactionService accountTxnService = new AccountTransactionService();
    private static BankAccountInfo account1;
    private static BankAccountInfo account2;

    @BeforeAll
    public static void setup() throws NoSuchFieldException, IllegalAccessException {
        final Field field = accountTxnService.getClass().getDeclaredField("accountService");
        field.setAccessible(true);
        final BankAccountService accountService = (BankAccountService) field.get(accountTxnService);
        account1 = accountService.createAccount(Currency.USD.name());
        account2 = accountService.createAccount(Currency.EUR.name());
    }

    @Test
    public void testGetSinkAmount()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final Double inputAmount = new Double(100);
        final Currency sourceCurrency = Currency.USD;
        final Currency sinkCurrency = Currency.EUR;

        final Double expectedValue = Double.valueOf(89);

        Method method = accountTxnService.getClass().getDeclaredMethod("getSinkAmount",
                Double.class, Currency.class, Currency.class);
        method.setAccessible(true);

        final Double actualValue = (Double) method.invoke(accountTxnService, inputAmount,
                sourceCurrency, sinkCurrency);

        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testCreditSuccess() {

        account1.setBalance(0);
        final double balance = account1.getBalance();
        double creditAmount = 100;
        accountTxnService.credit(account1.getAccountNumber(), creditAmount);
        Assertions.assertEquals(balance + creditAmount, account1.getBalance());

    }

    @Test
    public void testCreditIllegalArgumentException() {

        account1.setBalance(0);
        long invalidAccount = 3243;
        try {
            accountTxnService.credit(invalidAccount, 0);
            Assertions.fail("Credited amount into non-existing account");
        }
        catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
        }

    }

    @Test
    public void testDebitSuccess() throws InsufficientFundsException {
        account1.setBalance(100);

        final double balance = account1.getBalance();
        double debitAmount = 100;

        accountTxnService.debit(account1.getAccountNumber(), debitAmount);
        Assertions.assertEquals(balance - debitAmount, account1.getBalance());

    }

    @Test
    public void testDebitIllegalArgumentException() {

        account1.setBalance(100);

        try {
            long invalidAccount = 3243;
            accountTxnService.debit(invalidAccount, 0);
            Assertions.fail("Debited amount into non-existing account");
        }
        catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException);
        }

    }

    @Test
    public void testDebitInsufficientFundsException() {

        account1.setBalance(100);

        double debitAmount = 100;

        try {
            debitAmount = account1.getBalance() + 1;
            accountTxnService.debit(account1.getAccountNumber(), debitAmount);
            Assertions.fail("Debited amount greater than existing balance");
        }
        catch (InsufficientFundsException e) {
            Assertions.assertTrue(e instanceof InsufficientFundsException);
        }

    }

    @Test
    public void testTransferMultipleCase() {
        account1.setBalance(100);
        account2.setBalance(0);

        double sourceBalance = account1.getBalance();
        double destinationBalance = account2.getBalance();
        try {
            accountTxnService.transfer(account1.getAccountNumber(), account2.getAccountNumber(),
                    1000);
            Assertions.fail("Transfered amount greater than balance");
        }
        catch (InsufficientFundsException e) {
        }

        try {
            double amountTransfered = 100;

            double expectedValueCredited = (100 * Currency.EUR.getFxRate())
                    / Currency.USD.getFxRate();
            accountTxnService.transfer(account1.getAccountNumber(), account2.getAccountNumber(),
                    100);

            Assertions.assertEquals(sourceBalance - amountTransfered, account1.getBalance());
            Assertions.assertEquals(destinationBalance + expectedValueCredited,
                    account2.getBalance());
        }
        catch (InsufficientFundsException e) {
        }
    }

}
