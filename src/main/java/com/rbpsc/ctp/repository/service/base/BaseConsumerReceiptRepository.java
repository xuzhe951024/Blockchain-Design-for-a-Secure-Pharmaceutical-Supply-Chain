package com.rbpsc.ctp.repository.service.base;

import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseConsumerReceiptRepository extends BaseRepositoryForMongoDB<Consumer, String>{
}
