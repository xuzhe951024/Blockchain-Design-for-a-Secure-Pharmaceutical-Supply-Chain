package main.java.org.rbpsc.api.entities.dto;


import main.java.org.rbpsc.api.entities.base.BaseEntity;
import main.java.org.rbpsc.api.entities.supplychain.operations.OperationBase;
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