package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import com.rbpsc.ctp.biz.service.AttackStepsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttackStepsServiceImpl implements AttackStepsService {

    @Override
    public boolean attackAvailability(AttackAvailability attackAvailability) {
        // TODO: access "toggle-api"
        return false;
    }

    @Override
    public boolean attackConfidentiality(AttackConfidentiality attackConfidentiality) {
        return false;
    }

    @Override
    public boolean attackIntegrity(AttackIntegrity attackIntegrity) {
        return false;
    }
}
