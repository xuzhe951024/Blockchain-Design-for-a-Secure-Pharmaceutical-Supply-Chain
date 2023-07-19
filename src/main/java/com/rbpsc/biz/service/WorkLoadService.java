package com.rbpsc.biz.service;

import org.rbpsc.api.entities.work_request.WorkLoadReq;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/
public interface WorkLoadService{
    String generateWorkLoad(WorkLoadReq workLoadReq) throws InterruptedException;

    String saveToDB(String url);
}
