package com.rbpsc.ctp.repository.impl;

import com.mongodb.client.result.UpdateResult;
import com.rbpsc.ctp.repository.service.WorkLoadRecordRepository;
import com.rbpsc.ctp.api.entities.WorkLoadRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import static com.rbpsc.ctp.common.Constant.WLConstants.MANGODB_COLLECTION_NAME_BENCHMARK;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/27
 **/
@Repository
public class WorkLoadRecordRepositoryImpl implements WorkLoadRecordRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(WorkLoadRecord workLoadRecord) {
        mongoTemplate.insert(workLoadRecord, MANGODB_COLLECTION_NAME_BENCHMARK);
    }

    @Override
    public void delete(WorkLoadRecord workLoadRecord) {
        mongoTemplate.remove(workLoadRecord, MANGODB_COLLECTION_NAME_BENCHMARK);
    }

    @Override
    public boolean update(String id, String field, String value) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set(field, value);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, WorkLoadRecord.class, MANGODB_COLLECTION_NAME_BENCHMARK);
        return updateResult.getMatchedCount() * updateResult.getModifiedCount() > 0;
    }

    @Override
    public WorkLoadRecord selectById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, WorkLoadRecord.class, MANGODB_COLLECTION_NAME_BENCHMARK);
    }

}
