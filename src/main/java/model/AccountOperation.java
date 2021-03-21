package model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class AccountOperation extends AbstractModel {
    @ManyToOne
    private Account source;
    @ManyToOne
    private Account destination;
    private BigDecimal amount;
    private OperationType type;
}
enum OperationType {
    DEPOSIT, TRANSFER, WITHDRAW
}

