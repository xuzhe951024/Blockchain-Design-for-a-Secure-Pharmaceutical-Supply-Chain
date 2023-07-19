package com.rbpsc.repository.service.base;

import org.rbpsc.api.entities.supplychain.roles.RoleBase;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRoleBaseRepository extends BaseRepositoryForMongoDB<RoleBase, String>{
}
