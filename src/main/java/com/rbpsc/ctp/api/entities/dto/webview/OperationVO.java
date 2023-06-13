package com.rbpsc.ctp.api.entities.dto.webview;

import com.rbpsc.ctp.api.entities.base.BaseEntity;
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
