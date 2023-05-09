package com.rbpsc.ctp.biz.service;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;

public interface AttackStepsService {
    public boolean attackAvailability(DrugInfo drug);

    public boolean attackConfidentiality(DrugInfo drug);

    public boolean attackIntegrity(DrugInfo drug);
}
