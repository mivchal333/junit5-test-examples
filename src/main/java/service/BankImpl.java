package service;

import dao.AccountDao;
import dao.jdbc.AccountDaoJdbcImpl;
import model.Account;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Logger;

public class BankImpl implements Bank {
    private final static Logger log =
            Logger.getLogger(BankImpl.class.getName());

    private final AccountDao accountDao;

    public BankImpl() {
        log.info("Creating bank instance");
        accountDao = new AccountDaoJdbcImpl();
    }

    @Override
    public Long createAccount(String name, String address) {
        log.fine("Starting create account");
        Account account = new Account(name, address);

        accountDao.save(account);

        log.fine("Account successfully created");

        return account.getId();
    }

    @Override
    public Long findAccount(String name, String address) {
        log.fine("Finding account by name and address");

        Optional<Account> foundByNameAndAddress = accountDao.findByNameAndAddress(name, address);

        log.fine("Account find ended");

        return foundByNameAndAddress
                .map(Account::getId)
                .orElse(null);

    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        log.fine("Deposit to account");
        accountDao.findById(id);
        Account account = getAccount(id);
        account.addBalance(amount);

        accountDao.update(account);
        log.fine("Deposit finished successfully");
    }

    @Override
    public BigDecimal getBalance(Long id) {
        log.fine("Checking balance");
        Account account = getAccount(id);
        log.fine("Check balance finished successfully");
        return account.getBalance();
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        log.fine("Preparing to withdraw money");
        Account account = getAccount(id);
        if (amount.compareTo(account.getBalance()) > 0) {
            log.severe("Not enough account balance for withdraw. Withdraw failed.");
            throw new InsufficientFundsException();
        }
        account.subtractBalance(amount);
        accountDao.update(account);
        log.fine("Withdraw finished");
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {
        log.fine("Starting transfer process");

        Account source = getAccount(idSource);
        Account destination = getAccount(idDestination);

        if (amount.compareTo(source.getBalance()) > 0) {
            log.severe("Not enough account balance for transfer. Transfer failed.");
            throw new InsufficientFundsException();
        }


        source.subtractBalance(amount);

        destination.addBalance(amount);

        accountDao.update(source);
        accountDao.update(destination);
        log.fine("Transfer finished successfully");

    }

    private Account getAccount(Long id) {
        Optional<Account> accountOpt = accountDao.findById(id);
        if (!accountOpt.isPresent()) {
            log.severe("Account doesn't exist!");
            throw new AccountIdException();
        }
        return accountOpt.get();
    }
}