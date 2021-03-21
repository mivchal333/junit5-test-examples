package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class BankImplTest {
    private Bank bank;
    long invalidId = 99999L;

    @BeforeEach
    void init() {
        this.bank = new BankImpl();
    }

    @Test
    void shouldCreateAccount() {
        String address = "address";
        String name = "name";

        Assertions.assertNull(bank.findAccount(name, address));

        bank.createAccount(name, address);

        Assertions.assertNotNull(bank.findAccount(name, address));
    }

    @Test
    void findAccount() {
        String address1 = "address1";
        String name1 = "name1";
        bank.createAccount(name1, address1);
        String address2 = "address2";
        String name2 = "name2";
        Long account2 = bank.createAccount(name2, address2);

        Long found = bank.findAccount(name2, address2);
        Assertions.assertEquals(account2, found);

        Assertions.assertNull(bank.findAccount(name2, "not existing"));
    }

    @Test
    void deposit() {
        // create account
        String address1 = "address1";
        String name1 = "name1";
        Long account = bank.createAccount(name1, address1);

        // deposit 1
        BigDecimal deposit1 = BigDecimal.valueOf(500);
        bank.deposit(account, deposit1);
        Assertions.assertEquals(bank.getBalance(account), deposit1);
        // deposit 2
        BigDecimal deposit2 = BigDecimal.valueOf(1000);
        bank.deposit(account, deposit2);
        Assertions.assertEquals(bank.getBalance(account), deposit1.add(deposit2));
        // deposit 3
        BigDecimal deposit3 = BigDecimal.valueOf(2);
        bank.deposit(account, deposit3);
        Assertions.assertEquals(bank.getBalance(account), deposit1.add(deposit2).add(deposit3));
        // throw exception when wrong account id
        Assertions.assertThrows(Bank.AccountIdException.class, () -> {
            bank.deposit(invalidId, BigDecimal.ONE);
        });
    }

    @Test
    void getBalance() {
        // create account
        String address1 = "address1";
        String name1 = "name1";
        Long account = bank.createAccount(name1, address1);

        // check initial balance
        Assertions.assertEquals(bank.getBalance(account), BigDecimal.ZERO);

        // deposit
        BigDecimal deposit1 = BigDecimal.valueOf(500);
        bank.deposit(account, deposit1);

        // check new balance
        Assertions.assertEquals(bank.getBalance(account), deposit1);


        // throw exception when wrong account id
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.getBalance(invalidId));

    }

    @Test
    void withdraw() {
        // create account
        String address1 = "address1";
        String name1 = "name1";
        Long account = bank.createAccount(name1, address1);

        // deposit
        BigDecimal deposit1 = BigDecimal.valueOf(500);
        bank.deposit(account, deposit1);

        Assertions.assertEquals(deposit1, bank.getBalance(account));
        bank.withdraw(account, deposit1);
        Assertions.assertEquals(BigDecimal.ZERO, bank.getBalance(account));

        // AccountIdException when account doesn't exist
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.withdraw(2L, BigDecimal.ONE));
        // InsufficientFundsException when amount bigger than available
        Assertions.assertThrows(Bank.InsufficientFundsException.class, () ->
                bank.withdraw(account, BigDecimal.ONE.add(deposit1)));

    }

    @Test
    void transfer() {
        // create accounts
        Long account1 = bank.createAccount("name1", "address1");
        Long account2 = bank.createAccount("name1", "address1");

        // deposit
        BigDecimal deposit1 = BigDecimal.valueOf(500);
        bank.deposit(account1, deposit1);

        Assertions.assertEquals(deposit1, bank.getBalance(account1));
        Assertions.assertEquals(BigDecimal.ZERO, bank.getBalance(account2));

        bank.transfer(account1, account2, deposit1);

        Assertions.assertEquals(deposit1, bank.getBalance(account2));
        Assertions.assertEquals(BigDecimal.ZERO, bank.getBalance(account1));

        // AccountIdException when source account doesn't exist
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.transfer(invalidId, account1, BigDecimal.ONE));
        // AccountIdException when target account doesn't exist
        Assertions.assertThrows(Bank.AccountIdException.class, () -> bank.transfer(account1, invalidId, BigDecimal.ONE));
        // InsufficientFundsException when amount bigger than available
        Assertions.assertThrows(Bank.InsufficientFundsException.class, () ->
                bank.transfer(account1, account2, BigDecimal.TEN));
    }
}
