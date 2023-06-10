package com.rbpsc.ctp.api.entities.supplychain.drug;

import com.rbpsc.ctp.api.entities.dto.OperationDTO;
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
public class DrugLifeCycle extends SupplyChainBaseEntity {
    DrugInfo drug;
    List<OperationDTO> operationDTOQueue;
    Consumer expectedReceiver;

    public OperationDTO pollOperationVOQ(){
        OperationDTO operationDTO = this.operationDTOQueue.get(0);
        return operationDTOQueue.remove(0);
    }

    public OperationDTO peakOperationVOQ(){
        return this.operationDTOQueue.get(0);
    }

    public void addOperation(OperationDTO operationDTO){
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
