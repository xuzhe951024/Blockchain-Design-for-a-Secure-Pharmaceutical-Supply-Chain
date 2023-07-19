package main.java.com.rbpsc.repository.impl;

import main.java.org.rbpsc.api.entities.supplychain.operations.attack.AttackConfidentiality;
import main.java.com.rbpsc.repository.impl.base.BaseAttackConfidentialityRepositoryForMongoDBImpl;
import main.java.com.rbpsc.repository.service.AttackConfidentialityRepository;
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
