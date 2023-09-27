package com.r3.developers.samples.obligation.etities.base;


import static com.r3.developers.samples.obligation.etities.constant.EntityConstants.BASE_ENTITY_ID_FIELD_NAME;

public class BaseEntity<T> {
    private T id;

    private String idFieldName = BASE_ENTITY_ID_FIELD_NAME;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }
}
