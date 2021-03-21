package dao;

import model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    void save(Account account);

    void update(Account account);

    void delete(Account account);

    Optional<Account> findById(Long id);

    List<Account> findAll();

    Optional<Account> findByNameAndAddress(String name, String address);
}
