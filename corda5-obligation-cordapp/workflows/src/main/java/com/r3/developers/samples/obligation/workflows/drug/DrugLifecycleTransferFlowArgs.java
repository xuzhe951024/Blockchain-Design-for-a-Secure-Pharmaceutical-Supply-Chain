package com.r3.developers.samples.obligation.workflows.drug;

// A class to hold the deserialized arguments required to start the flow.
public class DrugLifecycleTransferFlowArgs {

    private String newOperationMSG;
    private String drugLifecycleId;

    private String newSupplyChainDownStream;

    public DrugLifecycleTransferFlowArgs() {
    }

    public DrugLifecycleTransferFlowArgs(String newOperationMSG, String drugLifecycleId, String newSupplyChainDownStream) {
        this.newOperationMSG = newOperationMSG;
        this.drugLifecycleId = drugLifecycleId;
        this.newSupplyChainDownStream = newSupplyChainDownStream;
    }

    public String getNewOperationMSG() {
        return newOperationMSG;
    }

    public void setNewOperationMSG(String newOperationMSG) {
        this.newOperationMSG = newOperationMSG;
    }

    public String getDrugLifecycleId() {
        return drugLifecycleId;
    }

    public void setDrugLifecycleId(String drugLifecycleId) {
        this.drugLifecycleId = drugLifecycleId;
    }

    public String getNewSupplyChainDownStream() {
        return newSupplyChainDownStream;
    }

    public void setNewSupplyChainDownStream(String newSupplyChainDownStream) {
        this.newSupplyChainDownStream = newSupplyChainDownStream;
    }
}
