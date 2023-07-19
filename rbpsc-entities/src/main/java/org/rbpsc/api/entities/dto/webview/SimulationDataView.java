package main.java.org.rbpsc.api.entities.dto.webview;

import main.java.org.rbpsc.api.entities.supplychain.SupplyChainBaseEntity;
import main.java.org.rbpsc.api.entities.supplychain.drug.DrugLifeCycle;
import main.java.org.rbpsc.api.entities.dto.OperationDTO;
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
    private List<DrugLifeCycle<OperationDTO>> drugLifeCycleList;
    private Long simulationCreateTime;
    private String osName;
    private String osVersion;
    private int osBitness;
    private String cpuInfo;
    private int availableCPUCoreNum;
    private long freeMem;
    private long totalMem;
    private long swapMemTotal;
    private long swapMemUsed;

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
        this.totalMem = totalMemory;
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
