package bank;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BankImpl implements Bank {
    private final Map<Long, Account> db;

    public BankImpl() {
        this.db = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public Long createAccount(String name, String address) {
        long nextId = db.size();
        Account account = new Account(nextId, name, address);

        db.put(account.getId(), account);
        return nextId;
    }

    @Override
    public Long findAccount(String name, String address) {
        Optional<Account> accountOpt = db.values().stream()
                .filter(account -> account.getName().equals(name))
                .filter(account -> account.getAddress().equals(address))
                .findFirst();

        return accountOpt.map(Account::getId)
                .orElse(null);

    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        Account account = getAccount(id);
        account.addBalance(amount);
    }

    @Override
    public BigDecimal getBalance(Long id) {
        Account account = getAccount(id);
        return account.getBalance();
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        Account account = getAccount(id);
        if (amount.compareTo(account.getBalance()) > 0) {
            throw new InsufficientFundsException();
        }
        account.subtractBalance(amount);
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {
        Account source = getAccount(idSource);
        Account destination = getAccount(idDestination);

        if (amount.compareTo(source.getBalance()) > 0) {
            throw new InsufficientFundsException();
        }


        source.subtractBalance(amount);

        destination.addBalance(amount);
    }

    private Account getAccount(Long id) {
        Optional<Account> accountOpt = Optional.ofNullable(db.get(id));
        if (!accountOpt.isPresent()) {
            throw new AccountIdException();
        }
        return accountOpt.orElseThrow(AccountIdException::new);
    }
}
