package main.java.com.rbpsc.biz.service;

import main.java.org.rbpsc.api.entities.dto.webview.SimulationDataView;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/16/23
 **/
public interface SimulatorDispatcherService {
    public void startRequesting(SimulationDataView simulationDataView, String wsUUID);
}
