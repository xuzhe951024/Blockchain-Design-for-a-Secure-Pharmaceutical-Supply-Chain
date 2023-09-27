package com.r3.developers.samples.obligation.etities.base;

import com.r3.developers.samples.obligation.etities.base.BaseEntity;

public class SupplyChainBaseEntity extends BaseEntity<String> {
    private String batchId;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
