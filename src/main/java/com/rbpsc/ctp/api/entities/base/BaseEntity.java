package com.rbpsc.ctp.api.entities.base;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class BaseEntity<T> {

    @Id
    private T id;

    private String idFieldName;
}
