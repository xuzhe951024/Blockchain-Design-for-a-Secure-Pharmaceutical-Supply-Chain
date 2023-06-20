package com.rbpsc.ctp.repository.impl;

import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.repository.impl.base.BaseConsumerReceiptRepositoryForMongoDBImpl;
import com.rbpsc.ctp.repository.service.ConsumerReceiptRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class ConsumerReceiptRepositoryImpl implements ConsumerReceiptRepository {

    private final BaseConsumerReceiptRepositoryForMongoDBImpl baseConsumerReceiptRepositoryForMongoDB;

    public ConsumerReceiptRepositoryImpl(BaseConsumerReceiptRepositoryForMongoDBImpl baseConsumerReceiptRepositoryForMongoDB) {
        this.baseConsumerReceiptRepositoryForMongoDB = baseConsumerReceiptRepositoryForMongoDB;
    }


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
    public Consumer selectConsumerReceiptById(String id) {
        return baseConsumerReceiptRepositoryForMongoDB.findById(id).orElse(null);
    }

    @Override
    public List<Consumer> findAll() {
        return baseConsumerReceiptRepositoryForMongoDB.findAll();
    }

    @Override
    public boolean updateWithInsert(Consumer consumer) {

        if (StringUtils.isEmpty(consumer.getId())) {
            return false;
        }

        if (this.selectConsumerReceiptById(consumer.getId()) == null) {
            this.insertConsumerReceipt(consumer);
            return true;
        }

        return baseConsumerReceiptRepositoryForMongoDB.update(consumer);
    }
}
