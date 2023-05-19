package com.rbpsc.ctp.api.entities.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbpsc.ctp.api.entities.configs.ExperimentConfig;
import com.rbpsc.ctp.api.entities.dto.OperationVO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleView;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;

import java.util.*;

public class FunctionEntityFactory {
    public static DrugLifeCycleView createDrugLifeCycleView(ExperimentConfig experimentConfig, List<Consumer> consumerList, List<List<Institution>> institutionTree) throws JsonProcessingException {
        DrugLifeCycleView drugLifeCycleView = new DrugLifeCycleView();
        drugLifeCycleView.setBatchId(experimentConfig.getExperimentName());
        DataEntityFactory.setId(drugLifeCycleView);
        Random random = new Random();

        int numberOfDrugs = experimentConfig.getConsumerCount() * experimentConfig.getDoesForEachConsumer();
        String drugName = experimentConfig.getDrugName();
        List<DrugLifeCycle> drugLifeCycleList = new ArrayList<>();
        for (int i = 0; i < numberOfDrugs; i++) {
            DrugInfo drugInfo = DataEntityFactory.createDrugInfo(drugLifeCycleView, drugName);

            int levelNum = experimentConfig.getDistributorsForEachLevel().size();
            List<OperationVO> operationVOQueue = new ArrayList<>();
            for (int j = 0; j < levelNum; j++) {
                int randInt = random.nextInt(experimentConfig.getDistributorsForEachLevel().get(j));
                List<Institution> institutionList = institutionTree.get(j);

                DrugOrderStep drugOrderStep = DataEntityFactory.createDrugOrderStep(drugLifeCycleView, institutionList.get(randInt));

                OperationVO operationVO = new OperationVO();
                operationVO.setOperationType(drugOrderStep.getClass().getName());
                operationVO.setOperation(new ObjectMapper().writeValueAsString(drugOrderStep));

                operationVOQueue.add(operationVO);
            }

            DrugLifeCycle drugLifeCycle = DataEntityFactory.createDrugLifeCycle(drugLifeCycleView, drugInfo);
            drugLifeCycle.setOperationVOQueue(operationVOQueue);
            drugLifeCycle.setExpectedReceiver(consumerList.get(i/experimentConfig.getDoesForEachConsumer()));
            drugLifeCycleList.add(drugLifeCycle);
        }
        drugLifeCycleView.setDrugLifeCycleList(drugLifeCycleList);

        return drugLifeCycleView;
    }
}
