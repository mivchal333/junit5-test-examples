package model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation extends AbstractModel {
    @ManyToOne
    private Account source;
    @ManyToOne
    private Account destination;
    private BigDecimal amount;
    private OperationType type;

    public AccountOperation(Account source, BigDecimal amount, OperationType type) {
        this.source = source;
        this.amount = amount;
        this.type = type;
    }
}

