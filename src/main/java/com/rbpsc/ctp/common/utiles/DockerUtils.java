/**
 * @project: Blockchain-Design-for-a-Secure-Pharmaceutical-Supply-Chain
 * @description:
 * @author: zhexu
 * @create: 7/9/23
 **/

package main.java.com.rbpsc.ctp.common.utiles;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static main.java.com.rbpsc.ctp.common.Constant.ServiceConstants.*;


@Service
@Slf4j
public class DockerUtils {

    private final DockerClient dockerClient;
    final SimpMessagingTemplate simpMessagingTemplate;

    public DockerUtils(DockerClient dockerClient, SimpMessagingTemplate simpMessagingTemplate) {
        this.dockerClient = dockerClient;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void generateDockerfile() {
        //TODO: Fill this method
    }

    public String buildImage(String dockerFilePath, String imageName) {
        generateDockerfile();
        String imageId = this.dockerClient.buildImageCmd()
                .withLabels(DOCKER_LABEL_MAP)
                .withDockerfile(new File(dockerFilePath))
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        dockerClient.tagImageCmd(imageId, imageName, DOCKER_TAG_LATEST).exec();
        return imageId;
    }

    public CreateServiceResponse createAndStartService(String imageId, String serviceAlias, List<String> envList) {
        CreateServiceResponse service = dockerClient.createServiceCmd(new ServiceSpec()
                .withName(serviceAlias)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage(imageId)
                                .withEnv(envList)
                                .withTty(true)
                        )
                )
                .withNetworks(Collections.singletonList(new NetworkAttachmentConfig()
                        .withTarget(DOCKER_NETWORK_NAME)
                        .withAliases(Collections.singletonList(serviceAlias))
                ))
        ).exec();
        return service;
    }

    public String createAndStartContainer(String networkName, String imageId, String containerAlias, List<String> envList) {
        CreateContainerResponse container = dockerClient.createContainerCmd(imageId)
                .withName(containerAlias)
                .withEnv(envList)
                .withTty(true)
                .withNetworkMode(networkName)
                .withAliases(containerAlias)
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        return container.getId();
    }

    public String createNetWork(String networkName) {
        CreateNetworkResponse networkResponse = dockerClient.createNetworkCmd()
                .withName(networkName)
                .exec();

        return networkResponse.getId();
    }

    public void deleteNetWork(String networkId) {
        try {
            dockerClient.removeNetworkCmd(networkId).exec();
        } catch (NotFoundException e) {
            log.error(e.toString());
        }
    }

    public void deleteImage(String imageId, boolean forceDel) {
        try {
            dockerClient.removeImageCmd(imageId).withForce(forceDel).exec();
        } catch (NotFoundException e) {
            log.error(e.toString());
        }
    }

    public void stopAndDeleteContainer(String containerId) {
        try {
            dockerClient.stopContainerCmd(containerId).exec();
            dockerClient.removeContainerCmd(containerId).exec();
        } catch (NotFoundException | NotModifiedException e) {
            log.error(e.toString());
        }
    }

    public void deleteService(String serviceId) {
        try {
            dockerClient.removeServiceCmd(serviceId).exec();
        } catch (NotFoundException e) {
            log.error(e.toString());
        }
    }

    public void waitForContainerStarting(String[] containerIds, String targetLogMessage, String uuid){

        ExecutorService executorService = Executors.newFixedThreadPool(containerIds.length);
        CountDownLatch latch = new CountDownLatch(containerIds.length);

        for (String containerId : containerIds) {
            executorService.execute(() -> watchContainerLog(dockerClient, simpMessagingTemplate, containerId, targetLogMessage, latch, uuid));
        }

        executorService.shutdown();

        try {
            latch.await();
            log.info("All container log monitoring finished.");
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for container log monitoring to finish: " + e.getMessage());
        }
    }

    private void watchContainerLog(DockerClient docker, SimpMessagingTemplate simpMessagingTemplate, String containerId, String targetLogMessage, CountDownLatch latch, String uuid) {
        try {
            ResultCallback<Frame> callback = new LogContainerResultCallback() {
                @Override
                public void onNext(Frame item) {
                    String logMessage = item.toString();
                    if (logMessage.contains(targetLogMessage)) {

                        log.info("Target log message found in container " + containerId + ": " + logMessage);
                        latch.countDown();
                        log.info("Task: Waiting for container to start finished! latchCount:" + latch.getCount());
                        simpMessagingTemplate.convertAndSend(WEB_SCOKET_TOPIC_PROGRESS + uuid, "Task: Waiting for container to start finished! latchCount:" + latch.getCount());
                    }
                }
            };

            docker.logContainerCmd(containerId)
                    .withStdOut(true)
                    .withStdErr(true)
                    .withFollowStream(true)
                    .exec(callback);
        } catch (DockerException e) {
            log.error("Failed to monitor container log for " + containerId + ": " + e.getMessage());
            latch.countDown();
        }
    }
}

