package com.rbpsc.ctp.api.entities.dto;


import com.rbpsc.ctp.api.entities.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperationDTO extends BaseEntity<String> {
    private String operationType;
    private String operation;
}