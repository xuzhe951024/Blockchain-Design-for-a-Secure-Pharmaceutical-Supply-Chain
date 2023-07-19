package com.rbpsc.repository.service.base;

import org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import org.rbpsc.api.entities.supplychain.operations.Receipt;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDrugLifeCycleReceiptRepository extends BaseRepositoryForMongoDB<DrugLifeCycle<Receipt>, String> {
}
