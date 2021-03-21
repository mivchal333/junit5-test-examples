package dao;

import model.Account;

import java.util.Optional;

public interface AccountDao extends GenericDao<Account, Long> {
    Optional<Account> findByNameAndAddress(String name, String address);

}
