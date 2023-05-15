package com.rbpsc.ctp.repository.impl.base;
import com.mongodb.bulk.BulkWriteResult;

import com.mongodb.client.result.UpdateResult;
import com.rbpsc.ctp.api.entities.base.BaseEntity;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import com.rbpsc.ctp.repository.service.base.BaseRepositoryForMongoDB;
import com.rbpsc.ctp.repository.service.base.BaseWorkLoadRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ReflectionUtils;



import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Repository
public class BaseWorkLoadRecordRepositoryForMongoDBImpl {

    @Autowired
    private MongoDBUtil<String, WorkLoadRecord> mongoDBUtil;

    @Autowired
    private BaseWorkLoadRecordRepository baseRepositoryForMongoDB;

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
