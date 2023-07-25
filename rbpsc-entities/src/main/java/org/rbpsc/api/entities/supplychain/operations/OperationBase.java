package org.rbpsc.api.entities.supplychain.operations;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.rbpsc.api.entities.supplychain.roles.RoleBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@DataType()
public class OperationBase extends RoleBase {
    @Property()
    private String operationMSG;
}
