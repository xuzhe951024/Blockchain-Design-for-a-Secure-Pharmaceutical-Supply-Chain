package com.rbpsc.repository.impl;

import org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.repository.impl.base.BaseDrugInfoRepositoryForMongoDBImpl;
import com.rbpsc.repository.service.DrugInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DrugInfoRepositoryImpl implements DrugInfoRepository {
    private final BaseDrugInfoRepositoryForMongoDBImpl baseRepositoryForMongoDB;

    public DrugInfoRepositoryImpl(BaseDrugInfoRepositoryForMongoDBImpl baseRepositoryForMongoDB) {
        this.baseRepositoryForMongoDB = baseRepositoryForMongoDB;
    }

    @Override
    public void insertDrugInfo(DrugInfo drugInfo) {
        baseRepositoryForMongoDB.save(drugInfo);
    }

    @Override
    public void deleteDrugInfo(DrugInfo drugInfo) {
        baseRepositoryForMongoDB.deleteById(drugInfo.getId());
    }

    @Override
    public DrugInfo selectDrugInfoById(String id) {
        return baseRepositoryForMongoDB.findById(id).orElse(null);
    }

    @Override
    public List<DrugInfo> findAll() {
        return baseRepositoryForMongoDB.findAll();
    }
}
