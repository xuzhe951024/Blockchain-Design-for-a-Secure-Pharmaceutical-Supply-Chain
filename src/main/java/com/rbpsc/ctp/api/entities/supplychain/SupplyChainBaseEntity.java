package com.rbpsc.ctp.api.entities.supplychain;

import com.rbpsc.ctp.api.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class SupplyChainBaseEntity extends BaseEntity<String> {
    private String batchId;
}
