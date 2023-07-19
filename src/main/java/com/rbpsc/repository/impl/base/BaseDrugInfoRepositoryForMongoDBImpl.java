package com.rbpsc.repository.impl.base;

import org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.repository.service.base.BaseDrugInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BaseDrugInfoRepositoryForMongoDBImpl {

    private final BaseDrugInfoRepository baseRepositoryForMongoDB;

    public BaseDrugInfoRepositoryForMongoDBImpl(BaseDrugInfoRepository baseRepositoryForMongoDB) {
        this.baseRepositoryForMongoDB = baseRepositoryForMongoDB;
    }

    public void save(DrugInfo entity) {
        baseRepositoryForMongoDB.save(entity);
    }

    public List<DrugInfo> findAll() {
        return baseRepositoryForMongoDB.findAll();
    }

    public Optional<DrugInfo> findById(String id) {
        return baseRepositoryForMongoDB.findById(id);
    }

    public void deleteById(String id) {
        baseRepositoryForMongoDB.deleteById(id);
    }

}
