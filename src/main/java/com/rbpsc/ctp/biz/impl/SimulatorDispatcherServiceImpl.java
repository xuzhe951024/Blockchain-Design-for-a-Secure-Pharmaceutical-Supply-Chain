package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.dto.DrugOperationDTO;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.dto.webview.SimulationDataView;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.biz.service.SimulatorDispatcherService;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.rbpsc.ctp.common.Constant.ServiceConstants.WEB_SCOKET_TOPIC_PROGRESS;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/16/23
 **/

@Service
@Slf4j
public class SimulatorDispatcherServiceImpl implements SimulatorDispatcherService {

    private final TaskExecutor taskExecutor;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SimulatorDispatcherServiceImpl(@Qualifier("SimulatorExecutor") TaskExecutor taskExecutor, SimpMessagingTemplate simpMessagingTemplate) {
        this.taskExecutor = taskExecutor;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void startRequesting(SimulationDataView simulationDataView, String wsUUID) {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            taskExecutor.execute(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                simpMessagingTemplate.convertAndSend(WEB_SCOKET_TOPIC_PROGRESS + wsUUID, "MSG:" + finalI);
            });
        }

        List<DrugLifeCycle<OperationDTO>> drugLifeCycleList = simulationDataView.getDrugLifeCycleList();
        drugLifeCycleList.forEach(drugLifeCycle -> {
            while (drugLifeCycle.getOperationQueueSize() > 0){
                OperationDTO operationDTO = drugLifeCycle.pollOperationVOQ();

                DrugOperationDTO drugOperationDTO = new DrugOperationDTO();
                DataEntityFactory.setId(drugOperationDTO);
                drugOperationDTO.setDrug(drugLifeCycle.getDrug());
                drugOperationDTO.setOperationDTO(operationDTO);

                // Send to database-based system
                sendToNextStepBaseLine(drugOperationDTO);
                //TODO send to blockchain-based system(s)
            }
        });
    }

    private boolean sendToNextStepBaseLine(DrugOperationDTO drugOperationDTO)  {
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        OperationDTO operationDTO = drugOperationDTO.getOperationDTO();

        if (StringUtils.isEmpty(operationDTO.getOperation().getAddress())){
            log.error("Address can not be empty!");
            return false;
        }

        WebClientUtil webClientUtil = new WebClientUtil();

        Mono<DrugLifeCycleResponse> responseMono = webClientUtil.postWithParams(operationDTO.getOperation().getAddress(), drugOperationDTO, DrugOperationDTO.class, DrugLifeCycleResponse.class);

        responseMono.subscribe(result -> {
            log.info(result.toString());
        }, error -> {
            throw new RuntimeException(error);
        });

        return true;
    }
}
