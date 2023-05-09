package com.rbpsc.ctp.repository.service;

import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;

import java.util.List;

public interface DrugInfoRepository {
    void insertDrugInfo(DrugInfo drugInfo);

    void deleteDrugInfo(DrugInfo drugInfo);

    DrugInfo selectDrugInfoById(DrugInfo drugInfo);

    public List<DrugInfo> findAll();
}
