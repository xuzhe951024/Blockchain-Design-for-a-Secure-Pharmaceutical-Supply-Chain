package main.java.com.rbpsc.ctp.api.entities.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.rbpsc.ctp.api.entities.base.BaseEntity;
import main.java.com.rbpsc.ctp.api.entities.dto.OperationDTO;
import main.java.com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import main.java.com.rbpsc.ctp.api.entities.dto.webview.OperationVO;
import main.java.com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.Receipt;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.Institution;
import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import main.java.com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static main.java.com.rbpsc.ctp.common.Constant.EntityConstants.*;

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
        List<OperationVO> operationVOList = new ArrayList<>();
        drugLifeCycleVO.setOperationVOList(operationVOList);
        return drugLifeCycleVO;
    }

    public static DrugLifeCycle<OperationDTO> createDrugLifeCycleOperationDTO(SupplyChainBaseEntity supplyChainBaseEntity, DrugInfo drugInfo){
        DrugLifeCycle<OperationDTO> drugLifeCycle = new DrugLifeCycle<>();
        setId(drugLifeCycle);
        setSupplyChainBase(supplyChainBaseEntity, drugLifeCycle);
        drugLifeCycle.setDrug(drugInfo);
        ArrayList<OperationDTO> operationQueue = new ArrayList<>();
        drugLifeCycle.setLifeCycleQueue(operationQueue);
        return drugLifeCycle;
    }

    public static DrugLifeCycle<Receipt> createDrugLifeCycleReceipt(DrugInfo drugInfo){
        DrugLifeCycle<Receipt> drugLifeCycle = new DrugLifeCycle<>();
        drugLifeCycle.setId(drugInfo.getId());
        setSupplyChainBase(drugInfo, drugLifeCycle);
        drugLifeCycle.setDrug(drugInfo);
        ArrayList<Receipt> receiptArrayQueue = new ArrayList<>();
        drugLifeCycle.setLifeCycleQueue(receiptArrayQueue);
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

    public static Consumer createConsumer(int dose, String address, String batchId, Optional<String> consumerId){
        Consumer consumer = new Consumer();
        consumer.setId(consumerId.orElse(UUID.randomUUID().toString()));
        consumer.setExpectedDose(dose);
        consumer.setAddress(address);
        consumer.setRoleName(ROLE_NAME_CONSUMER);
        consumer.setBatchId(batchId);
        return consumer;
    }

    public static Institution createInstitution(String address, String roleName, String batchId){
        Institution institution = new Institution();
        setId(institution);
        institution.setAddress(address);
        institution.setRoleName(roleName);
        institution.setBatchId(batchId);
        return institution;
    }

    public static OperationDTO createOperationDTO(OperationBase operation, String type) {
        OperationDTO operationDTO = new OperationDTO();
        setId(operationDTO);
        operationDTO.setOperationType(type);
        operationDTO.setOperation(operation);
        return operationDTO;
    }

    public static OperationVO createOperationVO(String operationType, String operationAdd){
        OperationVO operationVO = new OperationVO();
        setId(operationVO);
        operationVO.setOperationMsg(DEFAULT_OPERATION_MSG);
        operationVO.setOperatorAdd(operationAdd);
        operationVO.setOperationType(operationType);
        return operationVO;
    }

    public static Receipt createReceipt(OperationBase operation){
        Receipt receipt = new Receipt();
        setId(receipt);

        receipt.setAddress(operation.getAddress());
        receipt.setRoleName(operation.getRoleName());
        receipt.setOperationMSG(operation.getOperationMSG());

        return receipt;
    }

}
