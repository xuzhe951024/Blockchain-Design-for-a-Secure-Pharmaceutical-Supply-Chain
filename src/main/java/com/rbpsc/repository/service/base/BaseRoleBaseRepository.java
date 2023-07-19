package main.java.com.rbpsc.repository.service.base;

import main.java.org.rbpsc.api.entities.supplychain.roles.RoleBase;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRoleBaseRepository extends BaseRepositoryForMongoDB<RoleBase, String>{
}
