package com.rbpsc.ctp.api.entities.dto.webview;

import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Document
@ToString(callSuper = true)
public class DrugLifeCycleVO extends SupplyChainBaseEntity {
    String drugName;
    String drugId;
    String physicalMarking;
    String targetConsumer;
    List<OperationVO> operationVOList;

    public void createSelf(DrugLifeCycle drugLifeCycle, List<OperationVO> operationVOList){
        DrugInfo drugInfo = drugLifeCycle.getDrug();
        this.drugName = drugInfo.getDrugName();
        this.drugId = drugInfo.getId();
        this.physicalMarking = drugInfo.getDrugTagTagId();
        this.targetConsumer = drugLifeCycle.getExpectedReceiver().getAddress();

        this.operationVOList = operationVOList;

        this.setBatchId(drugLifeCycle.getBatchId());
        this.setId(UUID.randomUUID().toString());
    }
}
