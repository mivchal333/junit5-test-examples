package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation extends AbstractModel {
    @ManyToOne(optional = false)
    private Account source;
    @ManyToOne
    private Account destination;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType type;

    public AccountOperation(Account source, BigDecimal amount, OperationType type) {
        this.source = source;
        this.amount = amount;
        this.type = type;
    }
}

