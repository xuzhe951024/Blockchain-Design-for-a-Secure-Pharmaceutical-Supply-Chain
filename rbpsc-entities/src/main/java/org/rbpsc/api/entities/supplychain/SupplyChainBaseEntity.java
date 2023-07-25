package org.rbpsc.api.entities.supplychain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.rbpsc.api.entities.base.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@DataType()
public class SupplyChainBaseEntity extends BaseEntity<String> {
    @Property()
    private String batchId;
}
