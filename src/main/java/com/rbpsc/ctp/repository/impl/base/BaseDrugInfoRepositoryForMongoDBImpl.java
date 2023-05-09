package com.rbpsc.ctp.repository.impl.base;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.UpdateResult;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import com.rbpsc.ctp.repository.service.base.BaseDrugInfoRepository;
import com.rbpsc.ctp.repository.service.base.BaseWorkLoadRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Repository
public class BaseDrugInfoRepositoryForMongoDBImpl {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BaseDrugInfoRepository baseRepositoryForMongoDB;

    public DrugInfo save(DrugInfo entity) {
        return baseRepositoryForMongoDB.save(entity);
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
