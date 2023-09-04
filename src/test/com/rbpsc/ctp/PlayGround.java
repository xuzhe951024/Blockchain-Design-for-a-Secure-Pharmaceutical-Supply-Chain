package com.rbpsc.ctp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rbpsc.common.factories.DataEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.rbpsc.api.entities.supplychain.drug.DrugInfo;
import org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import org.rbpsc.api.entities.supplychain.operations.OperationBase;
import org.rbpsc.api.entities.supplychain.operations.Receipt;
import org.rbpsc.api.entities.supplychain.roles.Consumer;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class PlayGround {

    public static DrugLifeCycle<Receipt> buildDrugCycle(String drugName){

        OperationBase operationBase = new OperationBase();
        operationBase.setId(UUID.randomUUID().toString());
        operationBase.setOperationMSG("Test MSG 1");
        operationBase.setBatchId("test");

        Receipt receipt = DataEntityFactory.createReceipt(operationBase);
        receipt.setRoleName("testRole");
        receipt.setAddress("http://manufacture0-T/v1/drugLifeCycle/drugOrderStep/manufacture");
        receipt.setBatchId("test");
        DrugInfo drugInfo = DataEntityFactory.createDrugInfo(operationBase, drugName);
        drugInfo.setDrugTagTagId(UUID.randomUUID().toString());

        DrugLifeCycle<Receipt> drugLifeCycle = DataEntityFactory.createDrugLifeCycleReceipt(drugInfo);
        drugLifeCycle.addOperation(receipt);

        Consumer consumer = DataEntityFactory.createConsumer(1, "http://manufacture0-T/v1/consumer", "test", Optional.of(UUID.randomUUID().toString()));
        drugLifeCycle.setExpectedReceiver(consumer);

        return drugLifeCycle;
    }
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
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

//        try (ScanResult scanResult = new ClassGraph().whitelistPackages("main.java.com.rbpsc.ctp.api.entities.supplychain.operations").scan()) {
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

        OperatingSystem os = si.getOperatingSystem();

        System.out.println("Operating System: " + os.getFamily() + " " + os.getVersionInfo());

        System.out.println("http://manufacture0-T/v1/drugLifeCycle/drugOrderStep/manufacture".split("/")[2]);
        AtomicInteger index = new AtomicInteger();
        for (int i = 0; i <5; i++) {

            log.info(index.getAndIncrement() + "");
        }
//        DrugLifeCycle<Receipt> covidVaccine = buildDrugCycle("covid-vaccine");
//        ObjectMapper objectMapper = new ObjectMapper();
//        log.info(objectMapper.writeValueAsString(covidVaccine));
    }
}
