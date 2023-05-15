package com.rbpsc.ctp;

import com.rbpsc.ctp.api.entities.dto.OperationVO;
import com.rbpsc.ctp.api.entities.dto.webview.DrugLifeCycleView;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import com.rbpsc.ctp.api.entities.supplychain.operations.DrugOrderStep;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;
import com.rbpsc.ctp.api.entities.work_request.WorkLoadReq;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.biz.service.WorkLoadService;
import com.rbpsc.ctp.common.Constant.ServiceConstants;
import com.rbpsc.ctp.common.utiles.TestDataGenerator;
import com.rbpsc.ctp.repository.service.WorkLoadRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
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
    void basicProcessesTest(){
        DrugLifeCycleView drugLifeCycleView = TestDataGenerator.generateDrugLifeCycleWithAttack();

        List<DrugLifeCycle> drugLifeCycleList = drugLifeCycleView.getDrugLifeCycleList();

        List<DrugInfo> consumerReceivedList = new ArrayList<>();

        lifeCycleLoop:
        for (DrugLifeCycle drugLifeCycle :
                drugLifeCycleList) {
            while (drugLifeCycle.getOperationQueueSize() > 0){
                if (StringUtils.isEmpty(drugLifeCycle.getDrug().getDrugTagTagId())){
                    drugLifeCycle.setTagTagId(UUID.randomUUID().toString());
                }

                OperationVO<RoleBase> operationVO = drugLifeCycle.pollOperationVOQ();

                if (!DrugOrderStep.class.getName().equals(operationVO.getOperationType())){
                    if (AttackAvailability.class.getName().equals(operationVO.getOperationType())){
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

}
