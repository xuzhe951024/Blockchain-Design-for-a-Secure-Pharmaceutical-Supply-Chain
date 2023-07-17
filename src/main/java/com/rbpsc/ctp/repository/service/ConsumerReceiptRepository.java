package main.java.com.rbpsc.ctp.repository.service;

import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerReceiptRepository {
    void insertConsumerReceipt(Consumer consumer);

    void deleteConsumerReceipt(Consumer consumer);

    boolean modifyConsumerReceipt(Consumer consumer);

    Consumer selectConsumerReceiptById(String id);

    public List<Consumer> findAll();

    public boolean updateWithInsert(Consumer consumer);
}
