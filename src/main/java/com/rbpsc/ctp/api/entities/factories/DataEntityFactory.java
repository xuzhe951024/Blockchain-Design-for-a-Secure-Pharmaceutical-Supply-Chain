package com.rbpsc.ctp.api.entities.factories;

import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleView;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackModelBase;
import com.rbpsc.ctp.api.entities.base.BaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;
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

    public static void setAttackModelBase(AttackModelBase attackModelBaseSource, AttackModelBase attackModelBaseTarget){
        attackModelBaseTarget.setTargetDrugId(attackModelBaseSource.getTargetDrugId());
    }

    public static AttackAvailability createAttackAvailability(AttackModelBase attackModelBase) {
        AttackAvailability attackAvailability = new AttackAvailability();
        setId(attackAvailability);
        setAttackModelBase(attackModelBase, attackAvailability);
        return attackAvailability;
    }

    public static AttackConfidentiality createAttackConfidentiality(AttackModelBase attackModelBase) {
        AttackConfidentiality attackConfidentiality = new AttackConfidentiality();
        setId(attackConfidentiality);
        setAttackModelBase(attackModelBase, attackConfidentiality);
        return attackConfidentiality;
    }

    public static AttackIntegrity createAttackIntegrity(AttackModelBase attackModelBase){
        AttackIntegrity attackIntegrity = new AttackIntegrity();
        setId(attackIntegrity);
        setAttackModelBase(attackModelBase, attackIntegrity);
        return attackIntegrity;
    }

    public static DrugInfo createDrugInfo(SupplyChainBaseEntity supplyChainBaseEntity, String drugName){
        DrugInfo drugInfo = new DrugInfo();
        setId(drugInfo);
        setSupplyChainBase(supplyChainBaseEntity, drugInfo);
        drugInfo.setDrugName(drugName);
        return drugInfo;
    }

    public static DrugLifeCycleView createDrugLifeCycleView(){
        DrugLifeCycleView drugLifeCycleView = new DrugLifeCycleView();
        setId(drugLifeCycleView);
        return drugLifeCycleView;
    }

    public static DrugLifeCycle createDrugLifeCycle(SupplyChainBaseEntity supplyChainBaseEntity, DrugInfo drugInfo){
        DrugLifeCycle drugLifeCycle = new DrugLifeCycle();
        setId(drugLifeCycle);
        setSupplyChainBase(supplyChainBaseEntity, drugLifeCycle);
        drugLifeCycle.setDrug(drugInfo);
        return drugLifeCycle;
    }

    public static DrugOrderStep createDrugOrderStep(SupplyChainBaseEntity supplyChainBaseEntity, Institution institution){
        DrugOrderStep drugOrderStep = new DrugOrderStep();
        setId(drugOrderStep);
        drugOrderStep.setAddress(institution.getAddress());
        drugOrderStep.setRoleName(institution.getRoleName());
        return drugOrderStep;
    }

    public static Consumer createConsumer(int dose, String address){
        Consumer consumer = new Consumer();
        setId(consumer);
        consumer.setDose(dose);
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
}
