package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import com.rbpsc.ctp.biz.service.AttackStepsService;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import com.rbpsc.ctp.repository.service.AttackConfidentialityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttackStepsServiceImpl implements AttackStepsService {

    @Autowired
    WebClientUtil webClientUtil;

    @Autowired
    AttackConfidentialityRepository attackConfidentialityRepository;

    @Override
    public boolean attackAvailability(AttackAvailability attackAvailability) {

        webClientUtil.postWithParams(
                        attackAvailability.getTargetAddress(),
                        true,
                        Boolean.class,
                        DrugLifeCycleResponse.class)
                .subscribe(result -> {
                    assert result.isSuccess();
                }, Throwable::printStackTrace);

        return true;
    }

    @Override
    public boolean attackConfidentiality(AttackConfidentiality attackConfidentiality) {

        // TODO: add "receipt response controller", automatically schedule the receipt response on the webpage
        // TODO: replace ExampleMsg here

        attackConfidentialityRepository.insertAttack(attackConfidentiality);

        return false;
    }

    @Override
    public boolean attackIntegrity(AttackIntegrity attackIntegrity) {
        return false;
    }
}
