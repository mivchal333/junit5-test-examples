package bank;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private Long id;
    private String name;
    private String address;
    private BigDecimal balance;

    public Account(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.balance = BigDecimal.ZERO;
    }

    public BigDecimal addBalance(BigDecimal value) {
        this.balance = balance.add(value);
        return this.balance;
    }
    public BigDecimal subtractBalance(BigDecimal value) {
        this.balance = balance.subtract(value);
        return this.balance;
    }
}
