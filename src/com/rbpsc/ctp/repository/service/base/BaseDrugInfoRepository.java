package com.rbpsc.ctp.repository.service.base;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDrugInfoRepository extends BaseRepositoryForMongoDB<DrugInfo, String>{
}
