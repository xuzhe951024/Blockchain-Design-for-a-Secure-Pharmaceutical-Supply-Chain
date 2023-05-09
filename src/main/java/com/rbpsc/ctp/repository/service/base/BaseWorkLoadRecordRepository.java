package com.rbpsc.ctp.repository.service.base;

import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseWorkLoadRecordRepository extends BaseRepositoryForMongoDB<WorkLoadRecord, String> {
}
