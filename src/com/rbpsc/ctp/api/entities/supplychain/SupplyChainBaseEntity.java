package com.rbpsc.ctp.api.entities.supplychain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.rbpsc.ctp.api.entities.base.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class SupplyChainBaseEntity extends BaseEntity<String> {
    private String batchId;
}
