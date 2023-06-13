package com.rbpsc.ctp.api.entities.supplychain.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Receipt extends OperationBase {
    private boolean isProcessed = true;
}
