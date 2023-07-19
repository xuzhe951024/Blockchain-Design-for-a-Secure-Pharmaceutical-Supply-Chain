package org.rbpsc.api.entities.dto;

import org.rbpsc.api.entities.base.BaseEntity;
import org.rbpsc.api.entities.supplychain.drug.DrugInfo;
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
