package com.r3.developers.samples.obligation.etities.supplychain.drug;

import com.r3.developers.samples.obligation.etities.base.SupplyChainBaseEntity;
import net.corda.v5.base.annotations.CordaSerializable;

@CordaSerializable
public class DrugInfo extends SupplyChainBaseEntity {
    private String drugTagTagId;
    private String drugName;
    private boolean isFake = false;
    private boolean isRecalled = false;

    public String getDrugTagTagId() {
        return drugTagTagId;
    }

    public void setDrugTagTagId(String drugTagTagId) {
        this.drugTagTagId = drugTagTagId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public boolean getFake() {
        return isFake;
    }

    public void setFake(boolean fake) {
        isFake = fake;
    }

    public boolean getRecalled() {
        return isRecalled;
    }

    public void setRecalled(boolean recalled) {
        isRecalled = recalled;
    }

}
