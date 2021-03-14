package bank;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class BankImpl implements Bank {
    private final static Logger log =
            Logger.getLogger(BankImpl.class.getName());

    private final Map<Long, Account> db;

    public BankImpl() {
        log.info("Creating bank instance");
        this.db = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public Long createAccount(String name, String address) {
        log.fine("Starting create account");
        long nextId = db.size();
        Account account = new Account(nextId, name, address);

        db.put(account.getId(), account);
        log.fine("Account successfully created");
        return nextId;
    }

    @Override
    public Long findAccount(String name, String address) {
        log.fine("Finding account by name and address");
        Optional<Account> accountOpt = db.values().stream()
                .filter(account -> account.getName().equals(name))
                .filter(account -> account.getAddress().equals(address))
                .findFirst();

        log.fine("Account find ended");
        return accountOpt.map(Account::getId)
                .orElse(null);

    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        log.fine("Deposit to account");
        Account account = getAccount(id);
        account.addBalance(amount);
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
        log.fine("Transfer finished successfully");

    }

    private Account getAccount(Long id) {
        Optional<Account> accountOpt = Optional.ofNullable(db.get(id));
        if (!accountOpt.isPresent()) {
            log.severe("Account doesn't exist!");
            throw new AccountIdException();
        }
        return accountOpt.get();
    }
}
