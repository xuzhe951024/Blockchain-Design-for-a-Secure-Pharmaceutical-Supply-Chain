package com.rbpsc.ctp.repository.service;

import com.rbpsc.ctp.api.entities.work_request.WorkLoadRecord;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/27
 **/
public interface WorkLoadRecordRepository {
    void insertWorkLoadRecord(WorkLoadRecord workLoadRecord);

    void deleteWorkLoadRecord(WorkLoadRecord workLoadRecord);

    boolean updateWorkLoadRecord(WorkLoadRecord workLoadRecord);

    WorkLoadRecord selectWorkLoadRecordById(WorkLoadRecord workLoadRecord);
}
