package com.example.demo.biz;

import com.example.demo.biz.service.ICAdvisorRepository;
import com.example.demo.entities.CAdvisorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/27
 **/
@Repository
public class CAdvisorRepositoryImpl implements ICAdvisorRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(CAdvisorData cAdvisorData) {
        mongoTemplate.insert(cAdvisorData, "benchmark");
    }
}
