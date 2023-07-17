package main.java.com.rbpsc.ctp.api.entities.supplychain.operations;

import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.Institution;
import main.java.com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DrugOrderStep extends OperationBase {
}
