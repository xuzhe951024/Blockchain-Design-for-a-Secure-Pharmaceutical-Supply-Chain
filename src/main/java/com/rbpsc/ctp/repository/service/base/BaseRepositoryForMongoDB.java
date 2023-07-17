package main.java.com.rbpsc.ctp.repository.service.base;

import main.java.com.rbpsc.ctp.api.entities.base.BaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepositoryForMongoDB<T extends BaseEntity<ID>, ID> extends MongoRepository<T, ID> {
}
