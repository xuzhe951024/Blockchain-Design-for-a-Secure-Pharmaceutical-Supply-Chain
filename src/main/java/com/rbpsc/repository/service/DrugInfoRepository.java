package main.java.com.rbpsc.repository.service;

import main.java.org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugInfoRepository {
    void insertDrugInfo(DrugInfo drugInfo);

    void deleteDrugInfo(DrugInfo drugInfo);

    DrugInfo selectDrugInfoById(String id);

    List<DrugInfo> findAll();
}
