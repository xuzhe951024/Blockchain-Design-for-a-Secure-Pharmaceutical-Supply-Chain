package main.java.com.rbpsc.ctp.repository.service.base;

import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDrugLifeCycleReceiptRepository extends BaseRepositoryForMongoDB<DrugLifeCycle<Receipt>, String> {
}
