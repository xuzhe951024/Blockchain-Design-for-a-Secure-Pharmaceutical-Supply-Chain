package com.rbpsc.ctp.repository.service;

import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/6/23
 **/
@Repository
public interface AttackConfidentialityRepository {
    void insertAttack(AttackConfidentiality attackConfidentiality);

    void deleteAttack(AttackConfidentiality attackConfidentiality);

    AttackConfidentiality selectAttackById(String id);

    public List<AttackConfidentiality> findAll();
}
