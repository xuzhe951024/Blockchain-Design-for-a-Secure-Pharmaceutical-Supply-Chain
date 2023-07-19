package main.java.com.rbpsc.biz.service;


import main.java.org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import main.java.org.rbpsc.api.entities.supplychain.operations.OperationBase;

public interface SupplyChainStepsService {
    public boolean manufacture(DrugInfo drug, OperationBase operationBase);

    public boolean distributor(DrugInfo drug, OperationBase operationBase);

    public boolean consumer(DrugInfo drug, OperationBase operationBase, String targetConsumerId);
}
