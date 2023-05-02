package com.rbpsc.ctp.api.entities.factory;

import com.rbpsc.ctp.api.entities.WorkLoadRecord;
import com.rbpsc.ctp.api.entities.WorkLoadReq;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 5/1/23
 **/
public class WorkLoadRecordFactory {
    public static WorkLoadRecord createWorkLoadRecord(WorkLoadReq workLoadReq) {
        WorkLoadRecord workLoadRecord = new WorkLoadRecord();
        workLoadRecord.setId(UUID.randomUUID().toString());
        workLoadRecord.setResponseTimeList(new ArrayList());
        workLoadRecord.setAverageResponseTime(0);
        return workLoadRecord;
    }
}
