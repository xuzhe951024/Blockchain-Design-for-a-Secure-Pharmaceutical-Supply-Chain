package com.rbpsc.ctp.biz.service;


import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;

public interface AttackStepsService {
    public boolean attackAvailability(OperationBase attackAvailability);

    public boolean attackConfidentiality(DrugInfo drug, OperationBase attackConfidentiality);

    public boolean attackIntegrity(DrugInfo drug, OperationBase attackIntegrity);
}
