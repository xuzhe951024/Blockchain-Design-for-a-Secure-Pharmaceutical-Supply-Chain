package com.rbpsc.ctp.repository.impl;

import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import com.rbpsc.ctp.repository.impl.base.BaseWorkLoadRecordRepositoryForMongoDBImpl;
import com.rbpsc.ctp.repository.service.WorkLoadRecordRepository;
import org.springframework.stereotype.Repository;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/27
 **/
@Repository
public class WorkLoadRecordRepositoryImpl implements WorkLoadRecordRepository {
    private final BaseWorkLoadRecordRepositoryForMongoDBImpl baseRepositoryForMongoDB;

    public WorkLoadRecordRepositoryImpl(BaseWorkLoadRecordRepositoryForMongoDBImpl baseRepositoryForMongoDB) {
        this.baseRepositoryForMongoDB = baseRepositoryForMongoDB;
    }

    @Override
    public void insertWorkLoadRecord(WorkLoadRecord workLoadRecord) {
        baseRepositoryForMongoDB.save(workLoadRecord);
    }

    @Override
    public void deleteWorkLoadRecord(WorkLoadRecord workLoadRecord) {
        baseRepositoryForMongoDB.deleteById(workLoadRecord.getId());
    }

    @Override
    public boolean updateWorkLoadRecord(WorkLoadRecord workLoadRecord) {
        return baseRepositoryForMongoDB.update(workLoadRecord);
    }

    @Override
    public WorkLoadRecord selectWorkLoadRecordById(WorkLoadRecord workLoadRecord) {
        return (WorkLoadRecord) baseRepositoryForMongoDB.findById(workLoadRecord.getId()).orElse(null);
    }

}
