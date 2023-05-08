package com.rbpsc.ctp.api.entities.supplychain;

import com.rbpsc.ctp.api.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplyChainBaseEntity extends BaseEntity<String> {
    String batchId;
}
