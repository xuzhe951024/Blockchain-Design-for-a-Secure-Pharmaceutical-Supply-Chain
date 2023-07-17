package main.java.com.rbpsc.ctp.api.entities.dto;


import main.java.com.rbpsc.ctp.api.entities.base.BaseEntity;
import main.java.com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
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