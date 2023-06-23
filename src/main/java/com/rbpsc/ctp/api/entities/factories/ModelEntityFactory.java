package com.rbpsc.ctp.api.entities.factories;

import com.rbpsc.ctp.api.entities.configs.ExperimentConfig;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.dto.webview.OperationVO;
import com.rbpsc.ctp.api.entities.dto.webview.SimulationDataView;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;

import java.util.*;

import static com.rbpsc.ctp.common.Constant.EntityConstants.DEFAULT_OPERATION_MSG;

public class ModelEntityFactory {
    public static List<DrugLifeCycleVO> createDrugLifeCycleVOList(ExperimentConfig experimentConfig) {
        List<Consumer> consumerList = createAndSaveConsumers(experimentConfig.getConsumerCount(), experimentConfig.getDoesForEachConsumer());
        List<Institution> manufactureList = createInstituteList(experimentConfig.getManufacturerCount());
        List<Institution> distributorList = createInstituteList(experimentConfig.getDistributorsCount());
        Random random = new Random();
        return new ArrayList<DrugLifeCycleVO>() {{
            assert consumerList != null;
            consumerList.forEach(consumer -> {
                DrugLifeCycleVO drugLifeCycleVO = DataEntityFactory.createDrugLifeCycleView();
                drugLifeCycleVO.setDrugName(experimentConfig.getDrugName());
                drugLifeCycleVO.setBatchId(experimentConfig.getExperimentName());
                drugLifeCycleVO.setDrugId(UUID.randomUUID().toString());
                drugLifeCycleVO.setPhysicalMarking(UUID.randomUUID().toString());
                drugLifeCycleVO.setTargetConsumer(consumer.toString());
                drugLifeCycleVO.setExpectedDose(consumer.getExpectedDose());

                assert manufactureList != null;
                OperationVO manufactureOperationVO = DataEntityFactory.createOperationVO(DrugOrderStep.class.getName(), manufactureList.get(random.nextInt(manufactureList.size() - 1)).getAddress());
                drugLifeCycleVO.addOperationVO(manufactureOperationVO);

                assert distributorList != null;
                HashSet<Integer> distributorPickIndexSet = new HashSet<>();
                int distributorStepNum = random.nextInt(distributorList.size());
                for (int i = 0; i < distributorStepNum; i++) {
                    int pickIndex = random.nextInt(distributorList.size());
                    if (distributorPickIndexSet.contains(pickIndex)){
                        i--;
                        continue;
                    }

                    distributorPickIndexSet.add(pickIndex);
                    OperationVO distributorOperationVO = DataEntityFactory.createOperationVO(DrugOrderStep.class.getName(), distributorList.get(pickIndex).getAddress());
                    drugLifeCycleVO.addOperationVO(distributorOperationVO);
                }

                OperationVO consumerOperationVO = DataEntityFactory.createOperationVO(DrugOrderStep.class.getName(), consumer.getAddress());
                drugLifeCycleVO.addOperationVO(consumerOperationVO);

                add(drugLifeCycleVO);
            });
        }};
    }

    private static List<Institution> createInstituteList(int institutionCount) {
        // TODO: fill this method
        return null;
    }

    private static List<Consumer> createAndSaveConsumers(int consumerCount, int doesForEachConsumer) {
        // TODO: fill this method
        return null;
    }

    public static List<DrugLifeCycle<OperationDTO>> buildDrugLifeCycleFromVOList(List<DrugLifeCycleVO> drugLifeCycleVOList, String batchId) {
        SimulationDataView simulationDataView = new SimulationDataView();
        simulationDataView.setId(UUID.randomUUID().toString());
        simulationDataView.setBatchId(batchId);

        return new ArrayList<DrugLifeCycle<OperationDTO>>() {{
            drugLifeCycleVOList.forEach(drugLifeCycleVO -> {
                Consumer consumer = DataEntityFactory.createConsumer(drugLifeCycleVO.getExpectedDose(), drugLifeCycleVO.getTargetConsumer());

                DrugInfo drugInfo = DataEntityFactory.createDrugInfo(simulationDataView, drugLifeCycleVO.getDrugName());
                drugInfo.setId(drugLifeCycleVO.getDrugId());

                List<OperationDTO> operationDTOList = new ArrayList<OperationDTO>() {
                    private void transferToDTO(OperationVO operationVO) {
                        OperationBase operationBase = new OperationBase();
                        operationBase.setOperationMSG(operationVO.getOperationMsg());
                        String address = operationVO.getOperatorAdd();
                        operationBase.setAddress(address);

                        String[] name = address.split("\\.");
                        operationBase.setRoleName(name[name.length - 1]);

                        OperationDTO operationDTOFromPage = DataEntityFactory.createOperationDTO(operationBase, operationVO.getOperationType());
                        add(operationDTOFromPage);
                    }

                    {
                        DrugOrderStep consumerStep = DataEntityFactory.createDrugOrderStep(consumer, DEFAULT_OPERATION_MSG);
                        OperationDTO operationDTO = DataEntityFactory.createOperationDTO(consumerStep, consumerStep.getClass().getName());
                        add(operationDTO);
                        drugLifeCycleVO.getOperationVOList().forEach(this::transferToDTO);
                    }
                };

                DrugLifeCycle<OperationDTO> drugLifeCycle = DataEntityFactory.createDrugLifeCycleOperationDTO(simulationDataView, drugInfo);
                drugLifeCycle.setExpectedReceiver(consumer);
                drugLifeCycle.setLifeCycleQueue(operationDTOList);
                drugLifeCycle.setDrug(drugInfo);

                add(drugLifeCycle);
            });
        }};
    }
}
