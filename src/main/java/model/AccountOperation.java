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
@NamedQuery(name = "AccountOperation.findByDateRange", query = "select ao from AccountOperation ao where ao.createDate between ?1 and ?2")
@NamedQuery(name = "AccountOperation.findOftenOperationTypeForAccount", query = "select ao.type from AccountOperation ao where ao.source.id = ?1 group by ao.type order by count(ao.type) desc")
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

