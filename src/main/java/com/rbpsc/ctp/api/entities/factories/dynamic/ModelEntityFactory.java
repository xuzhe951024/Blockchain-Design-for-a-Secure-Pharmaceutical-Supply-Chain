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
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.common.utiles.DockerUtils;
import com.rbpsc.ctp.repository.service.ConsumerReceiptRepository;
import com.rbpsc.ctp.repository.service.RoleBaseRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.rbpsc.ctp.common.Constant.EntityConstants.*;
import static com.rbpsc.ctp.common.Constant.ServiceConstants.*;

@Service
public class ModelEntityFactory {
    final ConsumerReceiptRepository consumerReceiptRepository;
    final RoleBaseRepository roleBaseRepository;
    final DockerUtils dockerUtils;
    final SimpMessagingTemplate simpMessagingTemplate;

    public ModelEntityFactory(ConsumerReceiptRepository consumerReceiptRepository, RoleBaseRepository roleBaseRepository, DockerUtils dockerUtils, SimpMessagingTemplate simpMessagingTemplate) {
        this.consumerReceiptRepository = consumerReceiptRepository;
        this.roleBaseRepository = roleBaseRepository;
        this.dockerUtils = dockerUtils;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public List<DrugLifeCycleVO> createDrugLifeCycleVOList(ExperimentConfig experimentConfig) {
        cleanRoleBase();
        List<Consumer> consumerList = createAndSaveConsumers(experimentConfig.getConsumerCount(), experimentConfig.getDoesForEachConsumer(), experimentConfig.getExperimentName());
        List<Institution> manufactureList = createInstituteList(experimentConfig.getManufacturerCount(), V1_SERVICE_NAME_MANUFACTURE_API, ROLE_NAME_MANUFACTURE, experimentConfig.getExperimentName());
        List<Institution> distributorList = createInstituteList(experimentConfig.getDistributorsCount(), V1_SERVICE_NAME_DISTRIBUTOR_API, ROLE_NAME_DISTRIBUTOR, experimentConfig.getExperimentName());

        // Create & Save attackers
        createInstituteList(experimentConfig.getManufacturerCount(), V1_SERVICE_NAME_ATTACK_INTEGRITY_API, ROLE_NAME_MANUFACTURE, experimentConfig.getExperimentName());
        createInstituteList(experimentConfig.getDistributorsCount(), V1_SERVICE_NAME_ATTACK_INTEGRITY_API, ROLE_NAME_DISTRIBUTOR, experimentConfig.getExperimentName());

        createInstituteList(experimentConfig.getManufacturerCount(), V1_SERVICE_NAME_ATTACK_AVAILABILITY_API, ROLE_NAME_MANUFACTURE, experimentConfig.getExperimentName());
        createInstituteList(experimentConfig.getDistributorsCount(), V1_SERVICE_NAME_ATTACK_AVAILABILITY_API, ROLE_NAME_DISTRIBUTOR, experimentConfig.getExperimentName());

        createInstituteList(1, V1_SERVICE_NAME_ATTACK_CONFIDENTIALITY_API, ROLE_NAME_ATTACKER, experimentConfig.getExperimentName());


        Random random = new Random();
        return Collections.unmodifiableList(new ArrayList<DrugLifeCycleVO>() {{
            consumerList.forEach(consumer -> {
                for (int i = 0; i < consumer.getExpectedDose(); i++) {
                    DrugLifeCycleVO drugLifeCycleVO = DataEntityFactory.createDrugLifeCycleView();
                    drugLifeCycleVO.setDrugName(experimentConfig.getDrugName());
                    drugLifeCycleVO.setBatchId(experimentConfig.getExperimentName());
                    drugLifeCycleVO.setDrugId(UUID.randomUUID().toString());
                    drugLifeCycleVO.setPhysicalMarking(UUID.randomUUID().toString());
                    drugLifeCycleVO.setTargetConsumer(consumer.getId());
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

    private void cleanRoleBase() {
        List<RoleBase> roleBaseList = roleBaseRepository.findAll();
        if (roleBaseList.size() > 0){
            roleBaseList.forEach(roleBaseRepository::deleteRoleBase);
        }
    }

    private List<Institution> createInstituteList(int institutionCount, String serviceName, String roleName, String batchId) {

        return Collections.unmodifiableList(new ArrayList<Institution>() {{
            for (int i = 0; i < institutionCount; i++) {

                //TODO: Modify the method of address generation
                Institution institution = DataEntityFactory.createInstitution(HTTP_URL_PRE_FIX + roleName + i + DASH + batchId + serviceName
                        , roleName
                        , batchId);

                add(institution);

                roleBaseRepository.insertRoleBase(institution);
            }
        }});
    }

    private List<Consumer> createAndSaveConsumers(int consumerCount, int doesForEachConsumer, String batchId) {

        return Collections.unmodifiableList(new ArrayList<Consumer>() {{
            for (int i = 0; i < consumerCount; i++) {

                //TODO: Modify the method of address generation
                Consumer consumer = DataEntityFactory.createConsumer(doesForEachConsumer, HTTP_URL_PRE_FIX + ROLE_NAME_CONSUMER
                                + i + DASH + batchId + V1_SERVICE_NAME_CONSUMER_API
                        , batchId, Optional.empty());
                consumerReceiptRepository.insertConsumerReceipt(consumer);
                add(consumer);

                roleBaseRepository.insertRoleBase(consumer);
            }
        }});
    }

    public SimulationDataView buildSimulationDataViewFromVOList(List<DrugLifeCycleVO> drugLifeCycleVOList, String uuid) {
        String batchId = drugLifeCycleVOList.get(0).getBatchId();
        SimulationDataView simulationDataView = new SimulationDataView();
        simulationDataView.setId(UUID.randomUUID().toString());
        simulationDataView.setBatchId(batchId);
        simulationDataView.detectEnvironment();
        simulationDataView.setDrugLifeCycleList(Collections.unmodifiableList(new ArrayList<DrugLifeCycle<OperationDTO>>() {
            private void build(DrugLifeCycleVO drugLifeCycleVO) {
                Consumer consumer = consumerReceiptRepository.selectConsumerReceiptById(drugLifeCycleVO.getTargetConsumer());
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
                        drugLifeCycleVO.getOperationVOList().forEach(this::transferToDTO);
                    }
                };

                DrugLifeCycle<OperationDTO> drugLifeCycle = DataEntityFactory.createDrugLifeCycleOperationDTO(simulationDataView, drugInfo);
                drugLifeCycle.setExpectedReceiver(consumer);
                drugLifeCycle.setLifeCycleQueue(operationDTOList);
                drugLifeCycle.setDrug(drugInfo);

                add(drugLifeCycle);
            }

            {
            drugLifeCycleVOList.forEach(this::build);
        }}));

        buildDockerContainers(DOCKER_FILE_PATH, batchId, uuid);

        return simulationDataView;
    }

    private void buildDockerContainers(String dockerFilePath, String batchId, String uuid){

        RoleBase roleBaseKey = new RoleBase();
        roleBaseKey.setBatchId(batchId);
        List<RoleBase> roleBaseList = roleBaseRepository.findByExample(roleBaseKey);

        Set<String> domainSet = new HashSet<String>(){{
            roleBaseList.forEach(roleBase -> {
                add(roleBase.getDomain());
            });
        }};

        AtomicInteger i = new AtomicInteger();
        String[] containerIds = new String[domainSet.size()];
        domainSet.forEach(domain -> {
            containerIds[i.getAndIncrement()] = dockerUtils.createAndStartContainer(DOCKER_NETWORK_NAME, dockerUtils.buildImage(dockerFilePath, domain + DOCKER_IMAGE_SUFIX),
                    domain,
                    Collections.singletonList("ROLE=" + domain.split(DASH)[0]));
        });

        dockerUtils.waitForContainerStarting(containerIds, DOCKER_LAUNCHED_LOG_SIGN, uuid);

    }
}
