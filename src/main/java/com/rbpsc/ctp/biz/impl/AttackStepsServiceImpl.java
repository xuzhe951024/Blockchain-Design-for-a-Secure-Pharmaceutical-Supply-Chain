package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import com.rbpsc.ctp.biz.service.AttackStepsService;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttackStepsServiceImpl implements AttackStepsService {

    @Autowired
    WebClientUtil webClientUtil;

    @Override
    public boolean attackAvailability(AttackAvailability attackAvailability) {
        // TODO: access "toggle-api"

        webClientUtil.postWithParams(
                        "http://127.0.0.1:8090/v1/drugLifeCycle/drugOrderStep/toggle",
                        true,
                        Boolean.class,
                        DrugLifeCycleResponse.class)
                .subscribe(result -> {
                    assert result.isSuccess();
                }, error -> {
                    error.printStackTrace();
                });

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
