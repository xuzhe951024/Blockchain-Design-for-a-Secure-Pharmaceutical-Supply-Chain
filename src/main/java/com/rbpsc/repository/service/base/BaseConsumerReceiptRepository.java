package main.java.com.rbpsc.repository.service.base;

import main.java.org.rbpsc.api.entities.supplychain.roles.Consumer;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseConsumerReceiptRepository extends BaseRepositoryForMongoDB<Consumer, String>{
}
