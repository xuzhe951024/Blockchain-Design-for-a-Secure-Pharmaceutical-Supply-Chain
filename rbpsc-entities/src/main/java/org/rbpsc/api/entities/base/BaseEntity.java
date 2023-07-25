package org.rbpsc.api.entities.base;

import lombok.Data;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.springframework.data.annotation.Id;

import static org.rbpsc.common.constant.EntityConstants.BASE_ENTITY_ID_FIELD_NAME;

@Data
@DataType()
public class BaseEntity<T> {

    @Property()
    @Id
    private T id;

    private String idFieldName = BASE_ENTITY_ID_FIELD_NAME;
}
