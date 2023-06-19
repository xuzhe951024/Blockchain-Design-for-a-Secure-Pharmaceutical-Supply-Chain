package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.biz.service.SupplyChainStepsService;
import com.rbpsc.ctp.repository.service.ConsumerReceiptRepository;
import com.rbpsc.ctp.repository.service.DrugInfoRepository;
import com.rbpsc.ctp.repository.service.DrugLifeCycleReceiptRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SupplyChainStepsServiceImpl implements SupplyChainStepsService {

    final DrugInfoRepository drugInfoRepository;

    final ConsumerReceiptRepository consumerReceiptRepository;

    final DrugLifeCycleReceiptRepository drugLifeCycleReceiptRepository;

    public SupplyChainStepsServiceImpl(DrugInfoRepository drugInfoRepository, ConsumerReceiptRepository consumerReceiptRepository, DrugLifeCycleReceiptRepository drugLifeCycleReceiptRepository) {
        this.drugInfoRepository = drugInfoRepository;
        this.consumerReceiptRepository = consumerReceiptRepository;
        this.drugLifeCycleReceiptRepository = drugLifeCycleReceiptRepository;
    }

    @Override
    public boolean manufacture(DrugInfo drug, OperationBase operation) {
        drugInfoRepository.insertDrugInfo(drug);

        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null == receiptDrugLifeCycle){
            receiptDrugLifeCycle =  DataEntityFactory.createDrugLifeCycleReceipt(drug);
        }

        Receipt receipt = DataEntityFactory.createReceipt(operation);

        receiptDrugLifeCycle.addOperation(receipt);

        return drugLifeCycleReceiptRepository.updateWithInsert(receiptDrugLifeCycle, Optional.of(drug.getId()));
    }

    @Override
    public boolean distributor(DrugInfo drug) {

        drugInfoRepository.insertDrugInfo(drug);

        return null == drugInfoRepository.selectDrugInfoById(drug.getId());
    }

    @Override
    public boolean consumer(DrugLifeCycle drugLifeCycle) {

        Consumer consumer = consumerReceiptRepository.selectConsumerReceiptById(drugLifeCycle.getExpectedReceiver().getId());
        if (null == consumer){
            log.error(String.format("No consumer information in database: \n {%s}", drugLifeCycle.getExpectedReceiver()));
            return false;
        }

        if (consumer.isSatisfied()){
            log.error(String.format("Consumer has already been satisfied:\n{%s}", consumer));
            return false;
        }

        consumer.satisfyDosage();

        return consumerReceiptRepository.modifyConsumerReceipt(consumer);
    }
}
