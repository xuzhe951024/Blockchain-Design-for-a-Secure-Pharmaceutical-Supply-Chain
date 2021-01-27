package com.example.demo.biz.service;

import com.example.demo.entities.WorkLoadReq;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/
public interface WorkLoadService{
    public String generateWorkLoad(WorkLoadReq workLoadReq) throws InterruptedException;

    public String saveToDB(String url);
}
