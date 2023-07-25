package org.rbpsc.api.entities.supplychain.roles;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.rbpsc.api.entities.supplychain.SupplyChainBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@DataType()
public class RoleBase extends SupplyChainBaseEntity {
    @Property()
    private String roleName;

    @Property()
    private String address;

    public String getDomain(){
        return this.address.split("/")[2];
    }
}
