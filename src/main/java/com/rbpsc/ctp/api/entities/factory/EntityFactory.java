package com.rbpsc.ctp.api.entities.factory;

import com.rbpsc.ctp.api.entities.supplychain.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.attack.AttackIntegrity;
import com.rbpsc.ctp.api.entities.supplychain.attack.AttackModelBase;
import com.rbpsc.ctp.api.entities.base.BaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
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
public class EntityFactory {

    private static void setId(BaseEntity<String> baseEntity) {
        baseEntity.setId(UUID.randomUUID().toString());
        baseEntity.setIdFieldName(BASE_ENTITY_ID_FIELD_NAME);
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
        setSupplyChainBase(attackModelBaseSource, attackModelBaseTarget);
        attackModelBaseTarget.setTargetDrugId(attackModelBaseSource.getTargetDrugId());
    }

    public static AttackAvailability createAttackAvailability(AttackModelBase attackModelBase, int expectedCompromisedNodes) {
        AttackAvailability attackAvailability = new AttackAvailability();
        setId(attackAvailability);
        setAttackModelBase(attackModelBase, attackAvailability);
        attackAvailability.setExpectedCompromisedNodes(expectedCompromisedNodes);
        attackAvailability.setAttackType(ATTACK_TYPE_AVAILABILITY);
        return attackAvailability;
    }

    public static AttackConfidentiality createAttackConfidentiality(AttackModelBase attackModelBase, String attackerAddress) {
        AttackConfidentiality attackConfidentiality = new AttackConfidentiality();
        setId(attackConfidentiality);
        setAttackModelBase(attackModelBase, attackConfidentiality);
        attackConfidentiality.setAttackerAddress(attackerAddress);
        attackConfidentiality.setAttackType(ATTACK_TYPE_CONFIDENTIALITY);
        return attackConfidentiality;
    }

    public static AttackIntegrity createAttackIntegrity(AttackModelBase attackModelBase){
        AttackIntegrity attackIntegrity = new AttackIntegrity();
        setId(attackIntegrity);
        setAttackModelBase(attackModelBase, attackIntegrity);
        attackIntegrity.setAttackType(ATTACK_TYPE_INTEGRITY);
        return attackIntegrity;
    }

    public static DrugInfo createDrugInfo(SupplyChainBaseEntity supplyChainBaseEntity, String drugTagTagId, String drugName){
        DrugInfo drugInfo = new DrugInfo();
        setId(drugInfo);
        setSupplyChainBase(supplyChainBaseEntity, drugInfo);
        drugInfo.setDrugTagTagId(drugTagTagId);
        drugInfo.setDrugName(drugName);
        return drugInfo;
    }
}
