package com.r3.developers.samples.obligation.etities.supplychain.drug;

import com.r3.developers.samples.obligation.etities.base.SupplyChainBaseEntity;
import com.r3.developers.samples.obligation.etities.roles.Consumer;
import com.r3.developers.samples.obligation.utils.StringUtils;

import java.util.List;



public class DrugLifeCycle<T> extends SupplyChainBaseEntity {
    DrugInfo drug;

    int operationQueueSize;

    List<T> lifeCycleQueue;

    Consumer expectedReceiver;

    Boolean isAttacked = false;

    Boolean produced = false;

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
    public void setOperationQueueSize(int operationQueueSize){

    }

    public boolean isProduced(){
        return StringUtils.isEmpty(drug.getDrugTagTagId());
    }

    public DrugInfo getDrug() {
        return drug;
    }

    public void setDrug(DrugInfo drug) {
        this.drug = drug;
    }

    public List<T> getLifeCycleQueue() {
        return lifeCycleQueue;
    }

    public void setLifeCycleQueue(List<T> lifeCycleQueue) {
        this.lifeCycleQueue = lifeCycleQueue;
    }

    public Consumer getExpectedReceiver() {
        return expectedReceiver;
    }

    public void setExpectedReceiver(Consumer expectedReceiver) {
        this.expectedReceiver = expectedReceiver;
    }

    public Boolean getIsAttacked() {
        return isAttacked;
    }

    public void setIsAttacked(Boolean attacked) {
        isAttacked = attacked;
    }

    public Boolean getProduced() {
        return produced;
    }

    public void setProduced(Boolean produced) {
        this.produced = produced;
    }
}
