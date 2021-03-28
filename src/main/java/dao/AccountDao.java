package dao;

import model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao extends GenericDao<Account, Long> {
    List<Account> findByNameAndAddress(String name, String address);

    List<Account> searchByName(String name);

    List<Account> findByBalanceRange(BigDecimal start, BigDecimal end);

    List<Account> findByBalanceDescending(int limit);

}
