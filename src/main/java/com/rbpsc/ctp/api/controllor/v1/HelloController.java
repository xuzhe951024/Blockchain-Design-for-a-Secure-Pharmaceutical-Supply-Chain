package com.rbpsc.ctp.api.controllor.v1;

import com.rbpsc.ctp.biz.service.Hello;
import com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rbpsc.ctp.common.Constant.ServiceConstants.API_ENABLED;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/

@V1RestController
@RequestMapping(value = "/WorkLoad/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HelloController {

    @Autowired
    private Hello helloWL;

    @GetMapping(value = "/hello")
    public String sayHello(){
        return API_ENABLED.get()? helloWL.say(): helloWL.refuse();
    }


}
