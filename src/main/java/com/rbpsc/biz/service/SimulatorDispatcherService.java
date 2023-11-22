package com.rbpsc.biz.service;

import org.rbpsc.api.entities.dto.webview.SimulationDataView;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/16/23
 **/
public interface SimulatorDispatcherService {
    void startRequesting(SimulationDataView simulationDataView, String wsUUID, int maxReqThreadNum) throws InterruptedException;
}
