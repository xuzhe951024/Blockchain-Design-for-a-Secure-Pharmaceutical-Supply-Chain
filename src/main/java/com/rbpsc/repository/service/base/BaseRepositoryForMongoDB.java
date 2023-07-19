package main.java.com.rbpsc.repository.service.base;

import main.java.org.rbpsc.api.entities.base.BaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepositoryForMongoDB<T extends BaseEntity<ID>, ID> extends MongoRepository<T, ID> {
}
