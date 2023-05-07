package com.rbpsc.ctp.repository.impl.base;
import com.mongodb.bulk.BulkWriteResult;

import com.mongodb.client.result.UpdateResult;
import com.rbpsc.ctp.api.entities.base.BaseEntity;
import com.rbpsc.ctp.repository.service.base.BaseRepositoryForMongoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ReflectionUtils;



import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Repository
public class BaseRepositoryForMongoDBImpl<T extends BaseEntity<ID>, ID> {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BaseRepositoryForMongoDB<T, ID> baseRepositoryForMongoDB;

    public T save(T entity) {
        return baseRepositoryForMongoDB.save(entity);
    }

    public List<T> findAll() {
        return baseRepositoryForMongoDB.findAll();
    }

    public Optional<T> findById(ID id) {
        return baseRepositoryForMongoDB.findById(id);
    }

    public void deleteById(ID id) {
        baseRepositoryForMongoDB.deleteById(id);
    }

    public List<T> findAllByExample(T entity) {
        Query query = new Query();
        for (Field field : entity.getClass().getDeclaredFields()) {

            boolean isAccessible = field.isAccessible();
            if (!isAccessible) {
                field.setAccessible(true);
            }

            Object value = ReflectionUtils.getField(field, entity);

            if (!isAccessible) {
                field.setAccessible(false);
            }

            if (value != null) {
                query.addCriteria(Criteria.where(field.getName()).is(value));
            }
        }
        return mongoTemplate.find(query, (Class<T>) entity.getClass());
    }

    public boolean update(T entity) {
        Query query = new Query(Criteria.where(entity.getIdFieldName()).is(entity.getId()));
        Update update = new Update();
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (!field.getName().equals(entity.getIdFieldName())) {

                boolean isAccessible = field.isAccessible();
                if (!isAccessible) {
                    field.setAccessible(true);
                }

                Object value = ReflectionUtils.getField(field, entity);

                if (!isAccessible) {
                    field.setAccessible(false);
                }

                if (value != null) {
                    update.set(field.getName(), value);
                }
            }
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, entity.getClass());
        return updateResult.getModifiedCount() * updateResult.getMatchedCount() != 0;
    }

    public BulkWriteResult saveAll(List<T> entities) {
        return mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, (Class<T>) entities.get(0).getClass()).insert(entities).execute();
    }

    public Page<T> findAll(Pageable pageable, Class<T> entityClass) {
        long total = baseRepositoryForMongoDB.count();
        List<T> entities = baseRepositoryForMongoDB.findAll(pageable).getContent();
        return new PageImpl<>(entities, pageable, total);
    }
}