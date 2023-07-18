package com.rbpsc.ctp.repository.impl.base;

import com.mongodb.bulk.BulkWriteResult;
import com.rbpsc.ctp.common.utiles.MongoDBUtil;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import com.rbpsc.ctp.repository.service.base.BaseWorkLoadRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BaseWorkLoadRecordRepositoryForMongoDBImpl {

    private final MongoDBUtil<String, WorkLoadRecord> mongoDBUtil;

    private final BaseWorkLoadRecordRepository baseRepositoryForMongoDB;

    public BaseWorkLoadRecordRepositoryForMongoDBImpl(MongoDBUtil<String, WorkLoadRecord> mongoDBUtil, BaseWorkLoadRecordRepository baseRepositoryForMongoDB) {
        this.mongoDBUtil = mongoDBUtil;
        this.baseRepositoryForMongoDB = baseRepositoryForMongoDB;
    }

    public WorkLoadRecord save(WorkLoadRecord entity) {
        return baseRepositoryForMongoDB.save(entity);
    }

    public List<WorkLoadRecord> findAll() {
        return baseRepositoryForMongoDB.findAll();
    }

    public Optional<WorkLoadRecord> findById(String id) {
        return baseRepositoryForMongoDB.findById(id);
    }

    public void deleteById(String id) {
        baseRepositoryForMongoDB.deleteById(id);
    }

    public List<WorkLoadRecord> findAllByExample(WorkLoadRecord entity) {
       return mongoDBUtil.findAllByExample(entity);
    }

    public boolean update(WorkLoadRecord entity) {
        return mongoDBUtil.autoUpdateMongoDB(entity);
    }

    public BulkWriteResult saveAll(List<WorkLoadRecord> entities) {
        return mongoDBUtil.saveAll(entities);
    }

    public Page<WorkLoadRecord> findAll(Pageable pageable, Class<WorkLoadRecord> entityClass) {
        long total = baseRepositoryForMongoDB.count();
        List<WorkLoadRecord> entities = baseRepositoryForMongoDB.findAll(pageable).getContent();
        return new PageImpl<>(entities, pageable, total);
    }
}
