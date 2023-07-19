package main.java.org.rbpsc.api.entities.supplychain.operations;

import main.java.org.rbpsc.api.entities.supplychain.roles.RoleBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class OperationBase extends RoleBase {
    private String operationMSG;
}
