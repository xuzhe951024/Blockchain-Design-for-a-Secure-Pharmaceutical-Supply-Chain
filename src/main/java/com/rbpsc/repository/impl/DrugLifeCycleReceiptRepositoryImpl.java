package main.java.com.rbpsc.repository.impl;

import main.java.org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.org.rbpsc.api.entities.supplychain.operations.Receipt;
import main.java.com.rbpsc.repository.service.DrugLifeCycleReceiptRepository;
import main.java.com.rbpsc.repository.impl.base.BaseDrugLifeCycleReceiptForMongoDBImpl;
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
