package com.rbpsc.ctp.repository.service.base;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDrugLifeCycleReceiptRepository extends BaseRepositoryForMongoDB<DrugLifeCycle<Receipt>, String> {
}
