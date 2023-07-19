package main.java.org.rbpsc.api.entities.base;

import lombok.Data;
import org.springframework.data.annotation.Id;

import static main.java.org.rbpsc.common.constant.EntityConstants.BASE_ENTITY_ID_FIELD_NAME;

@Data
public class BaseEntity<T> {

    @Id
    private T id;

    private String idFieldName = BASE_ENTITY_ID_FIELD_NAME;
}
