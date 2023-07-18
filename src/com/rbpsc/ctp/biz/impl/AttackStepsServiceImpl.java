package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.repository.service.DrugLifeCycleReceiptRepository;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import com.rbpsc.ctp.biz.service.AttackStepsService;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import com.rbpsc.ctp.repository.service.AttackConfidentialityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.rbpsc.ctp.common.Constant.EntityConstants.ATTACK_CONFIDENTIALITY_MSG_SUFFIX;

@Service
@Slf4j
public class AttackStepsServiceImpl implements AttackStepsService {

    final WebClientUtil webClientUtil;

    final DrugLifeCycleReceiptRepository drugLifeCycleReceiptRepository;

    public AttackStepsServiceImpl(WebClientUtil webClientUtil, AttackConfidentialityRepository attackConfidentialityRepository, DrugLifeCycleReceiptRepository drugLifeCycleReceiptRepository) {
        this.webClientUtil = webClientUtil;
        this.drugLifeCycleReceiptRepository = drugLifeCycleReceiptRepository;
    }

    @Override
    public boolean attackAvailability(OperationBase attackAvailability) {

        webClientUtil.postWithParams(
                        attackAvailability.getAddress(),
                        true,
                        Boolean.class,
                        DrugLifeCycleResponse.class)
                .subscribe(result -> {
                    log.info("Attack_Availability: \n" + attackAvailability + "\nresult: \n" + result);
                    assert result.isSuccess();
                }, Throwable::printStackTrace);

        return true;
    }

    @Override
    public boolean attackConfidentiality(DrugInfo drug, OperationBase attackConfidentiality) {

        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null == receiptDrugLifeCycle) {
            receiptDrugLifeCycle = DataEntityFactory.createDrugLifeCycleReceipt(drug);
        }

        attackConfidentiality.setOperationMSG(attackConfidentiality.getOperationMSG() + ATTACK_CONFIDENTIALITY_MSG_SUFFIX);

        Receipt receipt = DataEntityFactory.createReceipt(attackConfidentiality);

        receiptDrugLifeCycle.addOperation(receipt);

        return drugLifeCycleReceiptRepository.updateWithInsert(receiptDrugLifeCycle, Optional.of(drug.getId()));
    }

    @Override
    public boolean attackIntegrity(DrugInfo drug, OperationBase attackIntegrity) {

        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null == receiptDrugLifeCycle) {
            receiptDrugLifeCycle = DataEntityFactory.createDrugLifeCycleReceipt(drug);
        }

        Receipt receipt = DataEntityFactory.createReceipt(attackIntegrity);

        receiptDrugLifeCycle.addOperation(receipt);

        receiptDrugLifeCycle.setIsAttacked(true);

        return drugLifeCycleReceiptRepository.updateWithInsert(receiptDrugLifeCycle, Optional.of(drug.getId()));
    }
}
