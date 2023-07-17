package main.java.com.rbpsc.ctp.api.entities.supplychain.roles;

import main.java.com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleBase extends SupplyChainBaseEntity {
    private String roleName;
    private String address;

    public String getDomain(){
        return this.address.split("/")[2];
    }
}
