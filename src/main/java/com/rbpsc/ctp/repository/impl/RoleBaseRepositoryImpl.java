package com.rbpsc.ctp.repository.impl;

import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.repository.impl.base.BaseRoleBaseRepositoryForMongoDBImpl;
import com.rbpsc.ctp.repository.service.RoleBaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class RoleBaseRepositoryImpl implements RoleBaseRepository {

    private final BaseRoleBaseRepositoryForMongoDBImpl baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB;

    public RoleBaseRepositoryImpl(BaseRoleBaseRepositoryForMongoDBImpl baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB) {

        this.baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB = baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB;
    }


    @Override
    public void insertRoleBase(RoleBase roleBase) {
        baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB.save(roleBase);
    }

    @Override
    public void deleteRoleBase(RoleBase roleBase) {
        baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB.deleteById(roleBase.getId());
    }

    @Override
    public boolean modifyRoleBase(RoleBase roleBase) {
        return baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB.update(roleBase);
    }

    @Override
    public RoleBase selectRoleBaseById(String id) {
        return baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB.findById(id).orElse(null);
    }

    @Override
    public List<RoleBase> findAll() {
        return baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB.findAll();
    }

    @Override
    public boolean updateWithInsert(RoleBase roleBase) {

        if (StringUtils.isEmpty(roleBase.getId())) {
            return false;
        }

        if (this.selectRoleBaseById(roleBase.getId()) == null) {
            this.insertRoleBase(roleBase);
            return true;
        }

        return baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB.update(roleBase);
    }

    @Override
    public List<RoleBase> findByExample(RoleBase roleBase) {
        return baseRoleBaseRepositoryForMongoDBRepositoryForMongoDB.findByExample(roleBase);
    }
}
