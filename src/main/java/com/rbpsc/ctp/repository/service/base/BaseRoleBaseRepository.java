package main.java.com.rbpsc.ctp.repository.service.base;

import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRoleBaseRepository extends BaseRepositoryForMongoDB<RoleBase, String>{
}
