package main.java.com.rbpsc.repository.service.base;

import main.java.org.rbpsc.api.entities.work_request.WorkLoadRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseWorkLoadRecordRepository extends BaseRepositoryForMongoDB<WorkLoadRecord, String> {
}
