package com.rbpsc.ctp.api.entities.dto.webview;

import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

import java.util.List;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SimulationDataView extends SupplyChainBaseEntity {
    List<DrugLifeCycle> drugLifeCycleList;
    Long simulationCreateTime;
    String osName;
    String osVersion;
    int osBitness;
    String cpuInfo;
    int availableCPUCoreNum;
    long freeMem;
    long totalMem;
    long swapMemTotal;
    long swapMemUsed;

    public void detectEnvironment(){
        this.simulationCreateTime = System.currentTimeMillis();

        SystemInfo si = new SystemInfo();

        OperatingSystem os = si.getOperatingSystem();
        this.osName = os.getFamily();
        this.osVersion = os.getVersionInfo().toString();
        this.osBitness = os.getBitness();
        log.info("Operating System: " + os.getFamily() + " " + os.getBitness() + "-bit" + " " + os.getVersionInfo());

        CentralProcessor processor = si.getHardware().getProcessor();
        this.cpuInfo = processor.toString();
        log.info(this.cpuInfo);
        int cores = Runtime.getRuntime().availableProcessors();
        this.availableCPUCoreNum = cores;
        log.info("Available cores: " + cores);
        GlobalMemory memory = si.getHardware().getMemory();

        long totalMemory = memory.getTotal();
        this.totalMem = totalMem;
        long availableMemory = memory.getAvailable();
        this.freeMem = availableMemory;

        log.info("Total Memory: " + totalMemory/(1024*1024) + " MB");
        log.info("Available Memory: " + availableMemory/(1024*1024) + " MB");

        long swapUsed = memory.getVirtualMemory().getSwapUsed();
        this.swapMemUsed = swapUsed;
        long swapTotal = memory.getVirtualMemory().getSwapTotal();
        this.swapMemTotal = swapTotal;

        log.info("Swap used: " + swapUsed/(1024*1024) + " MB");
        log.info("Swap total: " + swapTotal/(1024*1024) + " MB");
    }
}
