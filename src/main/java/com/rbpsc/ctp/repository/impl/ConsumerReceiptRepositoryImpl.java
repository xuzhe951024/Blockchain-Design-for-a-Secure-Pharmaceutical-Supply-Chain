package com.rbpsc.ctp.repository.impl;

import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.repository.impl.base.BaseConsumerReceiptRepositoryForMongoDBImpl;
import com.rbpsc.ctp.repository.service.ConsumerReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConsumerReceiptRepositoryImpl implements ConsumerReceiptRepository {

    @Autowired
    private BaseConsumerReceiptRepositoryForMongoDBImpl baseConsumerReceiptRepositoryForMongoDB;


    @Override
    public void insertConsumerReceipt(Consumer consumer) {
        baseConsumerReceiptRepositoryForMongoDB.save(consumer);
    }

    @Override
    public void deleteConsumerReceipt(Consumer consumer) {
        baseConsumerReceiptRepositoryForMongoDB.deleteById(consumer.getId());
    }

    @Override
    public boolean modifyConsumerReceipt(Consumer consumer) {
        return baseConsumerReceiptRepositoryForMongoDB.update(consumer);
    }

    @Override
    public Consumer selectConsumerReceiptById(Consumer consumer) {
        return baseConsumerReceiptRepositoryForMongoDB.findById(consumer.getId()).orElse(null);
    }

    @Override
    public List<Consumer> findAll() {
        return baseConsumerReceiptRepositoryForMongoDB.findAll();
    }
}
