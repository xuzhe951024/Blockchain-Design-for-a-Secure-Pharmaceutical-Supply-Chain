package com.rbpsc.ctp.api.entities.supplychain.attack;

import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttackModelBase extends SupplyChainBaseEntity {
    String targetBatchId;
    String targetDrugId;
    int attackType;
}
