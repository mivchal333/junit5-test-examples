package dao;

import model.Account;
import model.AccountOperation;
import model.OperationType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

class AccountOperationDaoImplTest {
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
    void findByDateRange() {
        Account account = new Account("n1", "a1");
        account.setBalance(BigDecimal.TEN);
        accountDao.save(account);

        long baseDate = 1078884319047L;

        AccountOperation accountOperation1 = new AccountOperation(account, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperation1.setCreateDate(new Date(baseDate - 1000));
        accountOperationDao.save(accountOperation1);

        AccountOperation accountOperation2 = new AccountOperation(account, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperation2.setCreateDate(new Date(baseDate));
        accountOperationDao.save(accountOperation2);

        AccountOperation accountOperation3 = new AccountOperation(account, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperation3.setCreateDate(new Date(baseDate + 1000));
        accountOperationDao.save(accountOperation3);

        AccountOperation accountOperation4 = new AccountOperation(account, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperation4.setCreateDate(new Date(baseDate + 2000));
        accountOperationDao.save(accountOperation4);

        List<AccountOperation> found = accountOperationDao.findByDateRange(new Date(baseDate - 1), new Date(baseDate + 1500));

        Assertions.assertEquals(2, found.size());
    }

    @Test
    void findFrequentOperationTypeForAccount() {
        Account account = new Account("n1", "a1");
        account.setBalance(BigDecimal.TEN);
        accountDao.save(account);

        AccountOperation accountOperation1 = new AccountOperation(account, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperationDao.save(accountOperation1);

        AccountOperation accountOperation2 = new AccountOperation(account, BigDecimal.ONE, OperationType.DEPOSIT);
        accountOperationDao.save(accountOperation2);

        AccountOperation accountOperation3 = new AccountOperation(account, BigDecimal.ONE, OperationType.TRANSFER);
        accountOperationDao.save(accountOperation3);

        AccountOperation accountOperation4 = new AccountOperation(account, BigDecimal.ONE, OperationType.WITHDRAW);
        accountOperationDao.save(accountOperation4);

        Optional<OperationType> found = accountOperationDao.findOftenOperationForAccount(account.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(OperationType.WITHDRAW, found.get());
    }
}
