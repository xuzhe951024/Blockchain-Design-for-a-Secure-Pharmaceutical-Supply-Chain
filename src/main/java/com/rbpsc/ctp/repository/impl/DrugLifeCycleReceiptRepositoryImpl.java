package com.rbpsc.ctp.repository.impl;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import com.rbpsc.ctp.repository.impl.base.BaseDrugLifeCycleReceiptForMongoDBImpl;
import com.rbpsc.ctp.repository.service.DrugLifeCycleReceiptRepository;
import com.rbpsc.ctp.repository.service.base.BaseDrugLifeCycleReceiptRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class DrugLifeCycleReceiptRepositoryImpl implements DrugLifeCycleReceiptRepository {
    private final BaseDrugLifeCycleReceiptForMongoDBImpl baseDrugLifeCycleReceiptRepository;



    public DrugLifeCycleReceiptRepositoryImpl(BaseDrugLifeCycleReceiptForMongoDBImpl baseDrugLifeCycleReceiptRepository) {
        this.baseDrugLifeCycleReceiptRepository = baseDrugLifeCycleReceiptRepository;
    }

    @Override
    public void insertDrugLifeCycleReceipt(DrugLifeCycle<Receipt> receiptDrugLifeCycle) {
        baseDrugLifeCycleReceiptRepository.save(receiptDrugLifeCycle);
    }

    @Override
    public void deleteDrugLifeCycleReceipt(DrugLifeCycle<Receipt> receiptDrugLifeCycle) {
        baseDrugLifeCycleReceiptRepository.deleteById(receiptDrugLifeCycle.getId());
    }

    @Override
    public DrugLifeCycle<Receipt> selectDrugLifeCycleReceiptById(String id) {
        return baseDrugLifeCycleReceiptRepository.findById(id).orElse(null);
    }

    @Override
    public List<DrugLifeCycle<Receipt>> findAll() {
        return baseDrugLifeCycleReceiptRepository.findAll();
    }

    @Override
    public boolean updateWithInsert(DrugLifeCycle<Receipt> receiptDrugLifeCycle, Optional<String> id) {
        String keyId = id.orElse(receiptDrugLifeCycle.getId());

        if (StringUtils.isEmpty(keyId)){
            return false;
        }

        if (null == this.selectDrugLifeCycleReceiptById(keyId)){
            this.insertDrugLifeCycleReceipt(receiptDrugLifeCycle);
            return true;
        }

        return baseDrugLifeCycleReceiptRepository.update(receiptDrugLifeCycle);
    }
}
