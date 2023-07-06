package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import com.rbpsc.ctp.biz.service.AttackStepsService;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import com.rbpsc.ctp.repository.service.AttackConfidentialityRepository;
import com.rbpsc.ctp.repository.service.DrugLifeCycleReceiptRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttackStepsServiceImpl implements AttackStepsService {

    final
    WebClientUtil webClientUtil;

    final DrugLifeCycleReceiptRepository drugLifeCycleReceiptRepository;

    public AttackStepsServiceImpl(WebClientUtil webClientUtil, AttackConfidentialityRepository attackConfidentialityRepository, DrugLifeCycleReceiptRepository drugLifeCycleReceiptRepository) {
        this.webClientUtil = webClientUtil;
        this.drugLifeCycleReceiptRepository = drugLifeCycleReceiptRepository;
    }

    @Override
    public boolean attackAvailability(AttackAvailability attackAvailability) {

        webClientUtil.postWithParams(
                        attackAvailability.getAddress(),
                        true,
                        Boolean.class,
                        DrugLifeCycleResponse.class)
                .subscribe(result -> {
                    assert result.isSuccess();
                }, Throwable::printStackTrace);

        return true;
    }

    @Override
    public boolean attackConfidentiality(DrugInfo drug, AttackConfidentiality attackConfidentiality) {

        // TODO: add "receipt queue"
        // TODO: put fake receipt back in the receipt queue
        // TODO: replace ExampleMsg here

        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null == receiptDrugLifeCycle){
            receiptDrugLifeCycle =  DataEntityFactory.createDrugLifeCycleReceipt(drug);
        }

        return false;
    }

    @Override
    public boolean attackIntegrity(DrugInfo drug, AttackIntegrity attackIntegrity) {
        // TODO: Make the selling chain stop here and store "DrugLifeCycle" object
        return false;
    }
}
