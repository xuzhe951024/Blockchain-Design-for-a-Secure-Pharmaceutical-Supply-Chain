package com.rbpsc.ctp.api.entities.factories.dynamic;

import com.rbpsc.ctp.api.entities.configs.ExperimentConfig;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.dto.webview.OperationVO;
import com.rbpsc.ctp.api.entities.dto.webview.SimulationDataView;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;
import com.rbpsc.ctp.repository.service.ConsumerReceiptRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.rbpsc.ctp.common.Constant.EntityConstants.*;
import static com.rbpsc.ctp.common.Constant.ServiceConstants.*;

@Service
public class ModelEntityFactory {
    private final ConsumerReceiptRepository consumerReceiptRepository;

    public ModelEntityFactory(ConsumerReceiptRepository consumerReceiptRepository) {
        this.consumerReceiptRepository = consumerReceiptRepository;
    }

    public List<DrugLifeCycleVO> createDrugLifeCycleVOList(ExperimentConfig experimentConfig) {
        List<Consumer> consumerList = createAndSaveConsumers(experimentConfig.getConsumerCount(), experimentConfig.getDoesForEachConsumer(), experimentConfig.getExperimentName());
        List<Institution> manufactureList = createInstituteList(experimentConfig.getManufacturerCount(), V1_SERVICE_NAME_MANUFACTURE_API, ROLE_NAME_MANUFACTURE, experimentConfig.getExperimentName());
        List<Institution> distributorList = createInstituteList(experimentConfig.getDistributorsCount(), V1_SERVICE_NAME_DISTRIBUTOR_API, ROLE_NAME_DISTRIBUTOR, experimentConfig.getExperimentName());
        Random random = new Random();
        return Collections.unmodifiableList(new ArrayList<DrugLifeCycleVO>() {{
            consumerList.forEach(consumer -> {
                for (int i = 0; i < consumer.getExpectedDose(); i++) {
                    DrugLifeCycleVO drugLifeCycleVO = DataEntityFactory.createDrugLifeCycleView();
                    drugLifeCycleVO.setDrugName(experimentConfig.getDrugName());
                    drugLifeCycleVO.setBatchId(experimentConfig.getExperimentName());
                    drugLifeCycleVO.setDrugId(UUID.randomUUID().toString());
                    drugLifeCycleVO.setPhysicalMarking(UUID.randomUUID().toString());
                    drugLifeCycleVO.setTargetConsumer(consumer.getAddress());
                    drugLifeCycleVO.setExpectedDose(consumer.getExpectedDose());

                    OperationVO manufactureOperationVO = DataEntityFactory.createOperationVO(DrugOrderStep.class.getName(), manufactureList.get(random.nextInt(manufactureList.size() - 1)).getAddress());
                    drugLifeCycleVO.addOperationVO(manufactureOperationVO);

                    HashSet<Integer> distributorPickIndexSet = new HashSet<>();
                    int distributorStepNum = random.nextInt(distributorList.size());
                    for (int j = 0; j < distributorStepNum; j++) {
                        int pickIndex = random.nextInt(distributorList.size());
                        if (distributorPickIndexSet.contains(pickIndex)) {
                            j--;
                            continue;
                        }

                        distributorPickIndexSet.add(pickIndex);
                        OperationVO distributorOperationVO = DataEntityFactory.createOperationVO(DrugOrderStep.class.getName(), distributorList.get(pickIndex).getAddress());
                        drugLifeCycleVO.addOperationVO(distributorOperationVO);
                    }

                    OperationVO consumerOperationVO = DataEntityFactory.createOperationVO(DrugOrderStep.class.getName(), consumer.getAddress());
                    drugLifeCycleVO.addOperationVO(consumerOperationVO);

                    add(drugLifeCycleVO);
                }
            });
        }});
    }

    private List<Institution> createInstituteList(int institutionCount, String serviceName, String roleName, String batchId) {

        return Collections.unmodifiableList(new ArrayList<Institution>() {{
            for (int i = 0; i < institutionCount; i++) {
                Institution institution = DataEntityFactory.createInstitution(roleName + i + DOT + batchId + serviceName
                        , roleName
                        , batchId);

                add(institution);
            }
        }});
    }

    private List<Consumer> createAndSaveConsumers(int consumerCount, int doesForEachConsumer, String batchId) {

        return Collections.unmodifiableList(new ArrayList<Consumer>() {{
            for (int i = 0; i < consumerCount; i++) {
                Consumer consumer = DataEntityFactory.createConsumer(doesForEachConsumer, ROLE_NAME_CONSUMER
                                + i + DOT + batchId + V1_SERVICE_NAME_CONSUMER_API
                        , batchId);
                consumerReceiptRepository.insertConsumerReceipt(consumer);
                add(consumer);
            }
        }});
    }

    public SimulationDataView buildSimulationDataViewFromVOList(List<DrugLifeCycleVO> drugLifeCycleVOList) {
        String batchId = drugLifeCycleVOList.get(0).getBatchId();
        SimulationDataView simulationDataView = new SimulationDataView();
        simulationDataView.setId(UUID.randomUUID().toString());
        simulationDataView.setBatchId(batchId);
        simulationDataView.detectEnvironment();
        simulationDataView.setDrugLifeCycleList(Collections.unmodifiableList(new ArrayList<DrugLifeCycle<OperationDTO>>() {{
            drugLifeCycleVOList.forEach(drugLifeCycleVO -> {
                //TODO: consumer id is not match
                Consumer consumer = DataEntityFactory.createConsumer(drugLifeCycleVO.getExpectedDose(), drugLifeCycleVO.getTargetConsumer(), batchId);

                DrugInfo drugInfo = DataEntityFactory.createDrugInfo(simulationDataView, drugLifeCycleVO.getDrugName());
                drugInfo.setId(drugLifeCycleVO.getDrugId());

                List<OperationDTO> operationDTOList = new ArrayList<OperationDTO>() {
                    private void transferToDTO(OperationVO operationVO) {
                        OperationBase operationBase = new OperationBase();
                        operationBase.setOperationMSG(operationVO.getOperationMsg());
                        String address = operationVO.getOperatorAdd();
                        operationBase.setAddress(address);

                        String[] name = address.split("\\.");
                        operationBase.setRoleName(name[0]);

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
        }}));

        return simulationDataView;
    }
}
