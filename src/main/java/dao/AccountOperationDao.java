package dao;

import model.Account;
import model.AccountOperation;
import model.OperationType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AccountOperationDao extends GenericDao<AccountOperation, Long> {

    List<AccountOperation> findByAccountId(Account account);

    List<AccountOperation> findByDateRange(Date start, Date end);

    Optional<OperationType> findOftenOperationForAccount(Long id);
}
