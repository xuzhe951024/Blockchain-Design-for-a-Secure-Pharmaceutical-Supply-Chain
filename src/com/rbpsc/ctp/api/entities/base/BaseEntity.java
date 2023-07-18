package com.rbpsc.ctp.api.entities.base;

import lombok.Data;
import org.springframework.data.annotation.Id;

import static com.rbpsc.ctp.common.Constant.EntityConstants.BASE_ENTITY_ID_FIELD_NAME;

@Data
public class BaseEntity<T> {

    @Id
    private T id;

    private String idFieldName = BASE_ENTITY_ID_FIELD_NAME;
}
