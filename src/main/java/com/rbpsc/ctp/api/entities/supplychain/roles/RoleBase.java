package com.rbpsc.ctp.api.entities.supplychain.roles;

import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleBase extends SupplyChainBaseEntity {
    String roleName;
    String address;
}
