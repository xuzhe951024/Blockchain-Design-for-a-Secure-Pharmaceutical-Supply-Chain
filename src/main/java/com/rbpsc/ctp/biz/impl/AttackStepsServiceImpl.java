package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.biz.service.AttackStepsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttackStepsServiceImpl implements AttackStepsService {
    @Override
    public boolean attackAvailability(DrugInfo drug) {
        return false;
    }

    @Override
    public boolean attackConfidentiality(DrugInfo drug) {
        return false;
    }

    @Override
    public boolean attackIntegrity(DrugInfo drug) {
        return false;
    }
}
