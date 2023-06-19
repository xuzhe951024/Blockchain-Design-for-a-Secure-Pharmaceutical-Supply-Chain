package com.rbpsc.ctp.repository.impl;

import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.repository.impl.base.BaseAttackConfidentialityRepositoryForMongoDBImpl;
import com.rbpsc.ctp.repository.service.AttackConfidentialityRepository;
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
    final
    BaseAttackConfidentialityRepositoryForMongoDBImpl baseAttackConfidentialityRepositoryForMongoDB;

    public AttackConfidentialityRepositoryImpl(BaseAttackConfidentialityRepositoryForMongoDBImpl baseAttackConfidentialityRepositoryForMongoDB) {
        this.baseAttackConfidentialityRepositoryForMongoDB = baseAttackConfidentialityRepositoryForMongoDB;
    }

    @Override
    public void insertAttack(AttackConfidentiality attackConfidentiality) {
        baseAttackConfidentialityRepositoryForMongoDB.save(attackConfidentiality);
    }

    @Override
    public void deleteAttack(AttackConfidentiality attackConfidentiality) {
        baseAttackConfidentialityRepositoryForMongoDB.deleteById(attackConfidentiality.getId());
    }

    @Override
    public AttackConfidentiality selectAttackById(String id) {
        return baseAttackConfidentialityRepositoryForMongoDB.findById(id).orElse(null);
    }

    @Override
    public List<AttackConfidentiality> findAll() {
        return baseAttackConfidentialityRepositoryForMongoDB.findAll();
    }
}
