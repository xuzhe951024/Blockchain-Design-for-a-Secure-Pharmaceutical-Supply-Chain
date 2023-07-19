package main.java.com.rbpsc.repository.impl.base;

import main.java.org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.org.rbpsc.api.entities.supplychain.operations.Receipt;
import main.java.com.rbpsc.common.utiles.MongoDBUtil;
import main.java.com.rbpsc.repository.service.base.BaseDrugLifeCycleReceiptRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BaseDrugLifeCycleReceiptForMongoDBImpl {
    private final BaseDrugLifeCycleReceiptRepository baseRepositoryForMongoDB;

    private final MongoDBUtil<String, DrugLifeCycle<Receipt>> mongoDBUtil;

    public BaseDrugLifeCycleReceiptForMongoDBImpl(BaseDrugLifeCycleReceiptRepository baseRepositoryForMongoDB, MongoDBUtil<String, DrugLifeCycle<Receipt>> mongoDBUtil) {
        this.baseRepositoryForMongoDB = baseRepositoryForMongoDB;
        this.mongoDBUtil = mongoDBUtil;
    }

    public void save(DrugLifeCycle<Receipt> entity) {
        baseRepositoryForMongoDB.save(entity);
    }

    public List<DrugLifeCycle<Receipt>> findAll() {
        return baseRepositoryForMongoDB.findAll();
    }

    public Optional<DrugLifeCycle<Receipt>> findById(String id) {
        return baseRepositoryForMongoDB.findById(id);
    }

    public void deleteById(String id) {
        baseRepositoryForMongoDB.deleteById(id);
    }

    public boolean update(DrugLifeCycle<Receipt> receiptDrugLifeCycle){
        return mongoDBUtil.autoUpdateMongoDB(receiptDrugLifeCycle);
    }
}
