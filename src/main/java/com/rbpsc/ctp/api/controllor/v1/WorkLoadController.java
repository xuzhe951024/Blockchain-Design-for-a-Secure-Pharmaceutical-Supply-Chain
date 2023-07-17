package main.java.com.rbpsc.ctp.api.controllor.v1;


import main.java.com.rbpsc.ctp.api.entities.work_request.WorkLoadReq;
import main.java.com.rbpsc.ctp.biz.service.WorkLoadService;
import main.java.com.rbpsc.ctp.configuration.v1prefix.V1RestController;
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
    public String saveToDB(String url) throws InterruptedException {
        return workLoadService.saveToDB(url);
    }
}
