package com.rbpsc.ctp.api.entities.supplychain.operations.attack;

import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttackModelBase extends RoleBase {
    String targetBatchId;
    String targetDrugId;
}
