package dao;

import model.Account;
import model.AccountOperation;
import model.OperationType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class AccountOperationDaoImpl extends GenericDaoJpaImpl<AccountOperation, Long> implements AccountOperationDao {
    @PersistenceContext(name = "PU")
    private EntityManager entityManager;

    @Override
    public List<AccountOperation> findByAccountId(Account account) {
        EntityManager em = entityManager;
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

    @Override
    public List<AccountOperation> findByDateRange(Date start, Date end) {
        TypedQuery<AccountOperation> query = entityManager.createNamedQuery("AccountOperation.findByDateRange", AccountOperation.class);
        query.setParameter(1, start);
        query.setParameter(2, end);
        return query.getResultList();
    }

    @Override
    public Optional<OperationType> findOftenOperationForAccount(Long id) {
        TypedQuery<OperationType> query = entityManager.createNamedQuery("AccountOperation.findOftenOperationTypeForAccount", OperationType.class);
        query.setParameter(1, id);
        query.setMaxResults(1);
        return query.getResultList()
                .stream()
                .findFirst();
    }
}
