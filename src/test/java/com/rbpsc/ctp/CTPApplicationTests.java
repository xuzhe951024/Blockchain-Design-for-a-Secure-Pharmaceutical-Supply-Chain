package com.rbpsc.ctp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rbpsc.ctp.api.entities.dto.OperationDTO;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleVO;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadReq;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.biz.service.WorkLoadService;
import com.rbpsc.ctp.common.Constant.ServiceConstants;
import com.rbpsc.ctp.common.utiles.TestDataGenerator;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import com.rbpsc.ctp.repository.service.WorkLoadRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.rbpsc.ctp.common.Constant.ServiceConstants.*;

@SpringBootTest
@Slf4j
class CTPApplicationTests {

    @Autowired
    private WorkLoadService workLoadService;
    
    @Autowired
    private WorkLoadRecordRepository workLoadRecordRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveToDB(){
        workLoadService.saveToDB("test");
    }
    
    @Test
    void testCURD(){
        WorkLoadReq workLoadReq = new WorkLoadReq();
        workLoadReq.setUrl("test");
        workLoadReq.setReqCount(1);
        workLoadReq.setLamda(1);
        workLoadReq.setSigma(1);
        workLoadReq.setMu(1);
        workLoadReq.setNormalOrPoisson(ServiceConstants.NORMAL_CASE);

        WorkLoadRecord workLoadRecord = DataEntityFactory.createWorkLoadRecord();
        workLoadRecordRepository.insertWorkLoadRecord(workLoadRecord);

        WorkLoadRecord workLoadRecordSelected = workLoadRecordRepository.selectWorkLoadRecordById(workLoadRecord);
        assert workLoadRecordSelected.getId().equals(workLoadRecord.getId());

        workLoadRecordSelected.addResponseTime(1);
        assert workLoadRecordRepository.updateWorkLoadRecord(workLoadRecordSelected);
        WorkLoadRecord workLoadRecordUpdated = workLoadRecordRepository.selectWorkLoadRecordById(workLoadRecord);
        assert workLoadRecordUpdated.getAverageResponseTime() == 1;

        workLoadRecordRepository.deleteWorkLoadRecord(workLoadRecordUpdated);
        WorkLoadRecord workLoadRecordDeleted = workLoadRecordRepository.selectWorkLoadRecordById(workLoadRecord);
        assert workLoadRecordDeleted == null;

    }

    @Test
    void basicProcessesTest() throws JsonProcessingException {

        List<DrugLifeCycle<OperationDTO>> drugLifeCycleList = TestDataGenerator.generateDrugLifeCycleWithAttack();

        List<DrugInfo> consumerReceivedList = new ArrayList<>();

        lifeCycleLoop:
        for (DrugLifeCycle drugLifeCycle :
                drugLifeCycleList) {
            while (drugLifeCycle.getOperationQueueSize() > 0){
                if (StringUtils.isEmpty(drugLifeCycle.getDrug().getDrugTagTagId())){
                    drugLifeCycle.setTagTagId(UUID.randomUUID().toString());
                }

                OperationDTO operationDTO = drugLifeCycle.pollOperationVOQ();

                if (!DrugOrderStep.class.getName().equals(operationDTO.getOperationType())){
                    if (AttackAvailability.class.getName().equals(operationDTO.getOperationType())){
                        break lifeCycleLoop;
                    }
                    break;
                }

                if (0 == drugLifeCycle.getOperationQueueSize()){
                    consumerReceivedList.add(drugLifeCycle.getDrug());
                }
            }
        }

        assert consumerReceivedList.size() == drugLifeCycleList.size() - 5 :
                "consumerReceiveListSize: " + consumerReceivedList.size() +
                "\tdrugLifeCycleListSize: " + drugLifeCycleList.size();
    }

    @Test
    void webClientAndApiToggleTest() throws InterruptedException {
        WebClientUtil webClientUtil = new WebClientUtil();

        Mono<DrugLifeCycleResponse> responseMono = webClientUtil.postWithParams(
                "http://127.0.0.1:8090/v1/drugLifeCycle/drugOrderStep/toggle",
                false,
                Boolean.class,
                DrugLifeCycleResponse.class);

        responseMono.subscribe(result -> {
            assert result.isSuccess();
            log.info("switch off success!");
        }, error -> {
            error.printStackTrace();
        });
        Thread.sleep(500);

        webClientUtil.getWithoutParams("http://127.0.0.1:8090/v1/drugLifeCycle/drugOrderStep/checkAvailable"
                        ,DrugLifeCycleResponse.class)
                .subscribe(resultCheckAvailable -> {
                    assert resultCheckAvailable.getResponseCode() == RESPONSE_CODE_FAIL_SERVICE_DISABLED;
                    log.info("service has been switched off!");
                }, error -> {
                    error.printStackTrace();
                });
        Thread.sleep(500);

        webClientUtil.postWithParams(
                "http://127.0.0.1:8090/v1/drugLifeCycle/drugOrderStep/toggle",
                true,
                Boolean.class,
                DrugLifeCycleResponse.class)
                .subscribe(result -> {
                    assert result.isSuccess();
                    log.info("switch on success!");
                }, error -> {
                    error.printStackTrace();
                });
        Thread.sleep(500);

        webClientUtil.getWithoutParams("http://127.0.0.1:8090/v1/drugLifeCycle/drugOrderStep/checkAvailable"
                        ,DrugLifeCycleResponse.class)
                .subscribe(resultCheckAvailable -> {
                    assert resultCheckAvailable.isSuccess();
                    log.info("service has been switched on!");
                }, error -> {
                    error.printStackTrace();
                });

        Thread.sleep(500);
    }

}
