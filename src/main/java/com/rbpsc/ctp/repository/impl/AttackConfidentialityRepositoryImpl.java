package com.rbpsc.ctp.repository.impl;

import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.repository.impl.base.BaseAttackConfidentialityRepositoryForMongoDBImpl;
import com.rbpsc.ctp.repository.service.AttackConfidentialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/6/23
 **/
@Repository
public class AttackConfidentialityRepositoryImpl implements AttackConfidentialityRepository {
    @Autowired
    BaseAttackConfidentialityRepositoryForMongoDBImpl baseAttackConfidentialityRepositoryForMongoDB;

    @Override
    public void insertAttack(AttackConfidentiality attackConfidentiality) {
        baseAttackConfidentialityRepositoryForMongoDB.save(attackConfidentiality);
    }

    @Override
    public void deleteAttack(AttackConfidentiality attackConfidentiality) {
        baseAttackConfidentialityRepositoryForMongoDB.deleteById(attackConfidentiality.getId());
    }

    @Override
    public AttackConfidentiality selectAttackById(AttackConfidentiality attackConfidentiality) {
        return baseAttackConfidentialityRepositoryForMongoDB.findById(attackConfidentiality.getId()).orElse(null);
    }

    @Override
    public List<AttackConfidentiality> findAll() {
        return baseAttackConfidentialityRepositoryForMongoDB.findAll();
    }
}
