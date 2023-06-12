package com.rbpsc.ctp;

import com.rbpsc.ctp.api.entities.base.BaseEntity;
import com.rbpsc.ctp.api.entities.base.BaseResponse;
import com.rbpsc.ctp.api.entities.dto.response.DrugLifeCycleResponse;
import com.rbpsc.ctp.api.entities.factories.DataEntityFactory;
import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import com.rbpsc.ctp.api.entities.supplychain.operations.attack.AttackAvailability;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import com.rbpsc.ctp.common.utiles.WebClientUtil;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import reactor.core.publisher.Mono;

import java.util.*;

import static com.rbpsc.ctp.common.Constant.ServiceConstants.RESPONSE_CODE_FAIL_SERVICE_DISABLED;

@Slf4j
public class PlayGround {
    public static void main(String[] args) throws InterruptedException {
//        Queue<String> queue = new ArrayDeque<>(Arrays.asList("a", "b", "c"));
//        System.out.printf("queue poll = %s%n", queue.poll());
//        queue.remove();
//        System.out.printf("queue = %s%n", queue);
//
//        System.out.println("queue class name: " + queue.getClass().getName());
//
//        Consumer consumer = new Consumer();
//        BaseEntity<String> entity = (BaseEntity<String>) consumer;
//        System.out.println("entity class:" + entity.getClass().getName());
//
//        AttackAvailability attackAvailability = new AttackAvailability();
//        System.out.println(attackAvailability);
//
//        SupplyChainBaseEntity supplyChainBaseEntity = new SupplyChainBaseEntity();
//        System.out.println(supplyChainBaseEntity);
//
//        DrugLifeCycleResponse drugLifeCycleResponse = new DrugLifeCycleResponse();
//        System.out.println(drugLifeCycleResponse);
//
//        attackAvailability.setRoleName("1");
//        attackAvailability.setTargetDrugId("2");

//        try (ScanResult scanResult = new ClassGraph().whitelistPackages("com.rbpsc.ctp.api.entities.supplychain.operations").scan()) {
//            scanResult.getAllClasses().forEach(classInfo -> System.out.println(classInfo.getName()));
//        }

        SystemInfo si = new SystemInfo();
        CentralProcessor processor = si.getHardware().getProcessor();
        System.out.println(processor);
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available cores: " + cores);
        GlobalMemory memory = si.getHardware().getMemory();

        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();

        System.out.println("Total Memory: " + totalMemory/(1024*1024));
        System.out.println("Available Memory: " + availableMemory/(1024*1024));

        long swapUsed = memory.getVirtualMemory().getSwapUsed();
        long swapTotal = memory.getVirtualMemory().getSwapTotal();

        System.out.println("Swap used: " + swapUsed/(1024*1024));
        System.out.println("Swap total: " + swapTotal/(1024*1024));


    }
}
