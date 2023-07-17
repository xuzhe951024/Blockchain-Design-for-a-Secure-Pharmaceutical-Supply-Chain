package main.java.com.rbpsc.ctp.api.controllor.v1;


import main.java.com.rbpsc.ctp.biz.service.Hello;
import main.java.com.rbpsc.ctp.configuration.v1prefix.V1RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static main.java.com.rbpsc.ctp.common.Constant.ServiceConstants.API_ENABLED;


/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/

@V1RestController
@RequestMapping(value = "/WorkLoad/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HelloController {

    private final Hello helloWL;

    public HelloController(Hello helloWL) {
        this.helloWL = helloWL;
    }

    @GetMapping(value = "/hello")
    public String sayHello(){
        return API_ENABLED.get()? helloWL.say(): helloWL.refuse();
    }


}
