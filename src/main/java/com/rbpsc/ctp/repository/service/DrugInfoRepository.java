package main.java.com.rbpsc.ctp.repository.service;

import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugInfoRepository {
    void insertDrugInfo(DrugInfo drugInfo);

    void deleteDrugInfo(DrugInfo drugInfo);

    DrugInfo selectDrugInfoById(String id);

    public List<DrugInfo> findAll();
}
