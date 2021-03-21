package dao;

import model.Account;
import model.AccountOperation;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class AccountOperationDaoImpl extends GenericDaoJpaImpl<AccountOperation, Long> implements AccountOperationDao {
    @Override
    public List<AccountOperation> findByAccountId(Account account) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AccountOperation> cq = cb.createQuery(AccountOperation.class);
        Root<AccountOperation> rootEntry = cq.from(AccountOperation.class);
        Predicate sourcePredicate = cb.equal(rootEntry.get("source"), account);
        Predicate destinationPredicate = cb.equal(rootEntry.get("destination"), account);
        Predicate sourceOrDestinationPredicate = cb.or(sourcePredicate, destinationPredicate);

        CriteriaQuery<AccountOperation> sourceOdDestinationQuery = cq.select(rootEntry)
                .where(sourceOrDestinationPredicate);

       return em.createQuery(sourceOdDestinationQuery).getResultList();
    }
}
