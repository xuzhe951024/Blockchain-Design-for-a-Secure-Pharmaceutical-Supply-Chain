package main.java.com.rbpsc.ctp.repository.service.base;

import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseConsumerReceiptRepository extends BaseRepositoryForMongoDB<Consumer, String>{
}
