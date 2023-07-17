package main.java.com.rbpsc.ctp.repository.service.base;

import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDrugInfoRepository extends BaseRepositoryForMongoDB<DrugInfo, String>{
}
