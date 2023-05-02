package com.rbpsc.ctp.repository.service;

import com.rbpsc.ctp.api.entities.WorkLoadRecord;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/27
 **/
public interface WorkLoadRecordRepository {
    public void insert(WorkLoadRecord workLoadRecord);

    public void delete(WorkLoadRecord workLoadRecord);

    public boolean update(String id, String field, String value);

    public WorkLoadRecord selectById(String id);
}
