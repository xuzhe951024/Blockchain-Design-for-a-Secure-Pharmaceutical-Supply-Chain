package main.java.org.rbpsc.api.entities.supplychain.drug;

import main.java.org.rbpsc.api.entities.supplychain.SupplyChainBaseEntity;
import main.java.org.rbpsc.api.entities.supplychain.roles.Consumer;
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
    List<T> lifeCycleQueue;
    Consumer expectedReceiver;
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
