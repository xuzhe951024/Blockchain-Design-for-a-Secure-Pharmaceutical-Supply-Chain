package com.rbpsc.ctp.repository.service.base;

import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
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
