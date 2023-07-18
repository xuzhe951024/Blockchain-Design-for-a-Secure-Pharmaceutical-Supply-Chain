package com.rbpsc.ctp.configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @project: Blockchain-Design-for-a-Secure-Pharmaceutical-Supply-Chain
 * @description:
 * @author: zhexu
 * @create: 7/9/23
 **/

@Configuration
public class DockerConfiguration {
    @Bean
    public DockerClient dockerClient() {
        return DockerClientBuilder.getInstance().build();
    }
}
