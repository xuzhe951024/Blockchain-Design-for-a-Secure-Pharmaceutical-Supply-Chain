package com.rbpsc.ctp.api.entities.dto.webview;

import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.supplychain.drug.DrugLifeCycle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SimulationDataView extends SupplyChainBaseEntity {
    List<DrugLifeCycle> drugLifeCycleList;
    Long simulationCreateTime;
    String osName;
    String osVersion;
    String osArch;
    String cpuName;
    int availableCPUCoreNum;
    long freeMem;
    long totalMem;
}
