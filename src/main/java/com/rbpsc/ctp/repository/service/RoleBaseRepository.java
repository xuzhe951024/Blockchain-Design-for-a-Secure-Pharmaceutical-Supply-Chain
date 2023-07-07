package com.rbpsc.ctp.repository.service;

import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleBaseRepository {
    void insertRoleBase(RoleBase roleBase);

    void deleteRoleBase(RoleBase roleBase);

    boolean modifyRoleBase(RoleBase roleBase);

    RoleBase selectRoleBaseById(String id);

    public List<RoleBase> findAll();

    public boolean updateWithInsert(RoleBase roleBase);
}
