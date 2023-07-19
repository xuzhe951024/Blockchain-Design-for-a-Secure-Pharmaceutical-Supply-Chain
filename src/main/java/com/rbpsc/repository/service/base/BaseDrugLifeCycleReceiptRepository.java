package main.java.com.rbpsc.repository.service.base;

import main.java.org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.org.rbpsc.api.entities.supplychain.operations.Receipt;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDrugLifeCycleReceiptRepository extends BaseRepositoryForMongoDB<DrugLifeCycle<Receipt>, String> {
}
