package com.example.demo;

import com.example.demo.biz.service.WorkLoadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private WorkLoadService workLoadService;

    @Test
    void contextLoads() {
    }

    @Test
    void testSaveToDB(){
        workLoadService.saveToDB("test");
    }

}
