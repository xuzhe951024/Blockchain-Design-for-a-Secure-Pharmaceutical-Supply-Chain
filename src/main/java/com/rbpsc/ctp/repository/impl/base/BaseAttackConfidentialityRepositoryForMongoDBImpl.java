package main.java.com.rbpsc.ctp.repository.impl.base;

import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import main.java.com.rbpsc.ctp.repository.service.base.BaseAttackConfidentialityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/6/23
 **/
@Repository
public class BaseAttackConfidentialityRepositoryForMongoDBImpl {
    private final BaseAttackConfidentialityRepository baseAttackConfidentialityRepository;

    public BaseAttackConfidentialityRepositoryForMongoDBImpl(BaseAttackConfidentialityRepository baseAttackConfidentialityRepository) {
        this.baseAttackConfidentialityRepository = baseAttackConfidentialityRepository;
    }

    public AttackConfidentiality save(AttackConfidentiality entity) {
        return baseAttackConfidentialityRepository.save(entity);
    }

    public List<AttackConfidentiality> findAll() {
        return baseAttackConfidentialityRepository.findAll();
    }

    public Optional<AttackConfidentiality> findById(String id) {
        return baseAttackConfidentialityRepository.findById(id);
    }

    public void deleteById(String id) {
        baseAttackConfidentialityRepository.deleteById(id);
    }
}
