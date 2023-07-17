package main.java.com.rbpsc.ctp.repository.service;

import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrugLifeCycleReceiptRepository {
    void insertDrugLifeCycleReceipt(DrugLifeCycle<Receipt> receiptDrugLifeCycle);

    void deleteDrugLifeCycleReceipt(DrugLifeCycle<Receipt> receiptDrugLifeCycle);

    DrugLifeCycle<Receipt> selectDrugLifeCycleReceiptById(String id);

    public List<DrugLifeCycle<Receipt>> findAll();

    public boolean updateWithInsert(DrugLifeCycle<Receipt> receiptDrugLifeCycle, Optional<String> id);
}
