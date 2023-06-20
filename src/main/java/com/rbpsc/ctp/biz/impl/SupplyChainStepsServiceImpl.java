package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
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

    final ConsumerReceiptRepository consumerReceiptRepository;

    final DrugLifeCycleReceiptRepository drugLifeCycleReceiptRepository;

    public SupplyChainStepsServiceImpl(ConsumerReceiptRepository consumerReceiptRepository, DrugLifeCycleReceiptRepository drugLifeCycleReceiptRepository) {
        this.consumerReceiptRepository = consumerReceiptRepository;
        this.drugLifeCycleReceiptRepository = drugLifeCycleReceiptRepository;
    }

    @Override
    public boolean manufacture(DrugInfo drug, DrugOrderStep drugOrderStep) {
        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null != receiptDrugLifeCycle){
            log.error("Drug can not be produce duplicate");
            return false;
        }

        receiptDrugLifeCycle =  DataEntityFactory.createDrugLifeCycleReceipt(drug);

        Receipt receipt = DataEntityFactory.createReceipt(drugOrderStep);

        receiptDrugLifeCycle.addOperation(receipt);

        return drugLifeCycleReceiptRepository.updateWithInsert(receiptDrugLifeCycle, Optional.of(drug.getId()));
    }

    @Override
    public boolean distributor(DrugInfo drug, DrugOrderStep drugOrderStep) {
        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null == receiptDrugLifeCycle){
            receiptDrugLifeCycle =  DataEntityFactory.createDrugLifeCycleReceipt(drug);
        }

        Receipt receipt = DataEntityFactory.createReceipt(drugOrderStep);

        receiptDrugLifeCycle.addOperation(receipt);

        return drugLifeCycleReceiptRepository.updateWithInsert(receiptDrugLifeCycle, Optional.of(drug.getId()));
    }

    @Override
    public boolean consumer(DrugLifeCycle drugLifeCycle) {

        //TODO: 1. UpdateWithInsert consumers into database(if existed, feed with dose)
        //TODO: 2. UpdateWithInsert receiptDrugLifeCycle.

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
