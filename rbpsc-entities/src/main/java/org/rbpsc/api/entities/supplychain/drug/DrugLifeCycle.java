package org.rbpsc.api.entities.supplychain.drug;

import org.rbpsc.api.entities.supplychain.SupplyChainBaseEntity;
import org.rbpsc.api.entities.supplychain.roles.Consumer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@DataType()
public class DrugLifeCycle <T> extends SupplyChainBaseEntity {
    @Property()
    DrugInfo drug;

    @Property()
    List<T> lifeCycleQueue;

    @Property()
    Consumer expectedReceiver;

    @Property()
    Boolean isAttacked = false;

    public T pollOperationVOQ(){
        return lifeCycleQueue.remove(0);
    }

    public T peakOperationVOQ(){
        return this.lifeCycleQueue.get(0);
    }

    public void addOperation(T operationDTO){
        if (!this.isAttacked){
            this.lifeCycleQueue.add(operationDTO);
        }
    }

    public void setTagTagId(String tagTagId){
        this.drug.setDrugTagTagId(tagTagId);
    }

    public int getOperationQueueSize(){
        return this.lifeCycleQueue.size();
    }

    public boolean isProduced(){
        return StringUtils.isEmpty(drug.getDrugTagTagId());
    }
}
