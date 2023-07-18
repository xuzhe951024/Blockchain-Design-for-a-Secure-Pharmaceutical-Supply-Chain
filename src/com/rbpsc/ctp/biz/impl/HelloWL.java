package com.rbpsc.ctp.biz.impl;

import com.rbpsc.ctp.biz.service.Hello;
import org.springframework.stereotype.Service;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/
@Service
public class HelloWL implements Hello {
    @Override
    public String say() {
        return "helloWL";
    }

    @Override
    public String refuse() {
        return "NO!!";
    }
}
