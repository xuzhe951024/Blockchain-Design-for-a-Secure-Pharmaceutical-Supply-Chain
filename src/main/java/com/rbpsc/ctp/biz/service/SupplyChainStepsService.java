package com.rbpsc.ctp.biz.service;

import com.rbpsc.ctp.api.entities.dto.OperationVO;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;

public interface SupplyChainStepsService {
    public DrugInfo manufacture(DrugInfo drug);

    public boolean distributor(DrugInfo drug);

    public boolean consumer(DrugLifeCycle drugLifeCycle);
}