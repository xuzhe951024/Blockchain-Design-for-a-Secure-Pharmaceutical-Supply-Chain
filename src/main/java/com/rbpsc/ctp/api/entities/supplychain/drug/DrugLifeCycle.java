package com.rbpsc.ctp.api.entities.supplychain.drug;

import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.dto.OperationVO;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
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
    List<OperationVO> operationVOQueue;
    Consumer expectedReceiver;

    public OperationVO pollOperationVOQ(){
        OperationVO operationVO = this.operationVOQueue.get(0);
        return operationVOQueue.remove(0);
    }

    public OperationVO peakOperationVOQ(){
        return this.operationVOQueue.get(0);
    }

    public void addOperation(OperationVO operationVO){
        this.operationVOQueue.add(operationVO);
    }

    public void setTagTagId(String tagTagId){
        this.drug.setDrugTagTagId(tagTagId);
    }

    public int getOperationQueueSize(){
        return this.operationVOQueue.size();
    }

    public boolean isProduced(){
        return StringUtils.isEmpty(drug.getDrugTagTagId());
    }
}
