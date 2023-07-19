package main.java.com.rbpsc.repository.impl.base;

import main.java.org.rbpsc.api.entities.supplychain.roles.RoleBase;
import main.java.com.rbpsc.common.utiles.MongoDBUtil;
import main.java.com.rbpsc.repository.service.base.BaseRoleBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public class BaseRoleBaseRepositoryForMongoDBImpl {

    final MongoDBUtil<String, RoleBase> mongoDBUtil;

    final BaseRoleBaseRepository baseRoleBaseRepository;

    public BaseRoleBaseRepositoryForMongoDBImpl(MongoDBUtil<String, RoleBase> mongoDBUtil, BaseRoleBaseRepository baseRoleBaseRepository) {
        this.mongoDBUtil = mongoDBUtil;
        this.baseRoleBaseRepository = baseRoleBaseRepository;
    }

    public RoleBase save(RoleBase entity) {
        return baseRoleBaseRepository.save(entity);
    }

    public List<RoleBase> findAll() {
        return baseRoleBaseRepository.findAll();
    }

    public Optional<RoleBase> findById(String id) {
        return baseRoleBaseRepository.findById(id);
    }

    public void deleteById(String id) {
        baseRoleBaseRepository.deleteById(id);
    }

    public boolean update(RoleBase entity) {
        return mongoDBUtil.autoUpdateMongoDB(entity);
    }

    public List<RoleBase> findByExample(RoleBase entity){
        return mongoDBUtil.findAllByExample(entity);
    }
}
