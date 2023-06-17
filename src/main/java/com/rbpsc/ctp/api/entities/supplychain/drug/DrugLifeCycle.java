package com.rbpsc.ctp.api.entities.supplychain.drug;

import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.util.List;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DrugLifeCycle <T> extends SupplyChainBaseEntity {
    DrugInfo drug;
    List<T> operationDTOQueue;
    Consumer expectedReceiver;

    public T pollOperationVOQ(){
        return operationDTOQueue.remove(0);
    }

    public T peakOperationVOQ(){
        return this.operationDTOQueue.get(0);
    }

    public void addOperation(T operationDTO){
        this.operationDTOQueue.add(operationDTO);
    }

    public void setTagTagId(String tagTagId){
        this.drug.setDrugTagTagId(tagTagId);
    }

    public int getOperationQueueSize(){
        return this.operationDTOQueue.size();
    }

    public boolean isProduced(){
        return StringUtils.isEmpty(drug.getDrugTagTagId());
    }
}
