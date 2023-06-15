package com.rbpsc.ctp.api.entities.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.base.BaseEntity;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;

import java.util.ArrayList;
import java.util.UUID;

import static com.rbpsc.ctp.common.Constant.EntityConstants.*;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 5/1/23
 **/
public class DataEntityFactory {

    private static ObjectMapper objectMapper;

    public static void setId(BaseEntity<String> baseEntity) {
        baseEntity.setId(UUID.randomUUID().toString());
    }

    public static WorkLoadRecord createWorkLoadRecord() {
        WorkLoadRecord workLoadRecord = new WorkLoadRecord();
        setId(workLoadRecord);
        workLoadRecord.setResponseTimeList(new ArrayList<>());
        workLoadRecord.setAverageResponseTime(0);
        return workLoadRecord;
    }

    public static void setSupplyChainBase(SupplyChainBaseEntity source, SupplyChainBaseEntity target){
        target.setBatchId(source.getBatchId());
    }

    public static void setAttackModelBase(OperationBase operationBaseSource, OperationBase operationBaseTarget){
        operationBaseTarget.setRoleName(ROLE_NAME_ATTACKER);
    }

    public static AttackAvailability createAttackAvailability(OperationBase operationBase) {
        AttackAvailability attackAvailability = new AttackAvailability();
        setId(attackAvailability);
        setAttackModelBase(operationBase, attackAvailability);
        return attackAvailability;
    }

    public static AttackConfidentiality createAttackConfidentiality(OperationBase operationBase) {
        AttackConfidentiality attackConfidentiality = new AttackConfidentiality();
        setId(attackConfidentiality);
        setAttackModelBase(operationBase, attackConfidentiality);
        return attackConfidentiality;
    }

    public static AttackIntegrity createAttackIntegrity(OperationBase operationBase){
        AttackIntegrity attackIntegrity = new AttackIntegrity();
        setId(attackIntegrity);
        setAttackModelBase(operationBase, attackIntegrity);
        return attackIntegrity;
    }

    public static DrugInfo createDrugInfo(SupplyChainBaseEntity supplyChainBaseEntity, String drugName){
        DrugInfo drugInfo = new DrugInfo();
        setId(drugInfo);
        setSupplyChainBase(supplyChainBaseEntity, drugInfo);
        drugInfo.setDrugName(drugName);
        return drugInfo;
    }

    public static DrugLifeCycleVO createDrugLifeCycleView(){
        DrugLifeCycleVO drugLifeCycleVO = new DrugLifeCycleVO();
        setId(drugLifeCycleVO);
        return drugLifeCycleVO;
    }

    public static DrugLifeCycle createDrugLifeCycle(SupplyChainBaseEntity supplyChainBaseEntity, DrugInfo drugInfo){
        DrugLifeCycle drugLifeCycle = new DrugLifeCycle();
        setId(drugLifeCycle);
        setSupplyChainBase(supplyChainBaseEntity, drugLifeCycle);
        drugLifeCycle.setDrug(drugInfo);
        ArrayList<OperationDTO> operationQueue = new ArrayList<>();
        ArrayList<Receipt> receiptArrayQueue = new ArrayList<>();
        drugLifeCycle.setOperationDTOQueue(operationQueue);
        drugLifeCycle.setReceiptQueue(receiptArrayQueue);
        return drugLifeCycle;
    }

    public static DrugOrderStep createDrugOrderStep(RoleBase role, String MSG){
        DrugOrderStep drugOrderStep = new DrugOrderStep();
        setId(drugOrderStep);
        setOperationBase(role, drugOrderStep, MSG);
        return drugOrderStep;
    }

    public static void setOperationBase(RoleBase source, OperationBase dest, String MSG){
        dest.setRoleName(source.getRoleName());
        dest.setAddress(source.getAddress());
        dest.setOperationMSG(MSG);
    }

    public static Consumer createConsumer(int dose, String address){
        Consumer consumer = new Consumer();
        setId(consumer);
        consumer.setExpectedDose(dose);
        consumer.setAddress(address);
        consumer.setRoleName(ROLE_NAME_CONSUMER);
        return consumer;
    }

    public static Institution createInstitution(String address){
        Institution institution = new Institution();
        setId(institution);
        institution.setAddress(address);
        institution.setRoleName(ROLE_NAME_INSTITUTION);
        return institution;
    }

    public static OperationDTO createOperationDTO(OperationBase operation, String type) {
        OperationDTO operationDTO = new OperationDTO();
        setId(operationDTO);
        operationDTO.setOperationType(type);
        operationDTO.setOperation(operation);
        return operationDTO;
    }

}
