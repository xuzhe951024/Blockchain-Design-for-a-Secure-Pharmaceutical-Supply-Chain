package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.api.entities.dto.webview.SimulationDataView;
import com.rbpsc.ctp.biz.service.SimulatorDispatcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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
    public void startRequesting(SimulationDataView simulationDataView, int threadNum, String wsUUID) {
        for (int i = 0; i < threadNum; i++) {
            int finalI = i;
            taskExecutor.execute(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                simpMessagingTemplate.convertAndSend("/topic/process-progress/" + wsUUID, "MSG:" + finalI);
            });
        }
    }
}
