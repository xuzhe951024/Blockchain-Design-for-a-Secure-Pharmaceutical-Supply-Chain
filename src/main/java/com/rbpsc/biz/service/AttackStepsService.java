package main.java.com.rbpsc.biz.service;


import main.java.org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import main.java.org.rbpsc.api.entities.supplychain.operations.OperationBase;

public interface AttackStepsService {
    boolean attackAvailability(OperationBase attackAvailability);

    boolean attackConfidentiality(DrugInfo drug, OperationBase attackConfidentiality);

    boolean attackIntegrity(DrugInfo drug, OperationBase attackIntegrity);
}
