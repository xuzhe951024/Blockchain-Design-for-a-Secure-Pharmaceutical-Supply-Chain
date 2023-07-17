package main.java.com.rbpsc.ctp.repository.service.base;

import main.java.com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseWorkLoadRecordRepository extends BaseRepositoryForMongoDB<WorkLoadRecord, String> {
}
