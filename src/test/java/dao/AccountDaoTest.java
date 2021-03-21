package dao;

import model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class AccountDaoTest {

    private AccountDao accountDao;

    @BeforeEach
    private void setUp() {
        accountDao = new AccountDaoImpl();
    }

    @AfterEach
    private void clear() {
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
    void findByNameAndAddress() {
        String name1 = "name1";
        String address1 = "address1";
        Account account = new Account(name1, address1);
        accountDao.save(account);


    }
}
