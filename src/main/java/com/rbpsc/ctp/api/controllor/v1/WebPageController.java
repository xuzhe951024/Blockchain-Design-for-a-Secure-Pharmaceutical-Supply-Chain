package com.rbpsc.ctp.api.controllor.v1;


import com.rbpsc.ctp.api.entities.configs.ExperimentConfig;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.dto.webview.SimulationDataView;
import com.rbpsc.ctp.api.entities.factories.dynamic.ModelEntityFactory;
import com.rbpsc.ctp.biz.service.SimulatorDispatcherService;
import com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.rbpsc.ctp.common.Constant.EntityConstants.OPERATION_TYPE_PACKAGE_NAME;

@V1RestController
@RequestMapping("/web")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class WebPageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimulatorDispatcherService simulatorDispatcherService;
    private final ModelEntityFactory modelEntityFactory;

    public WebPageController(SimpMessagingTemplate simpMessagingTemplate, SimulatorDispatcherService simulatorDispatcherService, ModelEntityFactory modelEntityFactory) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.simulatorDispatcherService = simulatorDispatcherService;
        this.modelEntityFactory = modelEntityFactory;
    }


    @GetMapping("/cards")
    public List<DrugLifeCycleVO> getCards() {


        ExperimentConfig experimentConfig = new ExperimentConfig();
        experimentConfig.setId(UUID.randomUUID().toString());
        experimentConfig.setExperimentName("T");
        experimentConfig.setExperimentDescription("Test experiment");
        experimentConfig.setDrugName("Covid-Vaccine");
        experimentConfig.setMaxThreadCount(10);
        experimentConfig.setManufacturerCount(2);
        experimentConfig.setDistributorsCount(2);
        experimentConfig.setConsumerCount(2);
        experimentConfig.setDoesForEachConsumer(1);


        return modelEntityFactory.createDrugLifeCycleVOList(experimentConfig);
    }


    @GetMapping("/operationTypes")
    public List<String> getOperationTypes() {

        return new ArrayList<String>(){{
            try (ScanResult scanResult = new ClassGraph().whitelistPackages(OPERATION_TYPE_PACKAGE_NAME).scan()) {
                scanResult.getAllClasses().forEach(classInfo -> add(classInfo.getName()));
            }
        }};
    }

    @PostMapping("/submit/{uuid}")
    public void processCards(@PathVariable("uuid") String uuid, @RequestBody List<DrugLifeCycleVO> drugLifeCycleVOList) {


        // Process data...
        SimulationDataView simulationDataView = modelEntityFactory.buildSimulationDataViewFromVOList(drugLifeCycleVOList);

        simulatorDispatcherService.startRequesting(simulationDataView, uuid);

//        for (int i = 0; i <= 5; i++) {
//            // Assume this is the processing progress
//            Thread.sleep(500);
//            log.info("destination: /topic/process-progress/" + uuid);
//            simpMessagingTemplate.convertAndSend("/topic/process-progress/" + uuid, "MSG:" + i);
//        }

    }

}
