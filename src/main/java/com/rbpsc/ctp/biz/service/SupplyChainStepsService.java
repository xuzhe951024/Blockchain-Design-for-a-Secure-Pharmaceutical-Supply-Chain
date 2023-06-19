package com.rbpsc.ctp.biz.service;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;

public interface SupplyChainStepsService {
    public boolean manufacture(DrugInfo drug, OperationBase operation);

    public boolean distributor(DrugInfo drug);

    public boolean consumer(DrugLifeCycle drugLifeCycle);
}
