package model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@NamedQuery(name = "Account.findByNameAndAddress", query = "select a from  Account a where a.name = ?1 and a.address = ?2")
@NamedQuery(name = "Account.searchByName", query = "select a from  Account a where a.name like ?1")
@NamedQuery(name = "Account.findByBalanceRange", query = "select a from  Account a where a.balance between ?1 and ?2")
@NamedQuery(name = "Account.findByBalanceDescending", query = "select a from  Account a order by a.balance desc")
@NamedQuery(name = "Account.findByEmptyOperations", query = "select a from  Account a where a.operations is empty")
@NamedQuery(name = "Account.findByOperationsCountDescending", query = "select a from  Account a group by a.id order by count(a.operations) desc ")
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
