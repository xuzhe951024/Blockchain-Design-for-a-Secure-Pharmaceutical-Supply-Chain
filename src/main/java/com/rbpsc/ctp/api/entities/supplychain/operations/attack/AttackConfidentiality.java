package com.rbpsc.ctp.api.entities.supplychain.operations.attack;

import com.rbpsc.ctp.api.entities.supplychain.operations.OperationBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttackConfidentiality extends OperationBase {
    private String ReplacedExampleMsg;
    private String exampleMsg;
}
