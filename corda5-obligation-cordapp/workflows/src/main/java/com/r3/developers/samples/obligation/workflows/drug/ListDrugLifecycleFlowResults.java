package com.r3.developers.samples.obligation.workflows.drug;

import com.r3.developers.samples.obligation.etities.dto.DrugLifeCycleReceipt;

// A class to hold the deserialized arguments required to start the flow.
public class ListDrugLifecycleFlowResults {

    private DrugLifeCycleReceipt drugLifeCycleReceipt;

    public ListDrugLifecycleFlowResults() {
    }

    public ListDrugLifecycleFlowResults(DrugLifeCycleReceipt drugLifeCycleReceipt) {
        this.drugLifeCycleReceipt = drugLifeCycleReceipt;
    }

    public DrugLifeCycleReceipt getDrugLifeCycleReceipt() {
        return drugLifeCycleReceipt;
    }

    public void setDrugLifeCycleReceipt(DrugLifeCycleReceipt drugLifeCycleReceipt) {
        this.drugLifeCycleReceipt = drugLifeCycleReceipt;
    }
}
