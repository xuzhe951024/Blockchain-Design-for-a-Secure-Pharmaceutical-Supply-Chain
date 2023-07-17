package main.java.com.rbpsc.ctp.api.entities.dto;

import main.java.com.rbpsc.ctp.api.entities.base.BaseEntity;
import main.java.com.rbpsc.ctp.api.entities.supplychain.drug.DrugInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @project: WorkLoad
 * @description:
 * @author: zhexu
 * @create: 6/17/23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DrugOperationDTO extends BaseEntity<String> {
    DrugInfo drug;
    OperationDTO operationDTO;
    String expectedReceiver;
}
