package main.java.org.rbpsc.api.entities.supplychain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import main.java.org.rbpsc.api.entities.base.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class SupplyChainBaseEntity extends BaseEntity<String> {
    private String batchId;
}
