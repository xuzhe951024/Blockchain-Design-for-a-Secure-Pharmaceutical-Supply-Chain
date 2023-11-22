package com.rbpsc.api.controllor.v1;


import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.rbpsc.api.entities.configs.ExperimentConfig;
import com.rbpsc.common.factories.dynamic.ModelEntityFactory;
import org.rbpsc.api.entities.dto.webview.DrugLifeCycleVO;
import org.rbpsc.api.entities.dto.webview.SimulationDataView;
import com.rbpsc.biz.service.SimulatorDispatcherService;
import com.rbpsc.biz.service.WebPageService;
import com.rbpsc.configuration.v1prefix.V1RestController;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.rbpsc.common.constant.EntityConstants.OPERATION_TYPE_PACKAGE_NAME;
import static org.rbpsc.common.constant.ServiceConstants.WEB_SCOKET_TOPIC_PROGRESS;


@V1RestController
@RequestMapping("/web")
@CrossOrigin(origins = "*")
@Slf4j
public class WebPageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimulatorDispatcherService simulatorDispatcherService;
    private final ModelEntityFactory modelEntityFactory;
    private final WebPageService webPageService;
    private int maxReqThreadNum;

    public WebPageController(SimpMessagingTemplate simpMessagingTemplate, SimulatorDispatcherService simulatorDispatcherService, ModelEntityFactory modelEntityFactory, WebPageService webPageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.simulatorDispatcherService = simulatorDispatcherService;
        this.modelEntityFactory = modelEntityFactory;
        this.webPageService = webPageService;
    }


    @PostMapping("/cards")
    public List<DrugLifeCycleVO> getCards(@RequestBody ExperimentConfig experimentConfig) {

        this.maxReqThreadNum = experimentConfig.getMaxThreadCount();
        List<DrugLifeCycleVO> drugLifeCycleVOList = modelEntityFactory.createDrugLifeCycleVOList(experimentConfig);
        return drugLifeCycleVOList;

    }


    @GetMapping("/operationTypes")
    public List<String> getOperationTypes() {

        ArrayList<String> result = new ArrayList<String>(){{
            try (ScanResult scanResult = new ClassGraph().whitelistPackages(OPERATION_TYPE_PACKAGE_NAME).scan()) {
                scanResult.getAllClasses().forEach(classInfo -> add(classInfo.getName()));
            }
        }};
        return result;
    }

    @PostMapping("/submit/{uuid}")
    public void processCards(@PathVariable("uuid") String uuid, @RequestBody List<DrugLifeCycleVO> drugLifeCycleVOList) throws InterruptedException {

        Thread.sleep(500);
        log.info("Starting to process data & build docker containers:");

        simpMessagingTemplate.convertAndSend(WEB_SCOKET_TOPIC_PROGRESS + uuid, "MSG: Starting to process data & build docker containers:");

        // Process data...
        SimulationDataView simulationDataView = modelEntityFactory.buildSimulationDataViewFromVOList(drugLifeCycleVOList, uuid);

        log.info("Finished processing data & building docker containers:");
        simpMessagingTemplate.convertAndSend(WEB_SCOKET_TOPIC_PROGRESS + uuid, "MSG: Finished processing data & building docker containers:");

        simulatorDispatcherService.startRequesting(simulationDataView, uuid, maxReqThreadNum);

//        for (int i = 0; i <= 5; i++) {
//            // Assume this is the processing progress
//            Thread.sleep(500);
//            log.info("destination: /topic/process-progress/" + uuid);
//            simpMessagingTemplate.convertAndSend(WEB_SCOKET_TOPIC_PROGRESS + uuid, "MSG:" + i);
//        }

    }

    @GetMapping("/operationAdds")
    public List<String> getOperationAddsByBatchId(@RequestParam(value = "batchId") String batchId){
        if (!StringUtils.isEmpty(batchId)){
            return webPageService.getAddressListByBatchId(batchId);
        }

        return null;
    }

}
