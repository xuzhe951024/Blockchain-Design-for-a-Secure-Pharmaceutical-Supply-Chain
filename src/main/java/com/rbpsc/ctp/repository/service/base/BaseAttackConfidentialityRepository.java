package main.java.com.rbpsc.ctp.repository.service.base;

import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import org.springframework.stereotype.Repository;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/6/23
 **/
@Repository
public interface BaseAttackConfidentialityRepository extends BaseRepositoryForMongoDB<AttackConfidentiality, String>{
}
