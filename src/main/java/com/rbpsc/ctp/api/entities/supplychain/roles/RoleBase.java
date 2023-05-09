package com.rbpsc.ctp.api.entities.supplychain.roles;

import com.rbpsc.ctp.api.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleBase extends BaseEntity<String> {
    String roleName;
    String address;
}
