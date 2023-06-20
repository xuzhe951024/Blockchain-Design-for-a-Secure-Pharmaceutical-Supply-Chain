package com.rbpsc.ctp.biz.service;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;

public interface SupplyChainStepsService {
    public boolean manufacture(DrugInfo drug, DrugOrderStep drugOrderStep);

    public boolean distributor(DrugInfo drug, DrugOrderStep drugOrderStep);

    public boolean consumer(DrugLifeCycle drugLifeCycle);
}
