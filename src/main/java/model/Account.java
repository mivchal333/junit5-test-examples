package model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Account extends AbstractModel {

    private String name;
    private String address;
    private BigDecimal balance;

    @OneToMany(mappedBy = "source", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AccountOperation> operations;

    public Account() {
        this.balance = BigDecimal.ZERO;
    }

    public Account(String name, String address) {
        this.name = name;
        this.address = address;
        this.balance = BigDecimal.ZERO;
    }

    public void addBalance(BigDecimal value) {
        this.balance = balance.add(value);
    }

    public void subtractBalance(BigDecimal value) {
        this.balance = balance.subtract(value);
    }
}
