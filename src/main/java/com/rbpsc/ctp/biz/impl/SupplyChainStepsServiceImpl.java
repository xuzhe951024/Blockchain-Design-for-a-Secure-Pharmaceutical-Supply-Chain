package main.java.com.rbpsc.ctp.biz.impl;

import main.java.com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import main.java.com.rbpsc.ctp.biz.service.SupplyChainStepsService;
import main.java.com.rbpsc.ctp.repository.service.ConsumerReceiptRepository;
import main.java.com.rbpsc.ctp.repository.service.DrugLifeCycleReceiptRepository;
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
    public boolean manufacture(DrugInfo drug, OperationBase operationBase) {
        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null != receiptDrugLifeCycle){
            log.error("Drug can not be produce duplicate");
            return false;
        }

        receiptDrugLifeCycle =  DataEntityFactory.createDrugLifeCycleReceipt(drug);

        Receipt receipt = DataEntityFactory.createReceipt(operationBase);

        receiptDrugLifeCycle.addOperation(receipt);

        return drugLifeCycleReceiptRepository.updateWithInsert(receiptDrugLifeCycle, Optional.of(drug.getId()));
    }

    @Override
    public boolean distributor(DrugInfo drug, OperationBase operationBase) {
        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null == receiptDrugLifeCycle){
            log.error("Products must be produced before selling!");
            return false;
        }

        Receipt receipt = DataEntityFactory.createReceipt(operationBase);

        receiptDrugLifeCycle.addOperation(receipt);

        return drugLifeCycleReceiptRepository.updateWithInsert(receiptDrugLifeCycle, Optional.of(drug.getId()));
    }

    @Override
    public boolean consumer(DrugInfo drug, OperationBase operationBase, String targetConsumerId) {

        Consumer consumer = consumerReceiptRepository.selectConsumerReceiptById(targetConsumerId);
        if (null == consumer){
            log.error(String.format("No consumer information in database: \n {%s}", targetConsumerId));
            return false;
        }

        if (consumer.isSatisfied()){
            log.error(String.format("Consumer has already been satisfied:\n{%s}", consumer));
            return false;
        }

        consumer.satisfyDosage();

        DrugLifeCycle<Receipt> receiptDrugLifeCycle = drugLifeCycleReceiptRepository.selectDrugLifeCycleReceiptById(drug.getId());
        if (null == receiptDrugLifeCycle){
            log.error("Products must be produced before selling!");
            return false;
        }

        Receipt receipt = DataEntityFactory.createReceipt(operationBase);

        receiptDrugLifeCycle.addOperation(receipt);

        return consumerReceiptRepository.modifyConsumerReceipt(consumer)
                && drugLifeCycleReceiptRepository.updateWithInsert(receiptDrugLifeCycle, Optional.of(drug.getId()));
    }
}
