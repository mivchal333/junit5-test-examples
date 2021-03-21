import service.Bank;
import service.BankImpl;

import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {

        Bank bank = new BankImpl();

        Long accountID = bank.createAccount("name", "address");

        bank.deposit(accountID, BigDecimal.TEN);

        bank.getBalance(accountID);

        bank.withdraw(accountID, BigDecimal.ONE);

        bank.getBalance(accountID);

        bank.getOperations(accountID);

    }
}
