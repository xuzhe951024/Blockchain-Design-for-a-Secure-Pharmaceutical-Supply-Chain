package main.java.org.rbpsc.api.entities.dto.webview;

import main.java.org.rbpsc.api.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperationVO extends BaseEntity<String> {
    private String operationType;
    private String operationMsg;
    private String operatorAdd;
}
