package com.rbpsc.ctp.api.entities.supplychain.roles;

import com.rbpsc.ctp.api.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleBase extends BaseEntity<String> {
    private String roleName;
    private String address;
}
