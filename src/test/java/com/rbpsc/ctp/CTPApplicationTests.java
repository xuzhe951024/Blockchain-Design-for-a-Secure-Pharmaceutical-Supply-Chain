package com.rbpsc.ctp;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.supplychain.roles.Institution;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadReq;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.biz.service.WorkLoadService;
import com.rbpsc.ctp.common.Constant.ServiceConstants;
import com.rbpsc.ctp.common.utiles.DockerUtils;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import com.rbpsc.ctp.repository.service.RoleBaseRepository;
import com.rbpsc.ctp.repository.service.WorkLoadRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.rbpsc.ctp.common.Constant.EntityConstants.DASH;
import static com.rbpsc.ctp.common.Constant.EntityConstants.HTTP_URL_PRE_FIX;
import static com.rbpsc.ctp.common.Constant.ServiceConstants.*;

@SpringBootTest
@Slf4j
class CTPApplicationTests {

    @Autowired
    private WorkLoadService workLoadService;

    @Autowired
    private WorkLoadRecordRepository workLoadRecordRepository;

    @Autowired
    private DockerUtils dockerUtils;

    @Autowired
    private RoleBaseRepository roleBaseRepository;

    @Autowired
    private DockerClient dockerClient;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveToDB() {
//        workLoadService.saveToDB("test");
    }

    @Test
    void testCURD() {
        WorkLoadReq workLoadReq = new WorkLoadReq();
        workLoadReq.setUrl("test");
        workLoadReq.setReqCount(1);
        workLoadReq.setLamda(1);
        workLoadReq.setSigma(1);
        workLoadReq.setMu(1);
        workLoadReq.setNormalOrPoisson(ServiceConstants.NORMAL_CASE);

        WorkLoadRecord workLoadRecord = DataEntityFactory.createWorkLoadRecord();
        workLoadRecordRepository.insertWorkLoadRecord(workLoadRecord);

        WorkLoadRecord workLoadRecordSelected = workLoadRecordRepository.selectWorkLoadRecordById(workLoadRecord.getId());
        assert workLoadRecordSelected.getId().equals(workLoadRecord.getId());

        workLoadRecordSelected.addResponseTime(1);
        assert workLoadRecordRepository.updateWorkLoadRecord(workLoadRecordSelected);
        WorkLoadRecord workLoadRecordUpdated = workLoadRecordRepository.selectWorkLoadRecordById(workLoadRecord.getId());
        assert workLoadRecordUpdated.getAverageResponseTime() == 1;

        workLoadRecordRepository.deleteWorkLoadRecord(workLoadRecordUpdated);
        WorkLoadRecord workLoadRecordDeleted = workLoadRecordRepository.selectWorkLoadRecordById(workLoadRecord.getId());
        assert workLoadRecordDeleted == null;

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
        }, Throwable::printStackTrace);
        Thread.sleep(500);

        webClientUtil.getWithoutParams("http://127.0.0.1:8090/v1/drugLifeCycle/drugOrderStep/checkAvailable"
                        , DrugLifeCycleResponse.class)
                .subscribe(resultCheckAvailable -> {
                    assert resultCheckAvailable.getResponseCode() == RESPONSE_CODE_FAIL_SERVICE_DISABLED;
                    log.info("service has been switched off!");
                }, Throwable::printStackTrace);
        Thread.sleep(500);

        webClientUtil.postWithParams(
                        "http://127.0.0.1:8090/v1/drugLifeCycle/drugOrderStep/toggle",
                        true,
                        Boolean.class,
                        DrugLifeCycleResponse.class)
                .subscribe(result -> {
                    assert result.isSuccess();
                    log.info("switch on success!");
                }, Throwable::printStackTrace);
        Thread.sleep(500);

        webClientUtil.getWithoutParams("http://127.0.0.1:8090/v1/drugLifeCycle/drugOrderStep/checkAvailable"
                        , DrugLifeCycleResponse.class)
                .subscribe(resultCheckAvailable -> {
                    assert resultCheckAvailable.isSuccess();
                    log.info("service has been switched on!");
                }, Throwable::printStackTrace);

        Thread.sleep(500);
    }

    @Test
    void startDockerContainerTest() throws InterruptedException {
        int containerNumber = 2;
        String[] containers = new String[containerNumber];
        String[] images = new String[containerNumber];
        String networkId = dockerUtils.createNetWork("test-network");

        for (int i = 0; i < containerNumber; i++) {
            log.info("Starting to build and launch container: " + i);
            String imageId = dockerUtils.buildImage("testAuto/app/DockerFile_SupplyChainNode", "auto-img-t-" + i);
            String containerId = dockerUtils.createAndStartContainer("test-network", imageId,
                    "autocontainer-t-" + i,
                    Arrays.asList("KEY=VALUE" + i));

            containers[i] = containerId;
            images[i] = imageId;
        }

        dockerUtils.waitForContainerStarting(containers, DOCKER_LAUNCHED_LOG_SIGN, UUID.randomUUID().toString());

        for (int i = 0; i < containerNumber; i++) {
            log.info("Starting to stop and remove container: " + i);
            dockerUtils.stopAndDeleteContainer(containers[i]);
        }

        log.info("Sleep " + containerNumber * 2 + "seconds before containers fully stopped");
        for (int i = 0; i < containerNumber * 2; i++) {
            log.info("Counting down: " + (containerNumber * 2 - i));
            Thread.sleep(1000);
        }

        for (int i = 0; i < containerNumber; i++) {
            log.info("Starting to remove image: " + i);
            dockerUtils.deleteImage(images[i], true);
        }

        dockerUtils.deleteNetWork(networkId);
    }

    @Test
    void findFromMongoDBByExampleTest() {

        for (int i = 0; i < 5; i++) {

            Institution institution = DataEntityFactory.createInstitution(HTTP_URL_PRE_FIX + "testRole" + i + DASH + "testBatch" + "/test"
                    , "testRole"
                    , "testBatch");

            roleBaseRepository.insertRoleBase(institution);
        }

        RoleBase roleBaseKey = new RoleBase();
        roleBaseKey.setBatchId("testBatch");
        List<RoleBase> roleBaseList = roleBaseRepository.findByExample(roleBaseKey);

        assert roleBaseList.size() == 5;
        log.info(roleBaseList.toString());

        roleBaseList.forEach(roleBase -> {
            roleBaseRepository.deleteRoleBase(roleBase);
        });

        assert roleBaseRepository.findByExample(roleBaseKey).size() == 0;
    }

}
