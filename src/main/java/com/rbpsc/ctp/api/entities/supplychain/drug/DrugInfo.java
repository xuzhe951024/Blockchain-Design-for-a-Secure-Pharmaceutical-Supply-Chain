package main.java.com.rbpsc.ctp.api.entities.supplychain.drug;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import main.java.com.rbpsc.ctp.api.entities.supplychain.SupplyChainBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DrugInfo extends SupplyChainBaseEntity {
    private String drugTagTagId;
    private String drugName;
    private boolean isFake = false;
    private boolean isRecalled = false;
}
