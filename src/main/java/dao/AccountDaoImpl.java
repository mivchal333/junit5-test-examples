package dao;

import lombok.extern.slf4j.Slf4j;
import model.Account;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Slf4j
public class AccountDaoImpl extends GenericDaoJpaImpl<Account, Long> implements AccountDao {
    @Override
    public Optional<Account> findByNameAndAddress(String name, String address) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> rootEntry = cq.from(Account.class);
        Predicate namePredicate = cb.equal(rootEntry.get("name"), name);
        Predicate addressPredicate = cb.equal(rootEntry.get("address"), address);

        CriteriaQuery<Account> byNameAndAddress = cq.select(rootEntry)
                .where(namePredicate, addressPredicate);

        try {
            Account singleResult = em.createQuery(byNameAndAddress).getSingleResult();
            return Optional.of(singleResult);
        } catch (NoResultException e){
            log.debug("Entries not found. Empty result.");
        }
        return Optional.empty();
    }
}