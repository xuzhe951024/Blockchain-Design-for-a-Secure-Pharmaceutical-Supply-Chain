package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.biz.service.SupplyChainStepsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SupplyChainStepsServiceImpl implements SupplyChainStepsService {
    @Override
    public boolean manufacture(DrugInfo drug) {
        return false;
    }

    @Override
    public boolean distributor(DrugInfo drug) {
        return false;
    }

    @Override
    public boolean consumer(DrugInfo drug) {
        return false;
    }
}
