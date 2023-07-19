package com.rbpsc.api.controllor.v1;


import org.rbpsc.api.entities.work_request.WorkLoadReq;
import com.rbpsc.biz.service.WorkLoadService;
import com.rbpsc.configuration.v1prefix.V1RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/
@V1RestController
@RequestMapping(value = "/WorkLoad/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WorkLoadController {
    private final WorkLoadService workLoadService;

    public WorkLoadController(WorkLoadService workLoadService) {
        this.workLoadService = workLoadService;
    }


    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    @ResponseBody
    public String workLoadNormal(WorkLoadReq workLoadReq) throws InterruptedException {
        return workLoadService.generateWorkLoad(workLoadReq);
    }

    @RequestMapping(value = "/saveToDB", method = RequestMethod.GET)
    @ResponseBody
    public String saveToDB(String url) {
        return workLoadService.saveToDB(url);
    }
}
