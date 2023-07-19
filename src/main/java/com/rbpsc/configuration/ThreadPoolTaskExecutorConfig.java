package main.java.com.rbpsc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/16/23
 **/

@Configuration
public class ThreadPoolTaskExecutorConfig {
    @Bean(name = "SimulatorExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cores = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(cores);
        executor.setMaxPoolSize(cores * 2);
        executor.setQueueCapacity(200);
        executor.initialize();
        return executor;
    }
}
