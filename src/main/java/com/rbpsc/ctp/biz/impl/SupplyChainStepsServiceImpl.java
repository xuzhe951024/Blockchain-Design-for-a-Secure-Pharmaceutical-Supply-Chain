package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.biz.service.SupplyChainStepsService;
import com.rbpsc.ctp.repository.service.ConsumerReceiptRepository;
import com.rbpsc.ctp.repository.service.DrugInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SupplyChainStepsServiceImpl implements SupplyChainStepsService {

    @Autowired
    DrugInfoRepository drugInfoRepository;

    @Autowired
    ConsumerReceiptRepository consumerReceiptRepository;

    @Override
    public DrugInfo manufacture(DrugInfo drug) {
        drug.setDrugTagTagId(UUID.randomUUID().toString());

        drugInfoRepository.insertDrugInfo(drug);

        return drug;
    }

    @Override
    public boolean distributor(DrugInfo drug) {

        drugInfoRepository.insertDrugInfo(drug);

        return null == drugInfoRepository.selectDrugInfoById(drug);
    }

    @Override
    public boolean consumer(DrugLifeCycle drugLifeCycle) {

        Consumer consumer = consumerReceiptRepository.selectConsumerReceiptById(drugLifeCycle.getExpectedReceiver());
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
