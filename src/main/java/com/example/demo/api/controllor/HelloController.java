package com.example.demo.api.controllor;

import com.example.demo.biz.service.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/

@RestController
@RequestMapping(value = "/WorkLoad/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HelloController {
    @Autowired
    private Hello helloWL;

    @GetMapping(value = "/hello")
    public String sayHello(){
        return helloWL.say();
    }
}
