package dao;

import model.Account;
import model.AccountOperation;
import model.OperationType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class AccountDaoTest {

    private AccountOperationDao accountOperationDao;
    private AccountDao accountDao;


    @BeforeEach
    private void setUp() {
        accountOperationDao = new AccountOperationDaoImpl();
        accountDao = new AccountDaoImpl();
    }

    @AfterEach
    private void clear() {
        accountOperationDao.findAll()
                .forEach(accountOperation -> accountOperationDao.delete(accountOperation));
        accountDao.findAll()
                .forEach(account -> accountDao.delete(account));
    }


    @Test
    void save() {
        Account account = new Account("name1", "address1");
        accountDao.save(account);
        Assertions.assertNotNull(account.getId());
    }

    @Test
    void update() {
        Account account = new Account("name1", "address1");
        accountDao.save(account);
        Assertions.assertNotNull(account.getId());

        account.setName("name2");
        account.setAddress("address2");
        account.setBalance(BigDecimal.TEN);

        accountDao.update(account);

        Account updatedAccount = accountDao.findById(account.getId()).get();
        Assertions.assertEquals(account.getId(), updatedAccount.getId());
        Assertions.assertEquals(account.getName(), updatedAccount.getName());
        Assertions.assertEquals(account.getAddress(), updatedAccount.getAddress());
        Assertions.assertEquals(account.getBalance(), updatedAccount.getBalance());
    }

    @Test
    void delete() {
        Account account = new Account("name1", "address1");
        accountDao.save(account);
        accountDao.delete(account);

        Account accountNotExisting = accountDao.findById(account.getId()).orElse(null);

        Assertions.assertNull(accountNotExisting);
    }

    @Test
    void findExistingById() {
        Account account = new Account("name1", "address1");
        accountDao.save(account);

        Optional<Account> existingByIdOpt = accountDao.findById(account.getId());
        Assertions.assertTrue(existingByIdOpt.isPresent());
    }

    @Test
    void tryFindNonExistingById() {
        Optional<Account> existingByIdOpt = accountDao.findById(99L);
        Assertions.assertFalse(existingByIdOpt.isPresent());
    }

    @Test
    void findAll() {
        List<Account> accounts = Arrays.asList(
                new Account("name1", "address1"),
                new Account("name2", "address2")
        );
        accounts.forEach(account -> accountDao.save(account));

        List<Account> foundAll = accountDao.findAll();

        Assertions.assertEquals(accounts.size(), foundAll.size());
    }

    @Test
    void shouldFindSavedAccount() {
        String name1 = "name1";
        String address1 = "address1";
        Account account = new Account(name1, address1);
        accountDao.save(account);

        List<Account> byNameAndAddress = accountDao.findByNameAndAddress(name1, address1);

        Assertions.assertFalse(byNameAndAddress.isEmpty());
    }

    @Test
    void shouldNotFindAnyAccount() {

        List<Account> byNameAndAddress = accountDao.findByNameAndAddress("ex", "ex");

        Assertions.assertTrue(byNameAndAddress.isEmpty());
    }

    @Test
    void shouldSearchAccountByName() {
        String name1 = "lorem";
        String address1 = "address1";
        Account account = new Account(name1, address1);
        accountDao.save(account);

        List<Account> byNameAndAddress = accountDao.searchByName("lore");

        Assertions.assertEquals(1, byNameAndAddress.size());
    }

    @Test
    void shouldFindAccountByBalanceRange() {
        String name1 = "lorem";
        String address1 = "address1";
        Account account = new Account(name1, address1);
        account.setBalance(BigDecimal.TEN);
        accountDao.save(account);

        Account account2 = new Account(name1, address1);
        account2.setBalance(BigDecimal.ONE);
        accountDao.save(account2);

        List<Account> found = accountDao.findByBalanceRange(BigDecimal.valueOf(2), BigDecimal.valueOf(11));

        Assertions.assertEquals(1, found.size());
    }

    @Test
    void shouldFindAccountsByBalanceDescending() {
        Account account = new Account("n1", "a1");
        account.setBalance(BigDecimal.TEN);
        accountDao.save(account);

        Account account2 = new Account("n2", "a2");
        account2.setBalance(BigDecimal.ONE);
        accountDao.save(account2);

        Account account3 = new Account("n3", "a3");
        account3.setBalance(BigDecimal.valueOf(99));
        accountDao.save(account3);

        List<Account> found = accountDao.findByBalanceDescending(2);

        Assertions.assertEquals(2, found.size());
        Assertions.assertEquals(account3.getName(), "n3");
    }

    @Test
    void shouldFindAccountsByEmptyOperations() {
        Account account1 = new Account("n1", "a1");
        account1.setBalance(BigDecimal.TEN);
        accountDao.save(account1);
        AccountOperation accountOperation1 = new AccountOperation(account1, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperationDao.save(accountOperation1);

        Account account2 = new Account("n2", "a2");
        account2.setBalance(BigDecimal.TEN);
        accountDao.save(account2);

        List<Account> byEmptyOperations = accountDao.findByEmptyOperations();
        Assertions.assertEquals(1, byEmptyOperations.size());
        Assertions.assertEquals("n2", byEmptyOperations.get(0).getName());
    }

    @Test
    void shouldFindAccountsOrderedByOperationsCount() {
        Account account1 = new Account("n1", "a1");
        account1.setBalance(BigDecimal.TEN);
        accountDao.save(account1);
        AccountOperation accountOperation1 = new AccountOperation(account1, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperationDao.save(accountOperation1);
        AccountOperation accountOperation2 = new AccountOperation(account1, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperationDao.save(accountOperation2);

        Account account2 = new Account("n2", "a2");
        account2.setBalance(BigDecimal.TEN);
        accountDao.save(account2);
        AccountOperation accountOperation3 = new AccountOperation(account2, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperationDao.save(accountOperation3);

        List<Account> byOperationsCount = accountDao.findByOperationsCount();
        Assertions.assertEquals(2, byOperationsCount.size());
        Assertions.assertEquals("n1", byOperationsCount.get(0).getName());
        Assertions.assertEquals("n2", byOperationsCount.get(1).getName());
    }
}
