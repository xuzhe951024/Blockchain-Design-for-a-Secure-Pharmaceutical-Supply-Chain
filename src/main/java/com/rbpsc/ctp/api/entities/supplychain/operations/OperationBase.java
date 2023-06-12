package com.rbpsc.ctp.api.entities.supplychain.operations;

import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class OperationBase extends RoleBase {
    String targetBatchId;
    String targetDrugId;
    String operationMSG;
}
