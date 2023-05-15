package com.rbpsc.ctp.repository.impl.base;

import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.repository.service.base.BaseConsumerReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public class BaseConsumerReceiptRepositoryForMongoDBImpl {

    @Autowired
    private MongoDBUtil<String, Consumer> mongoDBUtil;

    @Autowired
    BaseConsumerReceiptRepository baseConsumerReceiptRepository;

    public Consumer save(Consumer entity) {
        return baseConsumerReceiptRepository.save(entity);
    }

    public List<Consumer> findAll() {
        return baseConsumerReceiptRepository.findAll();
    }

    public Optional<Consumer> findById(String id) {
        return baseConsumerReceiptRepository.findById(id);
    }

    public void deleteById(String id) {
        baseConsumerReceiptRepository.deleteById(id);
    }

    public boolean update(Consumer entity) {
        return mongoDBUtil.autoUpdateMongoDB(entity);
    }
}