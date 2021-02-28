package bank;

import java.util.*;

public class BankDatabase {
    private static final Map<Long, Account> accountList = Collections.synchronizedMap(new HashMap<>());

    private BankDatabase() {
    }

    public static Map<Long, Account> getDB() {
        return accountList;
    }
}
