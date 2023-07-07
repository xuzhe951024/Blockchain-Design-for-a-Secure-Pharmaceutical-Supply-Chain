package com.rbpsc.ctp.repository.service.base;

import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRoleBaseRepository extends BaseRepositoryForMongoDB<RoleBase, String>{
}
