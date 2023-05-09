package com.rbpsc.ctp.api.entities.dto;


import com.rbpsc.ctp.api.entities.supplychain.roles.RoleBase;
import lombok.Data;

@Data
public class OperationVO<T> {
    String operationType;
    RoleBase operation;
}