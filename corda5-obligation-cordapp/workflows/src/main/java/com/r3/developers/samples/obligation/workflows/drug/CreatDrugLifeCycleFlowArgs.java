package com.r3.developers.samples.obligation.workflows.drug;
import com.r3.developers.samples.obligation.etities.dto.DrugLifeCycleReceipt;

public class CreatDrugLifeCycleFlowArgs {
    private DrugLifeCycleReceipt drugLifeCycleReceipt;
    private String supplyChainDownStream;

    private String operationMSG;

    public DrugLifeCycleReceipt getDrugLifeCycleReceipt() {
        return drugLifeCycleReceipt;
    }

    public void setDrugLifeCycleReceipt(DrugLifeCycleReceipt drugLifeCycleReceipt) {
        this.drugLifeCycleReceipt = drugLifeCycleReceipt;
    }

    public String getSupplyChainDownStream() {
        return supplyChainDownStream;
    }

    public void setSupplyChainDownStream(String supplyChainDownStream) {
        this.supplyChainDownStream = supplyChainDownStream;
    }

    public String getOperationMSG() {
        return operationMSG;
    }

    public void setOperationMSG(String operationMSG) {
        this.operationMSG = operationMSG;
    }
}
