package com.rbpsc.ctp.common.utiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.configs.ExperimentConfig;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.dto.webview.OperationVO;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.factories.ModelEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackConfidentiality;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackIntegrity;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestDataGenerator {
    private static ObjectMapper objectMapper;

    public static List<DrugLifeCycle<OperationDTO>> generateDrugLifeCycleViewRandom()  {
        ExperimentConfig experimentConfig = new ExperimentConfig();
        experimentConfig.setExperimentName("TestPage");
        experimentConfig.setConsumerCount(10);
        experimentConfig.setDoesForEachConsumer(2);
        experimentConfig.setDrugName("Covid-Vaccine");
        //TODO: Fill here
//        experimentConfig.setDistributorsForEachLevel(new ArrayList<Integer>(){{
//            add(1);
//            add(2);
//            add(3);
//        }});
//
//        List<Consumer> consumerList = new ArrayList<Consumer>(){{
//            for (int i = 0; i < experimentConfig.getConsumerCount(); i++) {
//                Consumer consumer = DataEntityFactory.createConsumer(experimentConfig.getDoesForEachConsumer(), "127.0.0.1");
//                add(consumer);
//            }
//        }};
//
//        List<List<Institution>> institutionTree = new ArrayList<List<Institution>>(){{
//            for (int institudNum :
//                    experimentConfig.getDistributorsForEachLevel()) {
//                List<Institution> institutionList = new ArrayList<Institution>(){{
//                    for (int i = 0; i < institudNum; i++) {
//                        Institution institution = DataEntityFactory.createInstitution("127.0.0.1");
//                        add(institution);
//                    }
//                }};
//
//                add(institutionList);
//            }
//        }};
//
//        return ModelEntityFactory.createDrugLifeCycleView(experimentConfig, consumerList, institutionTree);
        return null;
    }

    public static List<DrugLifeCycle<OperationDTO>> addAttacks(List<DrugLifeCycle<OperationDTO>> drugLifeCycleList) throws JsonProcessingException {
        OperationBase operationBase = new OperationBase();
        operationBase.setAddress("127.0.0.1");

        AttackAvailability attackAvailability = DataEntityFactory.createAttackAvailability(operationBase);
        AttackConfidentiality attackConfidentiality = DataEntityFactory.createAttackConfidentiality(operationBase);
        AttackIntegrity attackIntegrity = DataEntityFactory.createAttackIntegrity(operationBase);

        OperationDTO operationVOC = new OperationDTO();
        operationVOC.setId(UUID.randomUUID().toString());
        operationVOC.setOperationType(AttackConfidentiality.class.getName());
        operationVOC.setOperation(attackConfidentiality);

        OperationDTO operationVOI = new OperationDTO();
        operationVOI.setId(UUID.randomUUID().toString());
        operationVOI.setOperationType(AttackIntegrity.class.getName());
        operationVOI.setOperation(attackIntegrity);

        OperationDTO operationVOA = new OperationDTO();
        operationVOA.setId(UUID.randomUUID().toString());
        operationVOA.setOperationType(AttackAvailability.class.getName());
        operationVOA.setOperation(attackAvailability);

        drugLifeCycleList.get(0).addOperation(operationVOC);
        drugLifeCycleList.get(1).addOperation(operationVOI);
        drugLifeCycleList.get(drugLifeCycleList.size() - 3).addOperation(operationVOA);

        return drugLifeCycleList;
    }

    public static List<DrugLifeCycle<OperationDTO>> generateDrugLifeCycleWithAttack() throws JsonProcessingException {
        return  TestDataGenerator.addAttacks(TestDataGenerator.generateDrugLifeCycleViewRandom());
    }

    public static List<DrugLifeCycleVO> generateDrugLifeCycleVO(List<DrugLifeCycle<OperationDTO>> drugLifeCycleList) {
        List<DrugLifeCycleVO> drugLifeCycleVOList = new ArrayList<DrugLifeCycleVO>(){{
            drugLifeCycleList.forEach(drugLifeCycle -> {
                DrugLifeCycleVO drugLifeCycleVO = new DrugLifeCycleVO();
                List<OperationVO> operationVOList = new ArrayList<OperationVO>(){{
                    drugLifeCycle.getLifeCycleQueue().forEach(operationDTO -> {
                        OperationVO operationVO = new OperationVO();
                        operationVO.setId(operationDTO.getId());
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
