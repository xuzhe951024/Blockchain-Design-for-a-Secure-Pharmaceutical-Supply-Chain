package main.java.com.rbpsc.ctp.biz.service;

import main.java.com.rbpsc.ctp.api.entities.dto.webview.SimulationDataView;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/16/23
 **/
public interface SimulatorDispatcherService {
    public void startRequesting(SimulationDataView simulationDataView, String wsUUID);
}
