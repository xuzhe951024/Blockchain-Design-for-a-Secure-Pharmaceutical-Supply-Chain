package com.rbpsc.repository.service;

import org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import org.rbpsc.api.entities.supplychain.operations.Receipt;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrugLifeCycleReceiptRepository {
    void insertDrugLifeCycleReceipt(DrugLifeCycle<Receipt> receiptDrugLifeCycle);

    void deleteDrugLifeCycleReceipt(DrugLifeCycle<Receipt> receiptDrugLifeCycle);

    DrugLifeCycle<Receipt> selectDrugLifeCycleReceiptById(String id);

    List<DrugLifeCycle<Receipt>> findAll();

    boolean updateWithInsert(DrugLifeCycle<Receipt> receiptDrugLifeCycle, Optional<String> id);
}
