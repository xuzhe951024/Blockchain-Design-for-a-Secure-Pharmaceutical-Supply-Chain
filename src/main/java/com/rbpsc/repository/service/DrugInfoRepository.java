package com.rbpsc.repository.service;

import org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugInfoRepository {
    void insertDrugInfo(DrugInfo drugInfo);

    void deleteDrugInfo(DrugInfo drugInfo);

    DrugInfo selectDrugInfoById(String id);

    List<DrugInfo> findAll();
}
