package main.java.com.rbpsc.repository.service.base;

import main.java.org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDrugInfoRepository extends BaseRepositoryForMongoDB<DrugInfo, String>{
}
