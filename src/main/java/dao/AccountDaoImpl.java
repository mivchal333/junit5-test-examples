package dao;

import lombok.extern.slf4j.Slf4j;
import model.Account;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class AccountDaoImpl extends GenericDaoJpaImpl<Account, Long> implements AccountDao {
    @Override
    public List<Account> findByNameAndAddress(String name, String address) {
        TypedQuery<Account> query = getEntityManager().createNamedQuery("Account.findByNameAndAddress", Account.class);
        query.setParameter(1, name);
        query.setParameter(2, address);

        return query.getResultList();
    }

    @Override
    public List<Account> searchByName(String name) {
        TypedQuery<Account> query = getEntityManager().createNamedQuery("Account.searchByName", Account.class);
        query.setParameter(1, name + "%");
        return query.getResultList();
    }

    @Override
    public List<Account> findByBalanceRange(BigDecimal start, BigDecimal end) {
        TypedQuery<Account> query = getEntityManager().createNamedQuery("Account.findByBalanceRange", Account.class);
        query.setParameter(1, start);
        query.setParameter(2, end);
        return query.getResultList();
    }

    @Override
    public List<Account> findByBalanceDescending(int limit) {
        TypedQuery<Account> query = getEntityManager().createNamedQuery("Account.findByBalanceDescending", Account.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Account> findByEmptyOperations() {
        TypedQuery<Account> query = getEntityManager().createNamedQuery("Account.findByEmptyOperations", Account.class);
        return query.getResultList();

    }

    @Override
    public List<Account> findByOperationsCount() {
        TypedQuery<Account> query = getEntityManager().createNamedQuery("Account.findByOperationsCountDescending", Account.class);
        return query.getResultList();
    }
}
