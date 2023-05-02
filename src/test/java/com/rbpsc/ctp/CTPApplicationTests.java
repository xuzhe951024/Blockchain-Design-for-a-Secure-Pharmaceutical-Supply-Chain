package com.rbpsc.ctp;

import com.rbpsc.ctp.api.entities.WorkLoadRecord;
import com.rbpsc.ctp.api.entities.WorkLoadReq;
import com.rbpsc.ctp.api.entities.factory.WorkLoadRecordFactory;
import com.rbpsc.ctp.biz.service.WorkLoadService;
import com.rbpsc.ctp.common.Constant.WLConstants;
import com.rbpsc.ctp.repository.service.WorkLoadRecordRepository;
import org.aspectj.apache.bcel.classfile.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        workLoadReq.setNormalOrPoisson(WLConstants.NORMAL_CASE);

        WorkLoadRecord workLoadRecord = WorkLoadRecordFactory.createWorkLoadRecord(workLoadReq);
        workLoadRecordRepository.insert(workLoadRecord);

        WorkLoadRecord workLoadRecordSelected = workLoadRecordRepository.selectById(workLoadRecord.getId());
        assert workLoadRecordSelected.getId().equals(workLoadRecord.getId());

        workLoadRecordSelected.addResponseTime(1);
        assert workLoadRecordRepository.update(workLoadRecordSelected.getId(), "averageResponseTime", "1");
        WorkLoadRecord workLoadRecordUpdated = workLoadRecordRepository.selectById(workLoadRecord.getId());
        assert workLoadRecordUpdated.getAverageResponseTime() == 1;

        workLoadRecordRepository.delete(workLoadRecordUpdated);
        WorkLoadRecord workLoadRecordDeleted = workLoadRecordRepository.selectById(workLoadRecord.getId());
        assert workLoadRecordDeleted == null;

    }

}
