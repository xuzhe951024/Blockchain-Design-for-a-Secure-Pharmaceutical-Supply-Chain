package com.rbpsc.ctp.api.controllor.v1;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.factories.ModelEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.common.utiles.TestDataGenerator;
import com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.rbpsc.ctp.common.Constant.EntityConstants.OPERATION_TYPE_PACKAGE_NAME;

@V1RestController
@RequestMapping("/web")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class WebPageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping("/cards")
    public List<DrugLifeCycleVO> getCards() throws JsonProcessingException {


        List<DrugLifeCycle> drugLifeCycleList = TestDataGenerator.generateDrugLifeCycleViewRandom();

        return TestDataGenerator.generateDrugLifeCycleVO(drugLifeCycleList);
    }


    @GetMapping("/operationTypes")
    public List<String> getOperationTypes() throws JsonProcessingException {
        List<String> typeList = new ArrayList<String>(){{
            try (ScanResult scanResult = new ClassGraph().whitelistPackages(OPERATION_TYPE_PACKAGE_NAME).scan()) {
                scanResult.getAllClasses().forEach(classInfo -> add(classInfo.getName()));
            }
        }};

        return typeList;
    }

    @PostMapping("/submit/{uuid}")
    public void processCards(@PathVariable("uuid") String uuid, @RequestBody List<DrugLifeCycleVO> drugLifeCycleVOList) throws InterruptedException {


        // Process data...
        List<DrugLifeCycle> drugLifeCycleList = ModelEntityFactory.buildDrugLifeCycleFromVOList(drugLifeCycleVOList, drugLifeCycleVOList.get(0).getBatchId());

        for (int i = 0; i <= 5; i++) {
            // Assume this is the processing progress
            Thread.sleep(500);
            log.info("destination: /topic/process-progress/" + uuid);
            simpMessagingTemplate.convertAndSend("/topic/process-progress/" + uuid, "MSG:" + i);
        }

    }

}
