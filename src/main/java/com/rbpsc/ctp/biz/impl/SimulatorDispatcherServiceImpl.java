package main.java.com.rbpsc.ctp.biz.impl;

import main.java.com.rbpsc.ctp.api.entities.dto.DrugOperationDTO;
import main.java.com.rbpsc.ctp.api.entities.dto.OperationDTO;
import main.java.com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import main.java.com.rbpsc.ctp.api.entities.dto.webview.SimulationDataView;
import main.java.com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.com.rbpsc.ctp.biz.service.SimulatorDispatcherService;
import main.java.com.rbpsc.ctp.common.utiles.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;

import static main.java.com.rbpsc.ctp.common.Constant.ServiceConstants.RESPONSE_CODE_FAIL_FIND_ADDRESS;
import static main.java.com.rbpsc.ctp.common.Constant.ServiceConstants.WEB_SCOKET_TOPIC_PROGRESS;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/16/23
 **/

@Service
@Slf4j
public class SimulatorDispatcherServiceImpl implements SimulatorDispatcherService {

    final TaskExecutor taskExecutor;
    final SimpMessagingTemplate simpMessagingTemplate;
    final WebClientUtil webClientUtil;


    public SimulatorDispatcherServiceImpl(@Qualifier("SimulatorExecutor") TaskExecutor taskExecutor, SimpMessagingTemplate simpMessagingTemplate, WebClientUtil webClientUtil) {
        this.taskExecutor = taskExecutor;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.webClientUtil = webClientUtil;
    }

    @Override
    public void startRequesting(SimulationDataView simulationDataView, String wsUUID) {
//        for (int i = 0; i < 10; i++) {
//            int finalI = i;
//            taskExecutor.execute(() -> {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                simpMessagingTemplate.convertAndSend(WEB_SCOKET_TOPIC_PROGRESS + wsUUID, "MSG:" + finalI);
//            });
//        }

        List<DrugLifeCycle<OperationDTO>> drugLifeCycleList = simulationDataView.getDrugLifeCycleList();
        drugLifeCycleList.forEach(drugLifeCycle -> {

            taskExecutor.execute(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                while (drugLifeCycle.getOperationQueueSize() > 0) {
                    OperationDTO operationDTO = drugLifeCycle.pollOperationVOQ();

                    DrugOperationDTO drugOperationDTO = new DrugOperationDTO();
                    DataEntityFactory.setId(drugOperationDTO);
                    drugOperationDTO.setDrug(drugLifeCycle.getDrug());
                    drugOperationDTO.setOperationDTO(operationDTO);
                    drugOperationDTO.setExpectedReceiver(drugLifeCycle.getExpectedReceiver().getId());

                    DrugLifeCycleResponse response = sendToNextStepBaseLine(drugOperationDTO);

                    if (!response.isSuccess()){
                        String wsMessage = "Fail to access: " +
                                operationDTO.getOperation().getDomain() +
                                "\nIt may be suffering from DDoS attack!";
                        simpMessagingTemplate.convertAndSend(WEB_SCOKET_TOPIC_PROGRESS + wsUUID, wsMessage);
                        log.info(wsMessage);
                        break;
                    }

                    String wsMessage = "Access to: " +
                            operationDTO.getOperation().getDomain() +
                            ", result: " +
                            response;

                    // Send to database-based system
                    simpMessagingTemplate.convertAndSend(WEB_SCOKET_TOPIC_PROGRESS + wsUUID, wsMessage);

                    //TODO send to blockchain-based system(s)
                }
            });

        });
    }

    private DrugLifeCycleResponse sendToNextStepBaseLine(DrugOperationDTO drugOperationDTO) {
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        OperationDTO operationDTO = drugOperationDTO.getOperationDTO();

        if (StringUtils.isEmpty(operationDTO.getOperation().getAddress())) {
            response.setResponseWithCode(RESPONSE_CODE_FAIL_FIND_ADDRESS);
            response.setDescribe("Address can not be empty!");
            log.error("Address can not be empty!");
            return response;
        }

        Mono<DrugLifeCycleResponse> responseMono = webClientUtil.postWithParams(operationDTO.getOperation().getAddress(), drugOperationDTO, DrugOperationDTO.class, DrugLifeCycleResponse.class);

//        responseMono.subscribe(result -> {
//            log.info(result.toString());
//        }, error -> {
//            throw new RuntimeException(error);
//        });

        return responseMono.block();
    }
}
