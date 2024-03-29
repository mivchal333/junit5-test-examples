package service;

import model.Account;
import model.AccountOperation;

import java.math.BigDecimal;
import java.util.List;

public interface Bank {
    /**
     * Tworzy nowe lub zwraca id istniejącego konta.
     *
     * @param name    imie i nazwisko własciciela
     * @param address adres własciciela
     * @return id utworzonego lub istniejacego konta.
     */
    Long createAccount(String name, String address);

    /**
     * Znajduje identyfikator konta.
     *
     * @param name    imię i nazwisko właściciela
     * @param address adres właściciela
     * @return id konta lub null gdy brak konta o podanych parametrach
     */
    Long findAccount(String name, String address);

    List<Account> findAllAccounts();

    /**
     * Dodaje srodki do konta.
     *
     * @param id
     * @param amount srodki
     * @throws AccountIdException gdy id konta jest nieprawidlowe
     */
    void deposit(Long id, BigDecimal amount);

    /**
     * Zwraca ilosc srodkow na koncie.
     *
     * @param id
     * @return srodki
     * @throws AccountIdException gdy id konta jest nieprawidlowe
     */
    BigDecimal getBalance(Long id);

    /**
     * Pobiera srodki z konta.
     *
     * @param id
     * @param amount srodki
     * @throws AccountIdException         gdy id konta jest nieprawidlowe
     * @throws InsufficientFundsException gdy srodki na koncie nie sa
     *                                    wystarczajace do wykonania operacji
     */
    void withdraw(Long id, BigDecimal amount);

    /**
     * Przelewa srodki miedzy kontami.
     *
     * @param idSource
     * @param idDestination
     * @param amount        srodki
     * @throws AccountIdException         ,         gdy id konta jest nieprawidlowe
     * @throws InsufficientFundsException gdy srodki na koncie nie sa
     *                                    wystarczajace do wykonania operacji
     */
    void transfer(Long idSource, Long idDestination, BigDecimal amount);

    AccountOperation getLastOperation(Long accountId);

    List<AccountOperation> getOperations(Long accountId);

    class InsufficientFundsException extends RuntimeException {
    }

    class AccountIdException extends RuntimeException {
    }
}
