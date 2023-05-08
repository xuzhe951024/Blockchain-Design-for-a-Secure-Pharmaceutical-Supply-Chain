package com.rbpsc.ctp.api.entities.supplychain.drug;

import com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import com.rbpsc.ctp.api.entities.dto.OperationVO;
import com.rbpsc.ctp.api.entities.supplychain.roles.Consumer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Queue;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class DrugLifeCycle extends SupplyChainBaseEntity {
    DrugInfo drug;
    Queue<OperationVO> operationVOList;
    Consumer expectedReceiver;
}
