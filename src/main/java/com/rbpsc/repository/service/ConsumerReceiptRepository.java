package com.rbpsc.repository.service;

import org.rbpsc.api.entities.supplychain.roles.Consumer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerReceiptRepository {
    void insertConsumerReceipt(Consumer consumer);

    void deleteConsumerReceipt(Consumer consumer);

    boolean modifyConsumerReceipt(Consumer consumer);

    Consumer selectConsumerReceiptById(String id);

    List<Consumer> findAll();

    boolean updateWithInsert(Consumer consumer);
}
