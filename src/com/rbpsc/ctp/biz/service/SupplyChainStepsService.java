package com.rbpsc.ctp.biz.service;


import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;

public interface SupplyChainStepsService {
    public boolean manufacture(DrugInfo drug, OperationBase operationBase);

    public boolean distributor(DrugInfo drug, OperationBase operationBase);

    public boolean consumer(DrugInfo drug, OperationBase operationBase, String targetConsumerId);
}
