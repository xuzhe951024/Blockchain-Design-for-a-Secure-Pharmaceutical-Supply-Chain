package main.java.com.rbpsc.ctp.api.entities.dto.webview;

import main.java.com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
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
    private String drugName;
    private String drugId;
    private String physicalMarking;
    private String targetConsumer;
    private int expectedDose;
    private List<OperationVO> operationVOList;

    // This method is for test only
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

    public void addOperationVO(OperationVO operationVO){
        this.operationVOList.add(operationVO);
    }

    public OperationVO peakOperationVO(){
        return this.operationVOList.get(0);
    }

    public OperationVO pollOperationVO(){
        return this.operationVOList.remove(0);
    }

    public int getOperationVOListSize(){
        return this.operationVOList.size();
    }
}
