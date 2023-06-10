package com.rbpsc.ctp.common.utiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.configs.ExperimentConfig;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.dto.webview.OperationVO;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.factories.FunctionEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackModelBase;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {
    private static ObjectMapper objectMapper;

    public static List<DrugLifeCycle> generateDrugLifeCycleViewRandom() throws JsonProcessingException {
        ExperimentConfig experimentConfig = new ExperimentConfig();
        experimentConfig.setExperimentName("TestPage");
        experimentConfig.setConsumerCount(10);
        experimentConfig.setDoesForEachConsumer(2);
        experimentConfig.setDrugName("Covid-Vaccine");
        experimentConfig.setDistributorsForEachLevel(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
        }});

        List<Consumer> consumerList = new ArrayList<Consumer>(){{
            for (int i = 0; i < experimentConfig.getConsumerCount(); i++) {
                Consumer consumer = DataEntityFactory.createConsumer(experimentConfig.getDoesForEachConsumer(), "127.0.0.1");
                add(consumer);
            }
        }};

        List<List<Institution>> institutionTree = new ArrayList<List<Institution>>(){{
            for (int institudNum :
                    experimentConfig.getDistributorsForEachLevel()) {
                List<Institution> institutionList = new ArrayList<Institution>(){{
                    for (int i = 0; i < institudNum; i++) {
                        Institution institution = DataEntityFactory.createInstitution("127.0.0.1");
                        add(institution);
                    }
                }};

                add(institutionList);
            }
        }};

        return FunctionEntityFactory.createDrugLifeCycleView(experimentConfig, consumerList, institutionTree);
    }

    public static List<DrugLifeCycle> addAttacks(List<DrugLifeCycle> drugLifeCycleList) throws JsonProcessingException {
        AttackModelBase attackModelBase = new AttackModelBase();
        attackModelBase.setAddress("127.0.0.1");
        attackModelBase.setTargetBatchId(drugLifeCycleList.get(0).getBatchId());

        AttackAvailability attackAvailability = DataEntityFactory.createAttackAvailability(attackModelBase);
        AttackConfidentiality attackConfidentiality = DataEntityFactory.createAttackConfidentiality(attackModelBase);
        AttackIntegrity attackIntegrity = DataEntityFactory.createAttackIntegrity(attackModelBase);

        OperationDTO operationVOC = new OperationDTO();
        operationVOC.setOperationType(AttackConfidentiality.class.getName());
        operationVOC.setOperation(objectMapper.writeValueAsString(attackConfidentiality));

        OperationDTO operationVOI = new OperationDTO();
        operationVOI.setOperationType(AttackIntegrity.class.getName());
        operationVOI.setOperation(objectMapper.writeValueAsString(attackIntegrity));

        OperationDTO operationVOA = new OperationDTO();
        operationVOA.setOperationType(AttackAvailability.class.getName());
        operationVOA.setOperation(objectMapper.writeValueAsString(attackAvailability));

        drugLifeCycleList.get(0).addOperation(operationVOC);
        drugLifeCycleList.get(1).addOperation(operationVOI);
        drugLifeCycleList.get(drugLifeCycleList.size() - 3).addOperation(operationVOA);

        return drugLifeCycleList;
    }

    public static List<DrugLifeCycle> generateDrugLifeCycleWithAttack() throws JsonProcessingException {
        return  TestDataGenerator.addAttacks(TestDataGenerator.generateDrugLifeCycleViewRandom());
    }

    public static List<DrugLifeCycleVO> generateDrugLifeCycleVO(List<DrugLifeCycle> drugLifeCycleList) {
        List<DrugLifeCycleVO> drugLifeCycleVOList = new ArrayList<DrugLifeCycleVO>(){{
            drugLifeCycleList.forEach(drugLifeCycle -> {
                DrugLifeCycleVO drugLifeCycleVO = new DrugLifeCycleVO();
                List<OperationVO> operationVOList = new ArrayList<OperationVO>(){{
                    drugLifeCycle.getOperationDTOQueue().forEach(operationDTO -> {
                        OperationVO operationVO = new OperationVO();
                        operationVO.setOperationMsg("Default MSG");
                        operationVO.setOperatorAdd("127.0.0.1");
                        operationVO.setOperationType(operationDTO.getOperationType());
                        add(operationVO);
                    });
                }};
                drugLifeCycleVO.createSelf(drugLifeCycle, operationVOList);

                add(drugLifeCycleVO);
            });
        }};
        return drugLifeCycleVOList;
    }
}
