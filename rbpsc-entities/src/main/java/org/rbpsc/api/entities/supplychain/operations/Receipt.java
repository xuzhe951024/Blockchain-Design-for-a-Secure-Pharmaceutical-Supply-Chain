package org.rbpsc.api.entities.supplychain.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@DataType()
public class Receipt extends OperationBase {
    @Property()
    private boolean isProcessed = true;
}
