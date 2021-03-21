package dao;

import model.Account;
import model.AccountOperation;

import java.util.List;

public interface AccountOperationDao extends GenericDao<AccountOperation, Long> {

    List<AccountOperation> findByAccountId(Account account);
}
