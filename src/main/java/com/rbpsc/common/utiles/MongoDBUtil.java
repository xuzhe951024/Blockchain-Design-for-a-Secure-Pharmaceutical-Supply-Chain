package com.rbpsc.common.utiles;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.result.UpdateResult;
import org.rbpsc.api.entities.base.BaseEntity;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

@Repository
public class MongoDBUtil<T, E> {
    private final MongoTemplate mongoTemplate;

    public MongoDBUtil(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean autoUpdateMongoDB(BaseEntity<T> entity){
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

    public List<E> findAllByExample(E entity) {
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
        return mongoTemplate.find(query, (Class<E>) entity.getClass());
    }

    public BulkWriteResult saveAll(List<E> entities) {
        return mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, entities.get(0).getClass()).insert(entities).execute();
    }
}
