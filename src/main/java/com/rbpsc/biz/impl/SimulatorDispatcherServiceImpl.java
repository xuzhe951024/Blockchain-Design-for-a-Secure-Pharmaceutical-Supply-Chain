package com.rbpsc.biz.impl;

import com.rbpsc.biz.service.BlockChainRequestsHandler;
import com.rbpsc.biz.service.SimulatorDispatcherService;
import com.rbpsc.common.factories.DataEntityFactory;
import com.rbpsc.common.utiles.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.rbpsc.api.entities.dto.DrugOperationDTO;
import org.rbpsc.api.entities.dto.OperationDTO;
import org.rbpsc.api.entities.dto.response.DrugLifeCycleResponse;
import org.rbpsc.api.entities.dto.webview.SimulationDataView;
import org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import org.rbpsc.common.constant.ServiceConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    final List<BlockChainRequestsHandler> blockChainRequestsHandlers;

    public SimulatorDispatcherServiceImpl(@Qualifier("SimulatorExecutor") TaskExecutor taskExecutor, SimpMessagingTemplate simpMessagingTemplate, WebClientUtil webClientUtil, List<BlockChainRequestsHandler> blockChainRequestsHandlers) {
        this.taskExecutor = taskExecutor;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.webClientUtil = webClientUtil;
        this.blockChainRequestsHandlers = blockChainRequestsHandlers;
    }

    @Override
    public void startRequesting(SimulationDataView simulationDataView, String wsUUID, int maxReqThreadNum) {
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

        ExecutorService executorService = Executors.newFixedThreadPool(maxReqThreadNum);

        List<Future<?>> futures = new ArrayList<>();
        List<Long> taskTimes = new ArrayList<>();

        drugLifeCycleList.forEach(drugLifeCycle -> {

            Future<?> future = executorService.submit(() -> {
                try {
                    Random random = new Random();
                    long sleepTime = 500L + (long)(random.nextDouble() * 1501L);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                long start = System.currentTimeMillis();
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
                        simpMessagingTemplate.convertAndSend(ServiceConstants.WEB_SCOKET_TOPIC_PROGRESS + wsUUID, wsMessage);
                        log.info(wsMessage);
                    }

                    String wsMessage = "Access to: " +
                            operationDTO.getOperation().getDomain() +
                            ", result: " +
                            response;

                    // Send to database-based system
                    simpMessagingTemplate.convertAndSend(ServiceConstants.WEB_SCOKET_TOPIC_PROGRESS + wsUUID, wsMessage);

                    //TODO send to blockchain-based system(s)

                    start = System.currentTimeMillis();
                    for (BlockChainRequestsHandler handler :
                            blockChainRequestsHandlers) {
                        handler.sendToNextStep(drugOperationDTO);
                    }

                }
                long end = System.currentTimeMillis();
                return end - start;
            });

            futures.add(future);

            if (futures.size() == maxReqThreadNum) {
                for (Future<?> f : futures) {
                    try {
                        taskTimes.add((Long) f.get());

                        long total = taskTimes.stream().mapToLong(Long::longValue).sum();
                        double average = total / (double) taskTimes.size();

                        String wsMessage = "Average Task Time: " + average + " ms";
                        // Send to database-based system
                        simpMessagingTemplate.convertAndSend(ServiceConstants.WEB_SCOKET_TOPIC_PROGRESS + wsUUID, wsMessage);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
                futures.clear();
            }

        });


        for (Future<?> future : futures) {
            try {
                taskTimes.add((Long) future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        long total = taskTimes.stream().mapToLong(Long::longValue).sum();
        double average = total / (double) taskTimes.size();

        String wsMessage = "Average Task Time: " + average + " ms";
        // Send to database-based system
        simpMessagingTemplate.convertAndSend(ServiceConstants.WEB_SCOKET_TOPIC_PROGRESS + wsUUID, wsMessage);

        executorService.shutdown();

    }

    private DrugLifeCycleResponse sendToNextStepBaseLine(DrugOperationDTO drugOperationDTO) {
        DrugLifeCycleResponse response = new DrugLifeCycleResponse();

        OperationDTO operationDTO = drugOperationDTO.getOperationDTO();

        if (StringUtils.isEmpty(operationDTO.getOperation().getAddress())) {
            response.setResponseWithCode(ServiceConstants.RESPONSE_CODE_FAIL_FIND_ADDRESS);
            response.setDescribe("Address can not be empty!");
            log.error("Address can not be empty!");
            return response;
        }

        Mono<DrugLifeCycleResponse> responseMono = webClientUtil.postWithParams(operationDTO.getOperation().getAddress().replaceFirst("(?<=http://)[^/]*", "localhost:8091"),
                drugOperationDTO, DrugOperationDTO.class, DrugLifeCycleResponse.class);

//        responseMono.subscribe(result -> {
//            log.info(result.toString());
//        }, error -> {
//            throw new RuntimeException(error);
//        });

        return responseMono.block();
    }
}
