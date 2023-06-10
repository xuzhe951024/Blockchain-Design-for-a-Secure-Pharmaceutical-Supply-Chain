package com.rbpsc.ctp.api.entities.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.configs.ExperimentConfig;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;

import java.util.*;

public class FunctionEntityFactory {
    public static List<DrugLifeCycle> createDrugLifeCycleView(ExperimentConfig experimentConfig, List<Consumer> consumerList, List<List<Institution>> institutionTree) throws JsonProcessingException {
        DrugLifeCycleVO drugLifeCycleVO = new DrugLifeCycleVO();
        drugLifeCycleVO.setBatchId(experimentConfig.getExperimentName());
        DataEntityFactory.setId(drugLifeCycleVO);
        Random random = new Random();

        int numberOfDrugs = experimentConfig.getConsumerCount() * experimentConfig.getDoesForEachConsumer();
        String drugName = experimentConfig.getDrugName();
        List<DrugLifeCycle> drugLifeCycleList = new ArrayList<>();
        for (int i = 0; i < numberOfDrugs; i++) {
            DrugInfo drugInfo = DataEntityFactory.createDrugInfo(drugLifeCycleVO, drugName);

            int levelNum = experimentConfig.getDistributorsForEachLevel().size();
            List<OperationDTO> operationDTOQueue = new ArrayList<>();
            for (int j = 0; j < levelNum; j++) {
                int randInt = random.nextInt(experimentConfig.getDistributorsForEachLevel().get(j));
                List<Institution> institutionList = institutionTree.get(j);

                DrugOrderStep drugOrderStep = DataEntityFactory.createDrugOrderStep(drugLifeCycleVO, institutionList.get(randInt));

                OperationDTO operationDTO = new OperationDTO();
                operationDTO.setOperationType(drugOrderStep.getClass().getName());
                operationDTO.setOperation(new ObjectMapper().writeValueAsString(drugOrderStep));

                operationDTOQueue.add(operationDTO);
            }

            DrugLifeCycle drugLifeCycle = DataEntityFactory.createDrugLifeCycle(drugLifeCycleVO, drugInfo);
            drugLifeCycle.setOperationDTOQueue(operationDTOQueue);
            drugLifeCycle.setExpectedReceiver(consumerList.get(i/experimentConfig.getDoesForEachConsumer()));
            drugLifeCycleList.add(drugLifeCycle);
        }

        return drugLifeCycleList;
    }
}
