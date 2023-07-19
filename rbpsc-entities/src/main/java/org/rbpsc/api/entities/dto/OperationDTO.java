package org.rbpsc.api.entities.dto;


import org.rbpsc.api.entities.base.BaseEntity;
import org.rbpsc.api.entities.supplychain.operations.OperationBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperationDTO extends BaseEntity<String> {
    private String operationType;
    private OperationBase operation;
}